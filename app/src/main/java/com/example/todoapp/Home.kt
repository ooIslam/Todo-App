package com.example.todoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.todoapp.databinding.FragmentHomeBinding
import com.example.todoapp.utils.ToDoAdapter
import com.example.todoapp.utils.ToDoData
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Home : Fragment(), AddTodoPopupFragment.DialogNextBtnClickListener,
    ToDoAdapter.ToDoAdapterClickInterface {
    private lateinit var auth : FirebaseAuth
    private lateinit var navController: NavController
    private lateinit var databaseRef : DatabaseReference
    private lateinit var binding : FragmentHomeBinding
    private var popUp : AddTodoPopupFragment?= null
    private lateinit var adapter: ToDoAdapter
    private lateinit var mListe : MutableList<ToDoData>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init (view)
        getDataFromDatabase()
        registerEvents()
    }
    private fun init (view : View) {
        navController = Navigation.findNavController(view)
        auth = FirebaseAuth.getInstance()
        databaseRef = FirebaseDatabase.getInstance().reference.child(auth.currentUser?.uid.toString())

        binding.RecycleView.setHasFixedSize(true)
        binding.RecycleView.layoutManager = LinearLayoutManager(context)
        mListe = mutableListOf()
        adapter = ToDoAdapter(mListe)
        adapter.setListener(this)
        binding.RecycleView.adapter = adapter
    }
    private fun getDataFromDatabase() {
        databaseRef.addValueEventListener(object :ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mListe.clear()
                for (taskSnapshot in snapshot.children) {
                    val todoTask = taskSnapshot.key?.let {
                        ToDoData(it, taskSnapshot.value.toString())
                    }
                    if (todoTask != null) {
                        mListe.add(todoTask)
                    }
                }
                adapter.notifyDataSetChanged()

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , error.message ,Toast.LENGTH_SHORT).show()
            }

        })
    }
    
    override fun OnSaveTask (todo : String , todoEt : TextInputEditText) {
        databaseRef.push().setValue(todo).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context ,"Task saved successfully!!" ,Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context ,it.exception?.message ,Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popUp?.dismiss()
        }
    }

    override fun OnUpdateTask(toDoData: ToDoData, todoEt: TextInputEditText) {
        val taskRef = databaseRef.child(toDoData.taskId)
        taskRef.setValue(toDoData.task).addOnCompleteListener {
            if (it.isSuccessful) {
                Toast.makeText(context ,"task updated successfully !!" ,Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context ,it.exception?.message ,Toast.LENGTH_SHORT).show()
            }
            todoEt.text = null
            popUp?.dismiss()
        }
    }


    private fun registerEvents () {
        binding.AddBtn.setOnClickListener {
            if (popUp != null) {
                childFragmentManager.beginTransaction().remove(popUp!!).commit()
            }
            popUp = AddTodoPopupFragment()
            popUp!!.setListener(this)
            popUp!!.show(
                childFragmentManager,
                AddTodoPopupFragment.TAG
            )
        }
    }

    override fun deleteTaskBtnClicked(toDoData: ToDoData) {
        databaseRef.child(toDoData.taskId).removeValue().addOnCompleteListener {
            if (it.isSuccessful){
                Toast.makeText(context ,"Deleted successfully!!" ,Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context ,it.exception?.message ,Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun editTaskBtnClicked(toDoData: ToDoData) {
        if (popUp != null)
            childFragmentManager.beginTransaction().remove(popUp!!).commit()
        popUp = AddTodoPopupFragment.newInstance(toDoData.taskId, toDoData.task)
        popUp!!.setListener(this)
        popUp!!.show(childFragmentManager , AddTodoPopupFragment.TAG)
    }
}