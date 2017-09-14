package com.example.rkrde.awesometexteditor.activity

import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.rkrde.awesometexteditor.adapter.MyAdapter
import com.example.rkrde.awesometexteditor.MyApp
import com.example.rkrde.awesometexteditor.R
import com.example.rkrde.awesometexteditor.modal.AppDatabase
import com.example.rkrde.awesometexteditor.modal.Notes
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.android.synthetic.main.activity_notes_home.*
import timber.log.Timber


class HomeActivity : AppCompatActivity() {

    lateinit var myAdapter: MyAdapter
    lateinit var appDatabase:AppDatabase
    lateinit var notes :ArrayList<Notes>
    lateinit var context: Context
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_home)
        initVars()
        setAdapter()
        setClicks()

    }

    override fun onStart() {
        super.onStart()
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
        context = this
        appDatabase = MyApp.getAppDatabase(this)
    }

    fun setAdapter(){

        rv.layoutManager = LinearLayoutManager(this)
        myAdapter = MyAdapter(this, notes)
        rv.adapter = myAdapter
    }

    fun fetchNotesAndDisplay(){
        val allNotes = appDatabase.notesDao().getAllNotes()
        tvAddNote.visibility = View.GONE

        val obsSingle = object : SingleObserver<List<Notes>> {
            override fun onSubscribe(d: Disposable) {
                Timber.d("onSubscribe")
            }

            override fun onSuccess(t: List<Notes>) {
                Timber.d("onSuccess")
                if(t.size>0){
                    Timber.d("++++")
                    notes.clear()
                    notes.addAll(t)
                    myAdapter.notifyDataSetChanged()
                }else
                {
//                    Toast.makeText(context,"no notes",Toast.LENGTH_SHORT).show()
                    tvAddNote.visibility = View.VISIBLE
                }
            }

            override fun onError(e: Throwable) {
                Timber.d("onError: ${e.message}")
            }
        }


        allNotes.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(obsSingle)


    }
}
