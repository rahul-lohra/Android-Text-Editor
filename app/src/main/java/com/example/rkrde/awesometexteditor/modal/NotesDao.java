package com.example.rkrde.awesometexteditor.modal;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;

/**
 * Created by rkrde on 10-09-2017.
 */
@Dao
public interface NotesDao {

    @Query("select * from notes")
    Single<List<Notes>> getAllNotes();

    @Query("select * from notes where uId = :uId")
    Single<List<Notes>> getNotesForUid(Long uId);

    @Insert
    void insertAll(ArrayList<Notes> list);

    @Query("delete from notes where uId=:uId")
    void deleteNotes(Long uId);

    @Update
    int  updateNote(ArrayList<Notes> list);

}

