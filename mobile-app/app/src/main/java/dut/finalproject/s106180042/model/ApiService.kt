package dut.finalproject.s106180042.model

import okhttp3.MultipartBody
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiService {

    @GET("checking")
    suspend fun startChecking(
        @Query("text") text: String,
    ): String

    @Multipart
    @POST("ocr")
    suspend fun ocr(
        @Part image: MultipartBody.Part
    ): String
}
