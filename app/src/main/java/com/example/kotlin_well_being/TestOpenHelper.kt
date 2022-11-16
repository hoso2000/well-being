package com.example.kotlin_well_being

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class TestOpenHelper internal constructor(context: Context?) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {

        // テーブル作成
        // SQLiteファイルがなければSQLiteファイルが作成される
        db.execSQL(
            SQL_CREATE_ENTRIES
        )
        db.execSQL(
            SQL_CREATE_ENTRIES_2
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // アップデートの判別
        db.execSQL(
            SQL_DELETE_ENTRIES
        )
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }

    companion object {
        // データーベースのバージョン
        private const val DATABASE_VERSION = 1

        // データーベース名
        private const val DATABASE_NAME = "DB.db"
        private const val TABLE_NAME = "taskdb"
        private const val TABLE_NAME_2 = "rewarddb"
        private const val _ID = "_id"
        private const val _ID2 = "_id2"
        private const val COLUMN_NAME_DATE = "dateId"
        private const val COLUMN_NAME_GENRE = "genre"
        private const val COLUMN_NAME_TASK = "task"
        private const val COLUMN_NAME_REWARD = "reward"
        private const val COLUMN_NAME_TASKC = "taskChecker"
        private const val COLUMN_NAME_REWARDC = "rewardChecker"
        private const val SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DATE + " TEXT," +
                COLUMN_NAME_GENRE + " GENRE," +
                COLUMN_NAME_TASK + " TEXT," +
                COLUMN_NAME_TASKC + " INTEGER)"
        private const val SQL_CREATE_ENTRIES_2 = "CREATE TABLE " + TABLE_NAME_2 + " (" +
                _ID2 + " INTEGER PRIMARY KEY," +
                COLUMN_NAME_DATE + " TEXT," +
                COLUMN_NAME_REWARD + " TEXT," +
                COLUMN_NAME_REWARDC + " INTEGER)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME
    }
}