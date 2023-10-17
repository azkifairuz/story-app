package com.example.story_app.data

class ApiConfig {
    companion object{
        fun getApiService():ApiService {
            val myToken = BuildConfig.MY_TOKEN
            val loggingInterceptor = Interceptor {chain ->
                val req = chain.request()
                val requestHeaders = req.newBuilder()
                    .addHeader(
                        "Authorization" ,
                        "token $myToken"
                    )
                    .build()
                chain.proceed(requestHeaders)
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}