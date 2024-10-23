package com.example.mvvm_paperdb_retrofit.model.tasks

import com.example.mvvm_paperdb_retrofit.model.MyCustomCallback
import com.example.mvvm_paperdb_retrofit.retrofit.RetrofitService
import com.example.mvvm_paperdb_retrofit.retrofit.TaskServerInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class TaskServerApi :TaskInterface {
    private val service = RetrofitService.retrofit.create(TaskServerInterface::class.java)
    override fun getTaskById(id: String, callback: MyCustomCallback<TaskModel>) {
        try {
            service.getTaskById(id).enqueue(object : Callback<TaskModel>{
                override fun onResponse(p0: Call<TaskModel>, p1: Response<TaskModel>) {
                    callback.onSuccess(p1.body() ?: TaskModel())
                }

                override fun onFailure(p0: Call<TaskModel>, p1: Throwable) {
                    callback.onFailure(p1.toString())
                }

            })
        }catch (ex:Exception){
            callback.onFailure(ex.toString())
        }
    }

    override fun getTasks(callback: MyCustomCallback<TaskModel>) {
        try {
            service.getTasks().enqueue(object : Callback<List<TaskModel>>{
                override fun onResponse(p0: Call<List<TaskModel>>, p1: Response<List<TaskModel>>) {
                    callback.onSuccess(p1.body() ?: arrayListOf())
                }

                override fun onFailure(p0: Call<List<TaskModel>>, p1: Throwable) {
                    callback.onFailure(p1.toString())
                }
            })
        }catch (ex:Exception){
            callback.onFailure(ex.toString())
        }
    }


    override fun addTask(task: TaskModel, callback: MyCustomCallback<TaskModel>) {
        try {
            val cal = Calendar.getInstance()
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("RU"))
            task.timeCreated = formatter.format(Date(cal.timeInMillis))
            task.isCompleted = false
            service.addTask(task).enqueue(object : Callback<TaskModel>{
                override fun onResponse(p0: Call<TaskModel>, p1: Response<TaskModel>) {
                    callback.onSuccess(p1.body() ?: TaskModel())
                }

                override fun onFailure(p0: Call<TaskModel>, p1: Throwable) {
                    callback.onFailure(p1.toString())
                }
            })
        }catch (ex:Exception){
            callback.onFailure(ex.toString())
        }
    }

    override fun updateTask(task: TaskModel, callback: MyCustomCallback<TaskModel>) {
        try {
            if (task.id != null){
                service.updateTask(task.id!!, task).enqueue(object : Callback<TaskModel>{
                    override fun onResponse(p0: Call<TaskModel>, p1: Response<TaskModel>) {
                        callback.onSuccess(p1.body() ?: TaskModel())
                    }

                    override fun onFailure(p0: Call<TaskModel>, p1: Throwable) {
                        callback.onFailure(p1.toString())
                    }
                })
            }else{
                callback.onFailure("TASK ID IS NULL OR EMPTY")
            }
        }catch (ex:Exception){
            callback.onFailure(ex.toString())
        }
    }

    override fun deleteTask(id: String, callback: MyCustomCallback<TaskModel>) {
        try {
            service.deleteTask(id).enqueue(object : Callback<TaskModel>{
                override fun onResponse(p0: Call<TaskModel>, p1: Response<TaskModel>) {
                    callback.onSuccess(p1.body() ?: TaskModel())
                }

                override fun onFailure(p0: Call<TaskModel>, p1: Throwable) {
                    callback.onFailure(p1.toString())
                }
            })
        }catch (ex:Exception){
            callback.onFailure(ex.toString())
        }
    }

    override fun completeTask(id: String, callback: MyCustomCallback<TaskModel>) {
        try {
            service.completeTask(id, true).enqueue(object : Callback<TaskModel>{
                override fun onResponse(p0: Call<TaskModel>, p1: Response<TaskModel>) {
                    callback.onSuccess(p1.body() ?: TaskModel())
                }

                override fun onFailure(p0: Call<TaskModel>, p1: Throwable) {
                    callback.onFailure(p1.toString())
                }
            })
        }catch (ex:Exception){
            callback.onFailure(ex.toString())
        }
    }

    override fun syncData(listTasks: List<TaskModel>, callback: MyCustomCallback<TaskModel>) {
        TODO("Not yet implemented")
    }

}