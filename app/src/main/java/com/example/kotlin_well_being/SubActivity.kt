package com.example.kotlin_well_being

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import org.w3c.dom.Text

class SubActivity : AppCompatActivity() {

    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    private val spinnerItems = arrayOf("授業", "課題", "勉強", "研究・ゼミ", "バイト", "部活・サークル", "プロジェクト活動", "就活", "家事", "その他")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val textDate :TextView = findViewById(R.id.textView0)
        val btnBack :Button = findViewById(R.id.btnBack)
        val et1 :EditText = findViewById(R.id.editText1)
        val et2 :EditText = findViewById(R.id.editText2)
        val isChecked = 0
        val isChecked2 = 0
        var spinnerText = ""

        // 日付を取得
        val getDate = intent.getStringExtra("DATE_KEY")
        textDate.text = getDate

        val spinner = findViewById<Spinner>(R.id.spinner)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerItems)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            // 項目が選択された時に呼ばれる
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerText = parent?.selectedItem as String
                Toast.makeText(this@SubActivity, spinnerText, Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //登録ボタン（アクティビティの終了）
        btnBack.setOnClickListener {
            helper = TestOpenHelper(applicationContext)
            db = helper.writableDatabase
            val task = et1.text.toString()
            val reward = et2.text.toString()
            // 入力したテキストをSQLiteに登録
            insertData(db,getDate,spinnerText,task,reward,isChecked,isChecked2)
            //insertData(db,getDate,spinnerText,reward,isChecked,isChecked2)
            val intentBack = Intent(application, MainActivity::class.java)
            startActivity(intentBack)
        }
    }



    // SQLiteに登録
    private fun insertData(db: SQLiteDatabase, dateData: String?, genreData:String, taskData: String, rewardData: String, taskChecker: Int, rewardChecker: Int) {
        val values = ContentValues()
        values.put("date", dateData)
        values.put("genre", genreData)
        values.put("task", taskData)
        values.put("reward", rewardData)
        values.put("taskChecker", taskChecker)
        values.put("rewardChecker", rewardChecker)
        db.insert("testdb", null, values)
        db.update("testdb", values, "date = ?", arrayOf(dateData))
    }
}