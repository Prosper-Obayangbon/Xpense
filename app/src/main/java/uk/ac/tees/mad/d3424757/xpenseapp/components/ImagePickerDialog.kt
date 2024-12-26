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
import androidx.compose.ui.platform.LocalContext
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
    onDismiss: () -> Unit
) {
    // Access the context
    val context = LocalContext.current

    // Gallery launcher to pick an image
    val galleryLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            uri?.let { onImagePicked(it) } // When an image is picked, call onImagePicked
        }

    // Camera launcher to take a picture
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
            bitmap?.let {
                // Save the bitmap as a URI and pass it to onImagePicked
                val uri = saveBitmapToUri(context, bitmap)
                if (uri != Uri.EMPTY) {
                    onImagePicked(uri)
                } else {
                    // Show a Toast if image saving failed
                    Toast.makeText(context, "Failed to save image.", Toast.LENGTH_SHORT).show()
                }
            }
        }

    // Permission launcher for camera access
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            // If permission granted, launch the camera
            cameraLauncher.launch(null)
        } else {
            // If permission denied, show a Toast
            Toast.makeText(context, "Camera permission is required.", Toast.LENGTH_SHORT).show()
        }
    }

    // AlertDialog with options for the user to pick an image from the gallery or take a picture
    AlertDialog(
        onDismissRequest = onDismiss, // Close dialog on dismiss
        title = { Text("Choose an option") }, // Title of the dialog
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Button to pick an image from the gallery
                Button(onClick = {
                    // Check if READ_EXTERNAL_STORAGE permission is granted
                    if (hasGalleryPermission(context)) {
                        galleryLauncher.launch("image/*") // Launch the gallery
                    } else {
                        Toast.makeText(context, "Permission to access gallery is required.", Toast.LENGTH_SHORT).show()
                    }
                }) {
                    Text("Pick from Gallery")
                }

                // Button to take a picture using the camera
                Button(onClick = {
                    // Check if camera permission is granted
                    if (ContextCompat.checkSelfPermission(
                            context, Manifest.permission.CAMERA
                        ) == PackageManager.PERMISSION_GRANTED
                    ) {
                        cameraLauncher.launch(null) // Launch the camera
                    } else {
                        permissionLauncher.launch(Manifest.permission.CAMERA) // Request camera permission
                    }
                }) {
                    Text("Take a Picture")
                }
            }
        },
        confirmButton = {}, // No confirm button
        dismissButton = {
            // Cancel button to dismiss the dialog
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

/**
 * Resizes a bitmap to fit within the specified maximum width and height, preserving aspect ratio.
 * This is useful when dealing with large bitmaps from the camera to reduce memory usage.
 *
 * @param bitmap The original bitmap to resize.
 * @param maxWidth The maximum width of the resized bitmap.
 * @param maxHeight The maximum height of the resized bitmap.
 * @return A resized bitmap.
 */
fun resizeBitmap(bitmap: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
    // Calculate aspect ratio
    val aspectRatio = bitmap.width.toFloat() / bitmap.height
    val width = if (bitmap.width > maxWidth) maxWidth else bitmap.width
    val height = (width / aspectRatio).toInt().coerceAtMost(maxHeight)

    // Return the resized bitmap
    return Bitmap.createScaledBitmap(bitmap, width, height, true)
}

/**
 * A helper function to check and request permissions before accessing the gallery.
 *
 * @param context The context used to check permissions.
 * @return True if the required permission is granted, false otherwise.
 */
fun hasGalleryPermission(context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
}
