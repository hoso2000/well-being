package com.example.kotlin_well_being

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class SubActivity : AppCompatActivity() {

    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val btnBack :Button = findViewById(R.id.btnBack)
        val et1 :EditText = findViewById(R.id.editText1)
        val et2 :EditText = findViewById(R.id.editText2)

        //テスト
        val btnTest :Button = findViewById(R.id.btnTest)
        btnTest.setOnClickListener { readData() }

        //３）戻るボタン（アクティビティの終了）
        btnBack.setOnClickListener {
            helper = TestOpenHelper(applicationContext)
            db = helper.writableDatabase
            val task = et1.text.toString()
            val reward = et2.text.toString()
            insertData(db,task,reward)
            finish()
        }
    }

    private fun readData() {
        helper = TestOpenHelper(applicationContext)
        db = helper.readableDatabase

        val cursor = db.query(
            "testdb", arrayOf("task", "reward"),
            null,
            null,
            null,
            null,
            null
        )
        cursor.moveToFirst()
        val sbuilder = StringBuilder()
        for (i in 0 until cursor.count) {
            sbuilder.append(cursor.getString(0))
            sbuilder.append(": ")
            sbuilder.append(cursor.getString(1))
            sbuilder.append("\n")
            cursor.moveToNext()
        }

        // 忘れずに！
        cursor.close()
        val textView = findViewById<TextView>(R.id.textView3)
        textView.text = sbuilder.toString()
    }

    private fun insertData(db: SQLiteDatabase, taskData: String, rewardData: String) {
        val values = ContentValues()
        values.put("task", taskData)
        values.put("reward", rewardData)
        db.insert("testdb", null, values)
    }
}