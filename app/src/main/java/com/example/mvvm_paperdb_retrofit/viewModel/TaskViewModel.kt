package com.example.mvvm_paperdb_retrofit.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mvvm_paperdb_retrofit.model.MyCustomCallback
import com.example.mvvm_paperdb_retrofit.model.tasks.TaskLocalApi
import com.example.mvvm_paperdb_retrofit.model.tasks.TaskModel
import com.example.mvvm_paperdb_retrofit.model.tasks.TaskServerApi
import com.example.mvvm_paperdb_retrofit.repository.TaskRepository
import com.example.mvvm_paperdb_retrofit.retrofit.RetrofitService

class TaskViewModel:ViewModel(),
    MyCustomCallback<TaskModel>{

    private var serverRepo = TaskRepository(TaskServerApi())
    private var localRepo = TaskRepository(TaskLocalApi())


    private var _tasks = MutableLiveData<List<TaskModel>>()
    val tasks:LiveData<List<TaskModel>> = _tasks

    private var _currentTask = MutableLiveData<TaskModel?>()
    val currentTask:LiveData<TaskModel?> = _currentTask

    private var _notifyMsg = MutableLiveData<String>()
    val notifyMsg:LiveData<String> = _notifyMsg


    private var isConnected:Boolean = false
        get(){
            return RetrofitService.checkInternetConnection()
        }
    var showActive:Boolean = true
        set(value){
            field = value
            _tasks.value = _tasks.value
        }
    init {
        if (isConnected){
            //localRepo.getTasks(this)
            serverRepo.getTasks(object : MyCustomCallback<TaskModel>{
                override fun onSuccess(listModel: List<TaskModel>) {
                    _tasks.value = listModel
                    localRepo.syncData(listModel, object : MyCustomCallback<TaskModel>{
                        override fun notify(msg: String) {
                            _notifyMsg.value = msg
                        }
                    })
                }
            })
        }else{
            localRepo.getTasks(this@TaskViewModel)
        }
        _currentTask.value = null
    }

    fun completeTask(id:String){
        if (isConnected){
            serverRepo.completeTask(id, object : MyCustomCallback<TaskModel>{
                override fun onSuccess(model: TaskModel) {
                    localRepo.completeTask(id, this@TaskViewModel)
                }
            })
        }else{
            _notifyMsg.value = "NO INTERNET CONNECTION"
        }

    }
    fun addTask(task: TaskModel) {
        if (isConnected){
            serverRepo.addTask(task, object : MyCustomCallback<TaskModel> {
                override fun onSuccess(model: TaskModel) {
                    localRepo.addTask(task, this@TaskViewModel)
                }
            })
        }else{
            localRepo.addTask(task, this@TaskViewModel)
        }

    }
    fun setCurrentTask(task: TaskModel?){
        _currentTask.value = task
    }

    fun updateTask(task: TaskModel) {
        task.id = _currentTask.value?.id.toString()
        task.timeCreated = _currentTask.value?.timeCreated.toString()
        _currentTask.value = null
        if (isConnected){
            serverRepo.updateTask(task, object : MyCustomCallback<TaskModel>{
                override fun onSuccess(model: TaskModel) {
                    localRepo.updateTask(task, this@TaskViewModel)
                }
            })
        }else{
            _notifyMsg.value = "NO INTERNET CONNECTION"
        }
    }

    fun deleteTask(id: String) {
        if (isConnected){
            serverRepo.deleteTask(id, object :MyCustomCallback<TaskModel>{
                override fun onSuccess(model: TaskModel) {
                    localRepo.deleteTask(id, this@TaskViewModel)
                }
            })
        }else{
            _notifyMsg.value = "NO INTERNET CONNECTION"
        }
    }

    override fun onSuccess(model: TaskModel) {
        if (isConnected){
            //serverRepo.getTasks(this)
            localRepo.getTasks(this)
        }else{
            localRepo.getTasks(this)
        }
    }

    override fun onSuccess(listModel: List<TaskModel>) {
        _tasks.value = listModel
    }

    override fun onFailure(exception: String) {
        Log.e(TaskViewModel::class.java.simpleName, exception)
    }

    override fun notify(msg: String) {
        _notifyMsg.value = msg
    }
}