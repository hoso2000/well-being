package com.example.kotlin_well_being

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import org.w3c.dom.Text

class SubActivity : AppCompatActivity() {

    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val textDate :TextView = findViewById(R.id.textView0)
        val btnBack :Button = findViewById(R.id.btnBack)
        val et1 :EditText = findViewById(R.id.editText1)
        val et2 :EditText = findViewById(R.id.editText2)

        // 日付を取得
        val getDate = intent.getStringExtra("DATE_KEY")
        textDate.text = getDate

        //登録ボタン（アクティビティの終了）
        btnBack.setOnClickListener {
            helper = TestOpenHelper(applicationContext)
            db = helper.writableDatabase
            val task = et1.text.toString()
            val reward = et2.text.toString()
            // 入力したテキストをSQLiteに登録
            insertData(db,task,reward)
            val intentBack = Intent(application, MainActivity::class.java)
            //intentBack.putExtra("TASK_KEY",task)
            startActivity(intentBack)
            //intent.putExtra("REWARD_TEXT",reward)
            //finish()
        }
    }

    // SQLiteに登録
    private fun insertData(db: SQLiteDatabase, taskData: String, rewardData: String) {
        val values = ContentValues()
        values.put("task", taskData)
        values.put("reward", rewardData)
        db.insert("testdb", null, values)
    }
}