package com.example.rkrde.awesometexteditor.activity

import android.app.Activity
import android.content.ContentResolver
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.widget.AppCompatEditText
import android.support.v7.widget.AppCompatImageView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.webkit.MimeTypeMap
import android.widget.Toast
import com.example.rkrde.awesometexteditor.Constants
import com.example.rkrde.awesometexteditor.modal.FileModal
import com.example.rkrde.awesometexteditor.R
import com.example.rkrde.awesometexteditor.modal.Notes
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlinx.android.synthetic.main.activity_editor.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import org.jetbrains.anko.coroutines.experimental.bg
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription
import timber.log.Timber
import java.util.function.Consumer


class EditorActivity : BaseActivity() {

    private val SELECT_IMAGE = 2
    val TAG = "EditorActivity"
    val activity = this
    var uId: Long = -1
    val NO_UID: Long = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editor)
        handleIntent()
    }

    fun handleIntent() {
        val bundle = intent.extras
        if (bundle != null) {
            uId = bundle.getLong("uId", -1)

            if (uId != NO_UID) {
                showNoteFromDb()
            }
        }

    }

    fun showNoteFromDb() {

        val notes = appdatabase.notesDao().getNotesForUid(uId)

        val obsSingle = object : SingleObserver<List<Notes>> {
            override fun onSubscribe(d: Disposable) {
                Timber.d("onSubscribe")
            }

            override fun onSuccess(t: List<Notes>) {
                Timber.d("onSuccess")
                editorView.showNoteFromDb(t,contentResolver)
            }

            override fun onError(e: Throwable) {
                Timber.d("onError")
            }
        }

        notes.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(obsSingle)

    }


    fun saveData() {
        val linearLayout = editorView.ll
        val editorList = editorView.notesList

        var uniqueId = Constants.getCurrentTime()

        if (uId != NO_UID) {
            uniqueId = uId.toString()
            async(UI) {
                bg { appdatabase.notesDao().deleteNotes(uId) }
            }
        }
        val date = uniqueId
        val lastUpdated = Constants.getCurrentTime()
        var indexUri = 0 //editorView.uriList
        for (i in 0..linearLayout.childCount-1) {
            val v = linearLayout.getChildAt(i)

            editorList[i].noteId = uniqueId
            editorList[i].creationDate = date
            editorList[i].lastUpdated = lastUpdated
            if (v is AppCompatEditText) {
                editorList[i].text = v.text.toString()
            }else if(v is AppCompatImageView){
                /*
                * add file paths
                * */
                editorList[i].uri =  editorView.uriList[indexUri].toString()
                ++indexUri
            }
        }

        async(UI) {
            bg { appdatabase.notesDao().insertAll(editorList) }
        }


    }

    fun getImageFromCamera() {

    }

    fun convertUriToTempFile(uri: Uri, mimeType: String): FileModal {


        Log.d(TAG, "mimeType:" + mimeType)
        val tempFile = File(this.filesDir.absolutePath, "temp_image." + mimeType)
        //Copy Uri contents into temp File.
        try {
            tempFile.createNewFile()

            //save into private dir
            Constants.copyAndClose(this.contentResolver.openInputStream(uri), FileOutputStream(tempFile))
            Log.d(TAG, "temp file name:" + tempFile.name + "tempFile abs path:" + tempFile.absolutePath)
            Constants.saveImageFromDisk(this, tempFile, mimeType)
        } catch (e: IOException) {
            e.printStackTrace()
        }


        //Now fetch the new URI
        val tempUri = Uri.fromFile(tempFile)
        Log.d(TAG, "TempFIle:" + tempUri.path)

        return FileModal("", "", tempUri, mimeType)
    }

    fun uploadThisUri(uri: Uri, mimeType: String) {

    }

    fun sendImageToEditor(fileModal: FileModal) {
        editorView.addImage(fileModal)
    }

    fun isValidFile(uri: Uri, targetMimeType: Array<String>) {

        /*
        *
        * From Gallery
        * */
        var currentMimeType = ""
        if (uri.scheme == ContentResolver.SCHEME_CONTENT) {
            val mime = MimeTypeMap.getSingleton()
            val mimeType2 = mime.getExtensionFromMimeType(contentResolver.getType(uri))
            Log.d(TAG, " isValid File (1) uri:$uri,URI mimeType:$mimeType2")
            currentMimeType = mimeType2

            for (i in targetMimeType.indices) {
                if (currentMimeType.equals(targetMimeType[i], ignoreCase = true)) {
                    //save file in the disk
//                    val fileModal = convertUriToTempFile(uri, targetMimeType[i])
//                    val async = Async(uri,targetMimeType[i]).execute()
//                    sendImageToEditor(fileModal)
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    editorView.addBitmap(uri,bitmap)
                    break
                }

                if (i == targetMimeType.size - 1) {
                    Toast.makeText(this, "Invalid FIle Type", Toast.LENGTH_SHORT).show()
                    return
                }
            }


        } else {
            val extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(File(uri.path)).toString())
            currentMimeType = extension
            Log.d(TAG, " isValid File (2)  uri :$uri,FILE mimeType:$extension")

            for (i in targetMimeType.indices) {
                if (currentMimeType.equals(targetMimeType[i], ignoreCase = true)) {
                    //save Image into disk
                    val fileModal = convertUriToTempFile(uri, extension)
                    uploadThisUri(uri, targetMimeType[i])
                    break
                }

                if (i == targetMimeType.size - 1) {
                    Toast.makeText(this, "Invalid FIle Type", Toast.LENGTH_SHORT).show()
                    return
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            SELECT_IMAGE ->
                if (resultCode == Activity.RESULT_OK) {
                    val mUri = data?.data
                    val arr = arrayOf("jpg", "png", "jpeg", "webp")
                    if (mUri != null)
                        isValidFile(mUri, arr)
                }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.editor_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.menu_cam -> showDiaogToTakePic()
            R.id.menu_save -> saveData()
        }
        return true
    }


    fun showDiaogToTakePic() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick from below")
        val str = arrayOf("Gallery", "Camera")
        builder.setItems(str, { _, i ->
            when (i) {
                0 -> getImageFromGallery()
                1 -> getImageFromCamera()
            }
        })
        builder.show()
    }

    fun getImageFromGallery() {
        val intentImg = Intent(Intent.ACTION_GET_CONTENT)
        intentImg.putExtra(Intent.EXTRA_LOCAL_ONLY, true)
        intentImg.addCategory(Intent.CATEGORY_OPENABLE)
        intentImg.setType("image/*")
        try {
            startActivityForResult(Intent.createChooser(intentImg, "Select File to upload"), SELECT_IMAGE)
        } catch (ex: android.content.ActivityNotFoundException) {
            ex.printStackTrace()
        }
    }

    inner class Async(val uri: Uri, val mimeType: String) : AsyncTask<Unit, Unit, FileModal>() {
        val TAG = "Async"

        //todo iska returntype dekho

        override fun doInBackground(vararg p0: Unit?): FileModal {
            Log.d(TAG, "mimeType:" + mimeType)
            val tempFile = File(activity.filesDir.absolutePath, "temp_image." + mimeType)
            //Copy Uri contents into temp File.
            try {
                tempFile.createNewFile()

                //save into private dir
                Constants.copyAndClose(contentResolver.openInputStream(uri), FileOutputStream(tempFile))
                Log.d(TAG, "temp file name:" + tempFile.name + "tempFile abs path:" + tempFile.absolutePath)
                Constants.saveImageFromDisk(activity, tempFile, mimeType)
            } catch (e: IOException) {
                e.printStackTrace()
            }


            //Now fetch the new URI
            val tempUri = Uri.fromFile(tempFile)
            Log.d(TAG, "TempFIle:" + tempUri.path)

            return FileModal("", "", tempUri, mimeType)
        }

        override fun onPostExecute(result: FileModal) {
            super.onPostExecute(result)
            sendImageToEditor(result)
        }
    }
}
