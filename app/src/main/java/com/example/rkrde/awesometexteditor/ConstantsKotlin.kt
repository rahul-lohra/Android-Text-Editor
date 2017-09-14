package com.example.rkrde.awesometexteditor

import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by rkrde on 15-09-2017.
 */
class ConstantsKotlin{

    fun getCurTime(format:String="yyyy-MMM-dd HH:mm:ss"):String{
        val date = Date()
        val strDateFormat = format
        val dateFormat = SimpleDateFormat(strDateFormat)
        return dateFormat.format(date)
    }
}