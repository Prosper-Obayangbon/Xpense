import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import java.io.IOException

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
 * Saves a bitmap image to the appropriate storage based on the Android version.
 * For Android 10 and above, it uses MediaStore. For Android 9 and below, it requests WRITE_EXTERNAL_STORAGE permission.
 *
 * @param context The context used to access the content resolver and request permissions.
 * @param bitmap The bitmap image to be saved.
 * @return The URI pointing to the saved image, or Uri.EMPTY if the operation fails.
 */
fun saveBitmapToUri(context: Context, bitmap: Bitmap): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        // Use MediaStore for scoped storage (Android 10 and above)
        saveImageUsingMediaStore(context, bitmap)
    } else {
        // For Android 9 and below, use WRITE_EXTERNAL_STORAGE permission
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            == PackageManager.PERMISSION_GRANTED
        ) {
            saveImageToExternalStorage(context, bitmap)
        } else {
            requestStoragePermission(context)
            Uri.EMPTY
        }
    }
}

/**
 * Saves a bitmap to MediaStore for devices running Android 10 and above (Scoped Storage).
 *
 * @param context The context used to access the content resolver.
 * @param bitmap The bitmap image to be saved.
 * @return The URI pointing to the saved image, or Uri.EMPTY if the operation fails.
 */
fun saveImageUsingMediaStore(context: Context, bitmap: Bitmap): Uri {
    val uniqueFileName = "IMG_${System.currentTimeMillis()}.jpg"

    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.DISPLAY_NAME, uniqueFileName)
        put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
    }

    val uri: Uri? = try {
        context.contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }

    return uri?.let {
        try {
            context.contentResolver.openOutputStream(it)?.use { outputStream ->
                if (!bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)) {
                    throw IOException("Failed to compress bitmap")
                }
            }
            it // Successfully saved, return the URI
        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(context, "Error saving the image.", Toast.LENGTH_SHORT).show()
            Uri.EMPTY
        }
    } ?: Uri.EMPTY
}

/**
 * Saves a bitmap to external storage for devices running Android 9 and below.
 *
 * @param context The context used to access the content resolver.
 * @param bitmap The bitmap image to be saved.
 * @return The URI pointing to the saved image, or Uri.EMPTY if the operation fails.
 */
fun saveImageToExternalStorage(context: Context, bitmap: Bitmap): Uri {
    val uniqueFileName = "IMG_${System.currentTimeMillis()}.jpg"
    val externalStorageDir = context.getExternalFilesDir(null)

    if (externalStorageDir == null) {
        Toast.makeText(context, "External storage is not available.", Toast.LENGTH_SHORT).show()
        return Uri.EMPTY
    }

    val file = java.io.File(externalStorageDir, uniqueFileName)

    return try {
        val fileOutputStream = file.outputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.flush()
        fileOutputStream.close()

        Uri.fromFile(file) // Return the URI of the saved image
    } catch (e: IOException) {
        e.printStackTrace()
        Toast.makeText(context, "Error saving the image.", Toast.LENGTH_SHORT).show()
        Uri.EMPTY
    }
}

/**
 * Requests WRITE_EXTERNAL_STORAGE permission if needed for Android 9 and below.
 *
 * @param context The context used to request the permission.
 */
fun requestStoragePermission(context: Context) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(
            context as Activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
        // Show a rationale to the user
        Toast.makeText(context, "Storage permission is required to save images.", Toast.LENGTH_SHORT).show()
    }
    ActivityCompat.requestPermissions(context, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 100)
}
