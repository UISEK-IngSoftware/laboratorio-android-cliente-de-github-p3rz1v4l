package ec.edu.uisek.githubclient.interceptors

import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response


class BasicAuthInterceptor(private val username: String, private val password: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val credential = Credentials.basic(username,password)
        request = request.newBuilder()
            .header("Authorization", credential)
            .build()
        return chain.proceed(request)
    }

}



