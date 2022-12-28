package dut.finalproject.s106180042.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dut.finalproject.s106180042.BuildConfig
import dut.finalproject.s106180042.model.ApiService
import dut.finalproject.s106180042.repository.CheckingRepository
import dut.finalproject.s106180042.repository.CheckingRepositoryImpl
import java.util.Collections
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

@Module
@InstallIn(SingletonComponent::class)
interface NetworkModule {

    @Binds
    fun checkRepository(impl: CheckingRepositoryImpl): CheckingRepository

    companion object {
        private const val API_TIMEOUT = 10L
        private const val BASE_URL = "http://192.168.8.172:5555/"

        @Singleton
        @Provides
        fun remoteApi(): ApiService {
            val logging = HttpLoggingInterceptor()
            logging.level =
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

            val httpClientBuilder = OkHttpClient.Builder()
            httpClientBuilder.addInterceptor(logging)
            val client = httpClientBuilder
                .connectTimeout(API_TIMEOUT, TimeUnit.MINUTES)
                .writeTimeout(API_TIMEOUT, TimeUnit.MINUTES)
                .readTimeout(API_TIMEOUT, TimeUnit.MINUTES)
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build()


            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}