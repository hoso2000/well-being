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
        val isChecked = 0
        val isChecked2 = 0

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
            insertData(db,getDate,task,reward,isChecked,isChecked2)
            val intentBack = Intent(application, MainActivity::class.java)
            startActivity(intentBack)
        }
    }

    // SQLiteに登録
    private fun insertData(db: SQLiteDatabase, dateData: String?, taskData: String, rewardData: String, taskChecker: Int, rewardChecker: Int) {
        val values = ContentValues()
        values.put("date", dateData)
        values.put("task", taskData)
        values.put("reward", rewardData)
        values.put("taskChecker", taskChecker)
        values.put("rewardChecker", rewardChecker)
        db.insert("testdb", null, values)
        db.update("testdb", values, "date = ?", arrayOf(dateData))
    }
}