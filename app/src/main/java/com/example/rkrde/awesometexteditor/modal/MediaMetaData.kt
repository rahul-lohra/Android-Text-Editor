package com.example.rkrde.awesometexteditor.modal

import android.net.Uri

/**
 * Created by rkrde on 15-09-2017.
 */

class MediaMetaData(uri: Uri,extension:String,path:String=""){
    val uri = uri
    val ext = extension
    val filePath = path
}