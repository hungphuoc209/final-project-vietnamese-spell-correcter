package dut.finalproject.s106180042.ext

import android.app.Application
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.media.ThumbnailUtils
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import dut.finalproject.s106180042.BuildConfig
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MediaManager
@Inject
constructor(val application: Application) {
    companion object {
        const val AttachCameraForVideoRequestCode = 999
        const val AttachCameraForPhotoRequestCode = 1000
        const val AttachGalleryForPhotoRequestCode = 1001
        const val AttachGalleryForVideoRequestCode = 1002
    }


    /**
     * Open camera and record videos
     */

    /**
     * Open camera, take photo and get uri
     * @return uri value
     */
    fun dispatchTakePictureIntent(fragment: Fragment): Uri? {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(application.packageManager)?.also {
                val photoFile: File? = createImageFile()

                return if (photoFile != null) {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        application,
                        "${BuildConfig.APPLICATION_ID}.provider",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    fragment.startActivityForResult(
                        takePictureIntent,
                        AttachCameraForPhotoRequestCode
                    )
                    photoURI
                } else {
                    null
                }
            }
            return null
        }
    }

    /**
     * Open camera, take photo and get uri
     * @return uri value
     */
    fun dispatchTakePictureIntent(
        fragmentActivity: FragmentActivity
    ): Uri? {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            // Ensure that there's a camera activity to handle the intent
            takePictureIntent.resolveActivity(application.packageManager)?.also {
                val photoFile: File? = createImageFile()

                return if (photoFile != null) {
                    val photoURI: Uri = FileProvider.getUriForFile(
                        application,
                        "${BuildConfig.APPLICATION_ID}.provider",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    fragmentActivity.startActivityForResult(
                        takePictureIntent,
                        AttachCameraForPhotoRequestCode
                    )
                    photoURI
                } else {
                    null
                }
            }
            return null
        }
    }

    /**
     * Select image from gallery
     */
    fun dispatchGalleryPhotoIntent(fragment: FragmentActivity) {
        val pickPhotoIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        ).apply {
            type = "image/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        fragment.startActivityForResult(pickPhotoIntent, AttachGalleryForPhotoRequestCode)
    }

    /**
     * Select video from gallery
     */
    fun dispatchGalleryVideoIntent(fragmentActivity: FragmentActivity) {
        val pickVideoIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        ).apply {
            type = "video/*"
            putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        fragmentActivity.startActivityForResult(pickVideoIntent, AttachGalleryForVideoRequestCode)
    }

    /**
     * Select path of image thumbnail video
     * @param uri: Uri get from data result
     */
    fun getPathVideo(uri: Uri?) = getRealFilePathForImage(uri)

    /**
     * Select path of image
     * @param uri: Uri get from data result
     */
    fun getPathPhoto(uri: Uri?, parent: String = "") = getRealFilePathForImage(uri, parent)

    /**
     * Get thumbnail image
     */
    fun getThumbnailFromIntent(data: Intent?): Bitmap? = data?.extras?.get("data") as? Bitmap

    /**
     * Get thumbnail video
     */
    fun getThumbnailVideo(uri: String) {
        ThumbnailUtils.createVideoThumbnail(uri, MediaStore.Images.Thumbnails.MINI_KIND)
    }

    /**
     * Create image file
     */
    private fun createImageFile(parent: String = ""): File? {
        // Create an image file name
        val timeStamp: String = SimpleDateFormat("dd-MM-yyyy-HH-mm-ss", Locale.ROOT).format(Date())
        val storageDir = File(application.filesDir, parent)
        if (!storageDir.isDirectory) {
            storageDir.mkdir()
        }

        return try {
            File.createTempFile(
                "Image_${timeStamp}_${System.currentTimeMillis()}",
                ".jpg",
                storageDir
            )
        } catch (e: IOException) {
            e.printStackTrace()
            null
        }
    }

    /**
     * Get real file path from URI
     */
    private fun getRealFilePathForImage(uri: Uri?, parent: String = ""): String? {
        val returnCursor: Cursor? =
            uri?.let { application.contentResolver.query(it, null, null, null, null) }
        /**
         * Get the column indexes of the data in the Cursor,
         *  move to the first row in the Cursor, get the data,
         *  and display it.
         * */
        return if (returnCursor != null) {
            val nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            val sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE)
            returnCursor.moveToFirst()
            val name = "${System.currentTimeMillis()}-${returnCursor.getString(nameIndex)}"
            val size = returnCursor.getLong(sizeIndex).toString()
            val destFile = File(application.filesDir, parent)
            if (!destFile.isDirectory) {
                destFile.mkdir()
            }
            val file = File(destFile, name)
            try {
                val inputStream: InputStream? = application.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                var read = 0
                val maxBufferSize = 1 * 1024 * 1024
                val bytesAvailable: Int? = inputStream?.available()

                //int bufferSize = 1024;
                val bufferSize = bytesAvailable?.coerceAtMost(maxBufferSize)
                val buffers = bufferSize?.let { ByteArray(it) }
                while (inputStream?.read(buffers).also {
                        if (it != null) {
                            read = it
                        }
                    } != -1) {
                    outputStream.write(buffers, 0, read)
                }
                inputStream?.close()
                outputStream.close()
                returnCursor.close()
            } catch (e: Exception) {
                Log.e("Exception", e.localizedMessage ?: "")
            }

            file.path
        } else {
            null
        }
    }
}