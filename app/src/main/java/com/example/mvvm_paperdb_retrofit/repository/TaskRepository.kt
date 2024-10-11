package com.example.mvvm_paperdb_retrofit.repository

import com.example.mvvm_paperdb_retrofit.model.MyCustomCallback
import com.example.mvvm_paperdb_retrofit.model.tasks.TaskInterface
import com.example.mvvm_paperdb_retrofit.model.tasks.TaskModel
import com.example.mvvm_paperdb_retrofit.retrofit.RetrofitService

class TaskRepository(private val taskInterface: TaskInterface) {
    fun getTaskById(id: String, callback: MyCustomCallback<TaskModel>) {
        taskInterface.getTaskById(id, callback)
    }

    fun getTasks(callback: MyCustomCallback<TaskModel>){
        taskInterface.getTasks(callback)
    }

    fun addTask(task: TaskModel, callback: MyCustomCallback<TaskModel>) {
        taskInterface.addTask(task, callback)
    }

    fun updateTask(task: TaskModel, callback: MyCustomCallback<TaskModel>) {
        taskInterface.updateTask(task, callback)
    }

    fun deleteTask(id: String, callback: MyCustomCallback<TaskModel>) {
        taskInterface.deleteTask(id, callback)
    }

    fun completeTask(id:String, callback: MyCustomCallback<TaskModel>){
        return taskInterface.completeTask(id, callback)
    }
    fun syncData(callback: MyCustomCallback<TaskModel>){
        RetrofitService.syncData(callback)
    }
}