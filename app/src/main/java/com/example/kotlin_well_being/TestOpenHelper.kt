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
        private const val TABLE_NAME = "testdb"
        private const val COLUMN_NAME_DATE = "date"
        private const val COLUMN_NAME_GENRE = "genre"
        private const val COLUMN_NAME_TASK = "task"
        private const val COLUMN_NAME_GENRE2 = "genre2"
        private const val COLUMN_NAME_TASK2 = "task2"
        private const val COLUMN_NAME_GENRE3 = "genre3"
        private const val COLUMN_NAME_TASK3 = "task3"
        private const val COLUMN_NAME_REWARD = "reward"
        private const val COLUMN_NAME_TASKC = "taskChecker"
        private const val COLUMN_NAME_REWARDC = "rewardChecker"
        private const val SQL_CREATE_ENTRIES = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_NAME_DATE + " TEXT PRIMARY KEY," +
                COLUMN_NAME_GENRE + " GENRE," +
                COLUMN_NAME_TASK + " TEXT," +
                COLUMN_NAME_GENRE2 + " GENRE," +
                COLUMN_NAME_TASK2 + " TEXT," +
                COLUMN_NAME_GENRE3 + " GENRE," +
                COLUMN_NAME_TASK3 + " TEXT," +
                COLUMN_NAME_REWARD + " TEXT," +
                COLUMN_NAME_TASKC + " INTEGER," +
                COLUMN_NAME_REWARDC + " INTEGER)"
        private const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + TABLE_NAME
    }
}