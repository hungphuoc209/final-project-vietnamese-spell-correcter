package dut.finalproject.s106180042

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import dagger.hilt.android.AndroidEntryPoint
import dut.finalproject.s106180042.databinding.ActivityPreviewBinding
import dut.finalproject.s106180042.ext.MediaManager
import javax.inject.Inject

@AndroidEntryPoint
class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding

    @Inject
    lateinit var mediaManager: MediaManager

    private var cameraUri: Uri? = null
    private var imageUri: Uri? = null

    private val viewModel by viewModels<CheckingViewModel>()

    companion object {
        private const val REQUEST_CODE_CAMERA = 300
        private const val REQUEST_CODE_GALLERY = 222
    }

    private val permission = arrayOf(
        Manifest.permission.CAMERA
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        intent.getIntExtra("type", -1).let {
            if (it == 0) {
                if (isPermissionGranted(permission)) {
                    openCamera()
                } else {
                    requestPermission()
                }
            }
            if (it == 1) {
                openGallery()
            }
        }

        initListener()
        initObservers()
    }

    private fun initListener() {
        binding.btnRetry.setOnClickListener {
            intent.getIntExtra("type", -1).let {
                if (it == 0) {
                    if (isPermissionGranted(permission)) {
                        openCamera()
                    } else {
                        requestPermission()
                    }
                }
                if (it == 1) {
                    openGallery()
                }
            }
        }
        binding.btnDetect.setOnClickListener {
            imageUri?.let(viewModel::ocr)
        }
    }

    private fun initObservers() {
        viewModel.textOcr.observe(this) {
            val intent =
                Intent(this@PreviewActivity, AddSentencesActivity::class.java)
            intent.putExtra("text_key", it)
            startActivity(intent)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                    val cropResult = CropImage.getActivityResult(data)
                    imageUri = cropResult.uri
                    cropResult?.let {
                        Glide.with(this)
                            .load(imageUri)
                            .placeholder(R.drawable.bg_circle_progress)
                            .into(binding.imgPreview)
                    }
                }

                MediaManager.AttachCameraForPhotoRequestCode -> {
                    CropImage.activity(cameraUri).start(this)
                }

                REQUEST_CODE_GALLERY -> {
                    data?.data?.let {
                        CropImage.activity(it).start(this)
                    }
                }
            }
        }
    }

    private fun isPermissionGranted(permissions: Array<String>): Boolean = permissions.all {
        ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            permission,
            REQUEST_CODE_CAMERA
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if (!isPermissionGranted(permission)) {
                    Toast.makeText(this, "No have permission", Toast.LENGTH_LONG).show()
                } else {
                    openCamera()
                }
            }
        }
    }

    private fun openCamera() {
        cameraUri = mediaManager.dispatchTakePictureIntent(this)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("image/*")
            .addCategory(Intent.CATEGORY_OPENABLE)
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(
            Intent.createChooser(intent, "Select Image"),
            REQUEST_CODE_GALLERY
        )
    }
}