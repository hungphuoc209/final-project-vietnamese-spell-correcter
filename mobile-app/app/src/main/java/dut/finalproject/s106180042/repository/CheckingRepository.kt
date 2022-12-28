package dut.finalproject.s106180042.repository

import android.net.Uri
import android.util.Log
import com.github.michaelbull.result.Result
import com.github.michaelbull.result.coroutines.runSuspendCatching
import dut.finalproject.s106180042.model.ApiService
import java.io.File
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody


interface CheckingRepository {
    suspend fun startChecking(textCheck: String): Result<String, Throwable>
    suspend fun ocr(uri: Uri): Result<String, Throwable>
}

class CheckingRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : CheckingRepository {
    override suspend fun startChecking(textCheck: String): Result<String, Throwable> {
        return runSuspendCatching {
            apiService.startChecking(textCheck)
        }
    }

    override suspend fun ocr(uri: Uri): Result<String, Throwable> {
        return runSuspendCatching {
            withContext(Dispatchers.IO) {
                uri.path?.let { File(it) }?.let {
                    Log.d("xxx", "ocr: ${it.path}")
                    val requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), it)
                    val body = MultipartBody.Part.createFormData("image", it.name, requestFile)
                    apiService.ocr(body)
                } ?: ""
            }
        }
    }
}