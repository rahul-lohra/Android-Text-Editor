package com.example.rkrde.awesometexteditor.modal;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by rkrde on 10-09-2017.
 */
@Entity(tableName = "notes")
public class Notes {


    public final static int TYPE_TEXT = 1;
    public final static int TYPE_IMAGE = 2;

    @PrimaryKey(autoGenerate = true)
    long uId;

    @ColumnInfo(name = "text")
    private String text;

    @ColumnInfo(name = "fileName")
    private String fileName;

    @ColumnInfo(name = "uri")
    private String uri;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "noteId")
    private String noteId;

    @ColumnInfo(name = "creationDate")
    private String creationDate;

    @ColumnInfo(name = "lastUpdated")
    private String lastUpdated;

    @ColumnInfo(name = "index")
    private int index;

    public Notes(int type, int index) {
        this.type = type;
        this.index = index;
    }

    public void setuId(long uId) {
        this.uId = uId;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(String lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    public long getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getNoteId() {
        return noteId;
    }

    public void setNoteId(String noteId) {
        this.noteId = noteId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
