package com.example.rkrde.awesometexteditor.modal;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

/**
 * Created by rkrde on 10-09-2017.
 */

@Database(entities = {Notes.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NotesDao notesDao();
}

