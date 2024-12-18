package com.example.mvvm_paperdb_retrofit.view.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mvvm_paperdb_retrofit.R
import com.example.mvvm_paperdb_retrofit.TaskApplication
import com.example.mvvm_paperdb_retrofit.databinding.FragmentTaskListBinding
import com.example.mvvm_paperdb_retrofit.view.adapters.TaskAdapter
import com.example.mvvm_paperdb_retrofit.viewModel.TaskViewModel


class TaskFragment : Fragment() {

    private lateinit var binding:FragmentTaskListBinding
    private lateinit var taskViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTaskListBinding.inflate(layoutInflater, container, false)
        taskViewModel = (requireActivity().application as TaskApplication).taskViewModel

        taskViewModel.tasks.observe(viewLifecycleOwner){ listTasks ->
            if (!listTasks.isNullOrEmpty()){
                with(binding.list) {
                    layoutManager = LinearLayoutManager(context)
                    adapter = TaskAdapter(listTasks.filter { it.isCompleted == !taskViewModel.showActive }, requireActivity())
                }
                binding.btnShowCompletedTasks.text = if (taskViewModel.showActive) "Show Completed Tasks" else "Show Active Tasks"
                binding.btnShowCompletedTasks.setOnClickListener {
                    taskViewModel.showActive = !taskViewModel.showActive
                }
            }
        }
        taskViewModel.currentTask.observe(viewLifecycleOwner){
            Log.e(TaskFragment::class.java.simpleName, it.toString())
        }
        taskViewModel.notifyMsg.observe(viewLifecycleOwner){
            Toast.makeText(requireContext(), it, Toast.LENGTH_LONG).show()
        }
        binding.addTask.setOnClickListener {
            taskViewModel.setCurrentTask(null)
            Navigation.findNavController(binding.root).navigate(R.id.action_taskFragment_to_addTaskFragment)
        }


        return binding.root
    }
}