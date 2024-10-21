package com.example.mvvm_paperdb_retrofit.model.tasks

import kotlinx.serialization.Serializable

@Serializable
data class TaskModel(
    var id:String? = null,
    var name:String? = null,
    var description:String? = null,
    var isCompleted:Boolean? = null,
    var timeCreated:String? = null,
    var timeDeadLine:String? = null
    )
