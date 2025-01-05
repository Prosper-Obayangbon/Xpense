import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.io.IOException

/**
 * A composable function that presents a dialog with options to pick an image from the gallery
 * or take a picture using the camera.
 *
 * @param onImagePicked A lambda function that receives the URI of the picked image.
 * @param onDismiss A lambda function that is called when the dialog is dismissed.
 */
@Composable
fun ImagePickerDialog(
    onImagePicked: (Uri) -> Unit,
    onDismiss: () -> Unit,
    context: Context
) {


    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { onImagePicked(it) }
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                val uri = saveBitmapToUri(context, bitmap)
                if (uri != Uri.EMPTY) {
                    onImagePicked(uri)
                } else {
                    Toast.makeText(context, "Failed to save image.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        } else {
            Toast.makeText(context, "Camera permission is required.", Toast.LENGTH_SHORT).show()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Choose an option") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Button(onClick = {
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Pick from Gallery")
                }

                Button(onClick = {
                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraLauncher.launch(null)
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Text("Take a Picture")
                }
            }
        },
        confirmButton = {},
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}


/**
 * Saves a bitmap image to the external storage and returns its URI.
 * This method ensures that the image is saved as a JPEG file.
 *
 * @param context The context used to access the content resolver.
 * @param bitmap The bitmap image to be saved.
 * @return The URI pointing to the saved image, or Uri.EMPTY if the operation fails.
 */
fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri {
    // Generate unique file name based on current timestamp
    val uniqueFileName = "IMG_${System.currentTimeMillis()}.jpg"

    // Create content values to specify file metadata
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, uniqueFileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }

    // Insert the content values into the MediaStore
    val uri = try {
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    // Check if the URI was created successfully
    uri?.let {
        try {
            // Open an output stream to write the bitmap to the URI
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                // Compress and write the bitmap as JPEG
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            }
        } catch (e: IOException) {
            e.printStackTrace()
            // If saving the image fails, return an empty URI
            Toast.makeText(context, "Error saving the image.", Toast.LENGTH_SHORT).show()
        }
    }

    // Return the URI or Uri.EMPTY if the operation failed
    return uri ?: Uri.EMPTY
}

