package com.example.todoapp.utils

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.todoapp.databinding.EachTodoItemBinding

class ToDoAdapter(private val list: MutableList<ToDoData>) :
    RecyclerView.Adapter<ToDoAdapter.ToDoViewHolder>() {
        private var listener:ToDoAdapterClickInterface? = null
    fun setListener (listener:ToDoAdapterClickInterface) {
        this.listener = listener
    }
    inner class ToDoViewHolder(val binding: EachTodoItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ToDoViewHolder {
        val binding = EachTodoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ToDoViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ToDoViewHolder, position: Int) {
        with(holder) {
            with(list[position]) {
                binding.todoTask.text = this.task
                binding.deleteTask.setOnClickListener {
                    listener?.deleteTaskBtnClicked(this)
                }
                binding.editTask.setOnClickListener {
                    listener?.editTaskBtnClicked(this)
                }
            }
        }
    }
    interface ToDoAdapterClickInterface {
        fun deleteTaskBtnClicked (toDoData: ToDoData)
        fun editTaskBtnClicked (toDoData: ToDoData)
    }
}