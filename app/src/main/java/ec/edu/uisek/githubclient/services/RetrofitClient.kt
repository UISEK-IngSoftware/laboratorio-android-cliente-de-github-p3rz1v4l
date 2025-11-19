package ec.edu.uisek.githubclient.services

import android.util.Log
import ec.edu.uisek.githubclient.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Objeto singleton que configura y proporciona una instancia de Retrofit para conectarse a la API de GitHub.
 * Al ser un singleton, se asegura de que solo exista una instancia de Retrofit en toda la app.
 */
object RetrofitClient {

    private const val TAG = "RetrofitClient"
    private const val BASE_URL = "https://api.github.com/" // URL base para todas las llamadas a la API.

    /**
     * Interceptor que añade automáticamente el token de autenticación a todas las peticiones.
     * Esto centraliza la lógica de autenticación y evita repetirla en cada llamada.
     */
    private val authInterceptor = Interceptor { chain ->
        val originalRequest = chain.request()
        val token = BuildConfig.GITHUB_API_TOKEN // Token de acceso personal de GitHub.

        // Construye una nueva petición añadiendo los encabezados necesarios.
        val newRequest = if (token.isNotEmpty()) {
            originalRequest.newBuilder()
                .addHeader("Authorization", "Bearer $token") // Añade el token para autorización.
                .addHeader("Accept", "application/vnd.github.v3+json") // Especifica la versión de la API.
                .build()
        } else {
            Log.w(TAG, "⚠️ Token de GitHub NO configurado. Las peticiones pueden fallar.")
            originalRequest.newBuilder()
                .addHeader("Accept", "application/vnd.github.v3+json")
                .build()
        }

        // Procede con la petición modificada.
        chain.proceed(newRequest)
    }

    /**
     * Interceptor para registrar en Logcat los detalles de las peticiones y respuestas de red.
     * Es muy útil para depurar problemas de API. Solo se activa en builds de depuración.
     */
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY // En DEBUG, muestra todos los detalles (headers, body).
        } else {
            HttpLoggingInterceptor.Level.NONE // En RELEASE, no muestra logs.
        }
    }

    /**
     * Cliente HTTP (OkHttpClient) configurado con nuestros interceptores.
     * Este cliente será usado por Retrofit para realizar todas las peticiones de red.
     */
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)      // Añade el interceptor de autenticación.
        .addInterceptor(loggingInterceptor) // Añade el interceptor de logging.
        .build()

    /**
     * Instancia de Retrofit configurada de forma "lazy" (diferida).
     * Se crea solo la primera vez que se necesita.
     */
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient) // Usa nuestro cliente HTTP personalizado.
            .addConverterFactory(GsonConverterFactory.create()) // Convierte JSON a objetos Kotlin.
            .build()
    }

    /**
     * Implementación concreta de nuestra interfaz [GithubApiService].
     * Retrofit crea automáticamente el código necesario para hacer las llamadas a la API.
     */
    val gitHubApiService: GithubApiService by lazy {
        retrofit.create(GithubApiService::class.java)
    }
}
