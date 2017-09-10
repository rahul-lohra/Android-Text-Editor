package com.example.rkrde.awesometexteditor.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.rkrde.awesometexteditor.MyApp
import com.example.rkrde.awesometexteditor.R
import com.example.rkrde.awesometexteditor.modal.AppDatabase
import kotlinx.android.synthetic.main.activity_main.*
open class BaseActivity : AppCompatActivity() {

    lateinit var appdatabase:AppDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    fun init(){
       appdatabase = MyApp.getAppDatabase(this)
    }


}
