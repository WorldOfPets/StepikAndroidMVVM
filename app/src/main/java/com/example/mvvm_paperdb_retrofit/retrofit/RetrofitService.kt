package com.example.mvvm_paperdb_retrofit.retrofit

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection

object RetrofitService {
    //константа для хранения адреса сервера
    private const val URL_MOCKAPI:String = "https://6704d32eab8a8f892734f73d.mockapi.io/"
    //объект retrofit отвечающий за выполнение сетевых запросов и сериализацию данных
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(URL_MOCKAPI)
        //сериализация происходит за счет установки фабрики (в данном случае за сериализацию отвечате Gson)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun checkInternetConnection():Boolean {
        try{

            return runBlocking {
                CoroutineScope(Dispatchers.IO).async {
//                    val url = URL(URL_MOCKAPI)
//                    val connection = url.openConnection() as HttpURLConnection
//                    connection.requestMethod = "HEAD"
//                    connection.connectTimeout = 1000
//                    connection.connect()
//                    val responseCode = connection.responseCode
                    val responseCode = retrofit.create(TaskServerInterface::class.java).checkInternetConnection().execute().code()
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        Log.e("InternetConnection", "Internet connection is available")
                        true
                    } else {
                        Log.e("InternetConnection", "Internet connection is not available")
                        false
                    }
                }.await()

            }
        } catch (ex : Exception) {
            Log.e("InternetConnection", ex.toString())
            Log.d("InternetConnection", "No internet connection")
            return false
        }
    }
}