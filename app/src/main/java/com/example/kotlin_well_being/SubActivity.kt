package com.example.kotlin_well_being

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class SubActivity : AppCompatActivity() {

    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    private val spinnerItems = arrayOf("授業", "課題", "勉強", "研究・ゼミ", "バイト", "部活・サークル", "プロジェクト活動", "就活", "家事", "その他")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val textDate :TextView = findViewById(R.id.textView0)
        val btnBack :Button = findViewById(R.id.btnBack)
        val btnAdd :Button = findViewById(R.id.btnAdd)
        val btnAdd2 :Button = findViewById(R.id.btnAdd2)
        val et1 :EditText = findViewById(R.id.editText1)
        val et2 :EditText = findViewById(R.id.editText2)
        val isChecked = 0
        val isChecked2 = 0
        var spinnerText = ""

        // 日付を取得
        val getDate = intent.getStringExtra("DATE_KEY")
        textDate.text = getDate

        val spinner = findViewById<Spinner>(R.id.spinner)

        val adapter = ArrayAdapter(
            this,
            R.layout.custom_spinner,
            resources.getStringArray(R.array.list)
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spinner.adapter = adapter
//        val adapter = ArrayAdapter(this, android.R.layout., spinnerItems)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            // 項目が選択された時に呼ばれる
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerText = parent?.selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        btnAdd.setOnClickListener{
            helper = TestOpenHelper(applicationContext)
            db = helper.writableDatabase
            var task = et1.text.toString()
            insertData(db,getDate,spinnerText,task,isChecked)
            val toast = Toast.makeText(this, "タスクを追加しました。", Toast.LENGTH_SHORT)
            toast.show()
            et1.setText("")
        }

        btnAdd2.setOnClickListener{
            helper = TestOpenHelper(applicationContext)
            db = helper.writableDatabase
            val reward = et2.text.toString()
            insertData2(db,getDate,reward,isChecked2)
            val toast = Toast.makeText(this, "ご褒美を追加しました。", Toast.LENGTH_SHORT)
            toast.show()
            et2.setText("")
        }

        //登録ボタン（アクティビティの終了）
        btnBack.setOnClickListener {
//            if(reward == "") {
//                val toast = Toast.makeText(this, "ご褒美を入力してください", Toast.LENGTH_SHORT)
//                toast.show()
//            }else{
            // 入力したテキストをSQLiteに登録
            val intentBack = Intent(application, MainActivity::class.java)
            startActivity(intentBack)
        }
    }



    // SQLiteに登録
    private fun insertData(db: SQLiteDatabase, dateData: String?, genreData:String, taskData: String, taskChecker: Int) {
        val values = ContentValues()
        values.put("dateId", dateData)
        values.put("genre", genreData)
        values.put("task", taskData)
        values.put("taskChecker", taskChecker)
        db.insert("taskdb", "null", values)
        //db.update("taskdb", values, "date = ?", arrayOf(dateData))
        //db.update("rewarddb", values2, "date = ?", arrayOf(dateData))
    }

    private fun insertData2(db: SQLiteDatabase, dateData: String?, rewardData: String, rewardChecker: Int) {
        val values2 = ContentValues()
        values2.put("dateId", dateData)
        values2.put("reward", rewardData)
        values2.put("rewardChecker", rewardChecker)
        db.insert("rewarddb", "null", values2)
    }
}