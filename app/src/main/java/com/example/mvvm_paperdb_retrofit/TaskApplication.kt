package com.example.mvvm_paperdb_retrofit

import android.app.Application
import com.example.mvvm_paperdb_retrofit.viewModel.TaskViewModel

class TaskApplication : Application() {
    lateinit var taskViewModel: TaskViewModel

    override fun onCreate() {
        super.onCreate()
        taskViewModel = TaskViewModel()
    }
}