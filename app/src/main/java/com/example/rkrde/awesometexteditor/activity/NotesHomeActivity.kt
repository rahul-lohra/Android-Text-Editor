package com.example.rkrde.awesometexteditor.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.rkrde.awesometexteditor.adapter.MyAdapter
import com.example.rkrde.awesometexteditor.MyApp
import com.example.rkrde.awesometexteditor.R
import com.example.rkrde.awesometexteditor.modal.AppDatabase
import com.example.rkrde.awesometexteditor.modal.Notes
import kotlinx.android.synthetic.main.activity_notes_home.*


class NotesHomeActivity : AppCompatActivity() {

    lateinit var myAdapter: MyAdapter
    lateinit var appDatabase:AppDatabase
    lateinit var notes :ArrayList<Notes>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_home)
        initVars()
        setAdapter()
        setClicks()
        fetchNotesAndDisplay()
    }

    fun setClicks(){
        fab.setOnClickListener {
            val intent = Intent(this,EditorActivity::class.java)
            startActivity(intent)
        }
    }

    fun initVars(){
        notes = arrayListOf()
        appDatabase = MyApp.getAppDatabase(this)
    }

    fun setAdapter(){

        rv.layoutManager = LinearLayoutManager(this)
        myAdapter = MyAdapter(this, notes)
        rv.adapter = myAdapter
    }

    fun fetchNotesAndDisplay(){
        notes.addAll(appDatabase.notesDao().getAllNotes())

        if(notes.size>0){
            Log.e("++++","++++")
            myAdapter.notifyDataSetChanged()
        }else
            Toast.makeText(this,"no notes",Toast.LENGTH_SHORT).show()

    }
}
