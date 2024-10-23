package com.example.mvvm_paperdb_retrofit.view.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.mvvm_paperdb_retrofit.R
import io.paperdb.Paper


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Paper.init(this@MainActivity)
/*        val policy = ThreadPolicy.Builder().permitAll().build()
       StrictMode.setThreadPolicy(policy)*/
    }
}