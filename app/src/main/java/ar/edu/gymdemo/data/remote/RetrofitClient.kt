package ar.edu.gymdemo.data.remote

import ar.edu.gymdemo.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val logging = HttpLoggingInterceptor().apply {
        // Solo loguea cuerpos en debug; en release no se filtran datos.
        level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE
    }

    private val http = OkHttpClient.Builder()
        .connectTimeout(20, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(20, TimeUnit.SECONDS)
        .addInterceptor(logging)
        .build()

    val api: GymApi by lazy {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL) // configurable por buildType, no hardcodeada
            .client(http)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GymApi::class.java)
    }
}
