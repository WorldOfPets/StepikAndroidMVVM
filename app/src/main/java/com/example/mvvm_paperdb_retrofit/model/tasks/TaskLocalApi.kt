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
            val tasks = Paper.book().read<List<TaskModel>>(TASK_TABLE)
            var findTask = TaskModel()
            if(!tasks.isNullOrEmpty()){
                tasks.forEach {
                    if(it.id == id){
                        findTask = it
                    }
                }
            }
            callback.onSuccess(findTask)
        }catch (ex: Exception){
            callback.onFailure(ex.toString())
        }

    }

    override fun getTasks(callback: MyCustomCallback<TaskModel>) {
        try {
            callback.onSuccess(Paper.book().read<List<TaskModel>>(TASK_TABLE) ?: arrayListOf())
        }catch (ex : Exception){
            callback.onFailure(ex.toString())
        }
    }

    override fun addTask(task: TaskModel, callback: MyCustomCallback<TaskModel>) {
        try {
            val tasks = Paper.book().read<List<TaskModel>>(TASK_TABLE) ?: arrayListOf()
            var checkID = true
            val cal = Calendar.getInstance()
            val date = Date(cal.timeInMillis) // 2077-02-22T07:46:53.082Z
            val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("RU"))
            val formattedDate = formatter.format(date)
            task.timeCreated = formattedDate
            task.isCompleted = false
            if (task.id.isNullOrEmpty()){
                while (checkID){
                    task.id = UUID.randomUUID().toString()
                    checkID = tasks.filter {
                        it.id == task.id
                    }.isNotEmpty()
                }
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
            val tasks = Paper.book().read<List<TaskModel>>(TASK_TABLE)
            if(!tasks.isNullOrEmpty()){
                tasks.forEach {
                    if(it.id == task.id){
                        it.name = task.name ?: it.name
                        it.description = task.description ?: it.description
                        it.isCompleted = task.isCompleted ?: it.isCompleted
                        it.timeCreated = task.timeCreated ?: it.timeCreated
                        it.timeDeadLine = task.timeDeadLine ?: it.timeDeadLine
                    }
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
            val newTasks = arrayListOf<TaskModel>()
            val tasks = Paper.book().read<List<TaskModel>>(TASK_TABLE)
            var deletedTask = TaskModel()
            if (!tasks.isNullOrEmpty()){
                tasks.forEach {
                    if (it.id != id){
                        newTasks.add(it)
                    }else{
                        deletedTask = it
                    }
                }
                Paper.book().write(TASK_TABLE, newTasks)
                callback.onSuccess(deletedTask)
            }else{
                callback.onFailure(TASK_IS_NOT_DEFINE)
            }
        }catch (ex:Exception){
            Log.e(TaskLocalApi::class.java.simpleName, ex.toString())
            callback.onFailure(ex.toString())
        }
    }

    override fun completeTask(id: String, callback: MyCustomCallback<TaskModel>) {
        try {
            val tasks = Paper.book().read<List<TaskModel>>(TASK_TABLE)
            var completeTask = TaskModel()
            if(!tasks.isNullOrEmpty()){
                tasks.forEach {
                    if(it.id == id){
                        it.isCompleted = true
                        completeTask = it
                    }
                }
                Paper.book().write(TASK_TABLE, tasks)
                callback.onSuccess(completeTask)
            }else{
                callback.onFailure(TASK_IS_NOT_DEFINE)
            }

        }catch (ex:Exception){
            Log.e(TaskLocalApi::class.java.simpleName, ex.toString())
            callback.onFailure(ex.toString())
        }
    }

    override fun syncData(listTasks: List<TaskModel>, callback: MyCustomCallback<TaskModel>) {
        try {
            Paper.book().write(TASK_TABLE, listTasks)
            callback.notify("SYNC DATA WITH SERVER")
        }catch (ex:Exception){
            callback.onFailure(ex.toString())
        }
    }


}