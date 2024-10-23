package com.example.mvvm_paperdb_retrofit.model.tasks

import android.util.Log
import com.example.mvvm_paperdb_retrofit.model.MyCustomCallback
import io.paperdb.Paper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

class TaskLocalApi:TaskInterface {
    private val TASK_TABLE = "tasks"
    private val TASK_IS_NOT_DEFINE = "TASK IS NOT DEFINE"
    override fun getTaskById(id: String, callback: MyCustomCallback<TaskModel>) {
        try {
            val tasks = Paper.book().read<List<TaskModel>>(TASK_TABLE) ?: emptyList()
            val findTask = tasks.find { it.id == id } ?: TaskModel()
            callback.onSuccess(findTask)
        }catch (ex: Exception){
            callback.onFailure(ex.toString())
        }
    }

    override fun getTasks(callback: MyCustomCallback<TaskModel>) {
        try {
            callback.onSuccess(Paper.book().read<List<TaskModel>>(TASK_TABLE) ?: emptyList())
        }catch (ex : Exception){
            callback.onFailure(ex.toString())
        }
    }

    override fun addTask(task: TaskModel, callback: MyCustomCallback<TaskModel>) {
        try {
            val tasks = Paper.book().read<List<TaskModel>>(TASK_TABLE) ?: emptyList()
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("RU"))
            val cal = Calendar.getInstance()
            task.timeCreated = task.timeCreated ?: formatter.format(Date(cal.timeInMillis))
            task.isCompleted = task.isCompleted ?: false
            task.id ?: run {
                do {
                    task.id = UUID.randomUUID().toString()
                }while (tasks.any { it.id == task.id })
            }
            Paper.book().write(TASK_TABLE, tasks + task)
            callback.onSuccess(task)
        }catch (ex:Exception){
            Log.e(TaskLocalApi::class.java.simpleName, ex.toString())
            callback.onFailure(TASK_IS_NOT_DEFINE)
        }
    }

    override fun updateTask(task: TaskModel, callback: MyCustomCallback<TaskModel>) {
        try {
            val tasks = Paper.book().read<List<TaskModel>>(TASK_TABLE) ?: emptyList()
            if(tasks.isNotEmpty()) {
                tasks.find { it.id == task.id }?.apply {
                    name            = task.name             ?: name
                    description     = task.description      ?: description
                    timeDeadLine    = task.timeDeadLine     ?: timeDeadLine
                }
                Paper.book().write(TASK_TABLE, tasks)
                callback.onSuccess(task)
            }else{
                callback.onFailure(TASK_IS_NOT_DEFINE)
            }
        }catch (ex:Exception){
            Log.e(TaskLocalApi::class.java.simpleName, ex.toString())
            callback.onFailure(ex.toString())
        }
    }

    override fun deleteTask(id: String, callback: MyCustomCallback<TaskModel>) {
        try {
            val tasks = Paper.book().read<List<TaskModel>>(TASK_TABLE) ?: emptyList()
            if (tasks.isNotEmpty()){
                val deletedTask = tasks.find { it.id == id } ?: TaskModel()
                val newList = tasks.filter { it.id != id }

                Paper.book().write(TASK_TABLE, newList)
                callback.onSuccess(deletedTask)
            } else{
                callback.onFailure(TASK_IS_NOT_DEFINE)
            }
        }catch (ex:Exception){
            Log.e(TaskLocalApi::class.java.simpleName, ex.toString())
            callback.onFailure(ex.toString())
        }
    }

    override fun completeTask(id: String, callback: MyCustomCallback<TaskModel>) {
        try {
            val tasks = Paper.book().read<List<TaskModel>>(TASK_TABLE) ?: emptyList()
            if (tasks.isNotEmpty()){
                val completedTask = tasks.find { it.id == id }?.apply { isCompleted = true } ?: TaskModel()
                Paper.book().write(TASK_TABLE, tasks)
                callback.onSuccess(completedTask)
            } else{
                callback.onFailure(TASK_IS_NOT_DEFINE)
            }
        }catch (ex:Exception){
            Log.e(TaskLocalApi::class.java.simpleName, ex.toString())
            callback.onFailure(ex.toString())
        }
    }

    override fun syncData(listTasks: List<TaskModel>, callback: MyCustomCallback<TaskModel>) {
        try {
            val localList = Paper.book().read<List<TaskModel>>(TASK_TABLE) ?: emptyList()
            if(localList != listTasks){
                Paper.book().write(TASK_TABLE, listTasks)
                callback.notify("SYNC DATA WITH SERVER")
            }
        }catch (ex:Exception){
            callback.onFailure(ex.toString())
        }
    }


}