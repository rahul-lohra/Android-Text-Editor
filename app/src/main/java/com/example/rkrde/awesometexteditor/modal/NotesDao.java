package com.example.rkrde.awesometexteditor.modal;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;

/**
 * Created by rkrde on 10-09-2017.
 */
@Dao
public interface NotesDao {

    @Query("select * from notes")
    List<Notes> getAllNotes();

    @Query("select * from notes where uId = :uId")
    Flowable<Notes> getNotesForUid(Long uId);

    @Insert
    void insertAll(ArrayList<Notes> list);
}

