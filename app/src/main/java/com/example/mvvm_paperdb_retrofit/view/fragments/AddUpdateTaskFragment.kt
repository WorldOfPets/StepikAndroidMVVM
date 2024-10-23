package com.example.mvvm_paperdb_retrofit.view.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.mvvm_paperdb_retrofit.databinding.FragmentAddTaskBinding
import com.example.mvvm_paperdb_retrofit.model.tasks.TaskModel
import com.example.mvvm_paperdb_retrofit.viewModel.TaskViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class AddUpdateTaskFragment : Fragment() {

    private lateinit var binding: FragmentAddTaskBinding
    private lateinit var taskViewModel: TaskViewModel
    private var cal = Calendar.getInstance()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddTaskBinding.inflate(layoutInflater, container, false)
        taskViewModel = ViewModelProvider(requireActivity())[TaskViewModel::class.java]


        val timeSetListener =
            TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)
                updateDateInView()
            }

        val dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                TimePickerDialog(requireContext(), timeSetListener,
                    cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
            }
        binding.btnSetDeadLine.setOnClickListener {
            DatePickerDialog(requireContext(), dateSetListener,
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        taskViewModel.currentTask.observe(viewLifecycleOwner){
            if (it != null){
                binding.taskNameET.setText(it.name)
                binding.dateTimeView.setText(it.timeDeadLine)
                binding.taskDescriptionET.setText(it.description)
                binding.saveTask.setOnClickListener {
                    taskViewModel.updateTask(TaskModel(
                        name = binding.taskNameET.text.toString(),
                        description = binding.taskDescriptionET.text.toString(),
                        timeDeadLine = binding.dateTimeView.text.toString()
                    ))
                    Navigation.findNavController(binding.root).navigateUp()
                }
            }else{
                binding.saveTask.setOnClickListener {
                    taskViewModel.addTask(TaskModel(
                        name = binding.taskNameET.text.toString(),
                        description = binding.taskDescriptionET.text.toString(),
                        timeDeadLine = binding.dateTimeView.text.toString()
                    ))
                    Navigation.findNavController(binding.root).navigateUp()
                }
            }
        }

        return binding.root
    }

    private fun updateDateInView() {
        val date = Date(cal.timeInMillis)
        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale("RU"))
        val formattedDate = formatter.format(date)
        binding.dateTimeView.text = formattedDate
    }

}