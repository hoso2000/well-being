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

//    val spinnerItems = arrayOf("選択してください","授業", "課題", "勉強", "研究・ゼミ", "バイト", "部活・サークル", "プロジェクト活動", "就活", "家事", "その他")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub)

        val textDate :TextView = findViewById(R.id.textView0)
        val btnBack :Button = findViewById(R.id.btnBack)
        val et1 :EditText = findViewById(R.id.editText1)
        val et12 :EditText = findViewById(R.id.editText1_2)
        val et13 :EditText = findViewById(R.id.editText1_3)
        val et2 :EditText = findViewById(R.id.editText2)
        val btnDelete2 :ImageButton = findViewById(R.id.btnDelete2)
        val btnDelete3 :ImageButton = findViewById(R.id.btnDelete3)
        val isChecked = 0
        val isChecked12 = 0
        val isChecked13 = 0
        val isChecked2 = 0
        var spinnerText = ""
        var spinnerText2 = ""
        var spinnerText3 = ""
        val spinnerItems = mutableListOf<String>("選択してください","授業", "課題", "勉強", "研究・ゼミ", "バイト", "部活・サークル", "プロジェクト活動", "就活", "家事", "その他")
        val spinnerItems2 = mutableListOf<String>("選択してください","授業", "課題", "勉強", "研究・ゼミ", "バイト", "部活・サークル", "プロジェクト活動", "就活", "家事", "その他")
        val spinnerItems3 = mutableListOf<String>("選択してください","授業", "課題", "勉強", "研究・ゼミ", "バイト", "部活・サークル", "プロジェクト活動", "就活", "家事", "その他")
        // 日付を取得
        val getDate = intent.getStringExtra("DATE_KEY")
        textDate.text = getDate

        // データを取得
        helper = TestOpenHelper(applicationContext)
        db = helper.readableDatabase

        val cursor = db.query(
            "testdb", arrayOf("date", "genre", "task","genre2", "task2","genre3", "task3", "reward", "taskChecker","taskChecker2","taskChecker3", "rewardChecker"),
            "date == ?",
            arrayOf(getDate),
            null,
            null,
            null
        )
        if (cursor.count != 0) {
            cursor.moveToFirst()
            spinnerItems.removeAt(0)
            spinnerItems2.removeAt(0)
            spinnerItems3.removeAt(0)
            spinnerItems.add(0,cursor.getString(1))
            et1.setText(cursor.getString(2))
            spinnerItems2.add(0,cursor.getString(3))
            et12.setText(cursor.getString(4))
            spinnerItems3.add(0,cursor.getString(5))
            et13.setText(cursor.getString(6))
            et2.setText(cursor.getString(7))
            cursor.close()
        }

        val spinner = findViewById<Spinner>(R.id.spinner)
        val spinner2 = findViewById<Spinner>(R.id.spinner2)
        val spinner3 = findViewById<Spinner>(R.id.spinner3)

        val adapter = ArrayAdapter(
            this,
            R.layout.custom_spinner,
            spinnerItems
        )
        val adapter2 = ArrayAdapter(
            this,
            R.layout.custom_spinner,
            spinnerItems2
        )
        val adapter3 = ArrayAdapter(
            this,
            R.layout.custom_spinner,
            spinnerItems3
        )
        adapter.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        adapter2.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        adapter3.setDropDownViewResource(R.layout.custom_spinner_dropdown)
        spinner.adapter = adapter
        spinner2.adapter = adapter2
        spinner3.adapter = adapter3

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
        spinner2.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            // 項目が選択された時に呼ばれる
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerText2 = parent?.selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
        spinner3.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {

            // 項目が選択された時に呼ばれる
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spinnerText3 = parent?.selectedItem as String
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }

        //削除ボタン2段目
        btnDelete2.setOnClickListener {
            et12.setText("")
            spinnerItems2.removeAt(0)
            spinnerItems2.add(0,"選択してください")
            spinnerText2 = "選択してください"
            val toast = Toast.makeText(this, "タスク2を削除しました。", Toast.LENGTH_SHORT)
            toast.show()
        }
        btnDelete3.setOnClickListener {
            et13.setText("")
            spinnerItems3.removeAt(0)
            spinnerItems3.add(0,"選択してください")
            spinnerText3 = "選択してください"
            val toast = Toast.makeText(this, "タスク3を削除しました。", Toast.LENGTH_SHORT)
            toast.show()
        }


        //登録ボタン（アクティビティの終了）
        btnBack.setOnClickListener {
            helper = TestOpenHelper(applicationContext)
            db = helper.writableDatabase
            val task = et1.text.toString()
            val task2 = et12.text.toString()
            val task3 = et13.text.toString()
            val reward = et2.text.toString()
            if(reward == "") {
                val toast = Toast.makeText(this, "ご褒美を入力してください", Toast.LENGTH_SHORT)
                toast.show()
            }else{
                // 入力したテキストをSQLiteに登録
                insertData(db,getDate,spinnerText,task,spinnerText2,task2,spinnerText3,task3,reward,isChecked,isChecked12,isChecked13,isChecked2)
                val intentBack = Intent(application, MainActivity::class.java)
                startActivity(intentBack)
            }

        }
    }



    // SQLiteに登録
    private fun insertData(db: SQLiteDatabase, dateData: String?, genreData:String, taskData: String, genreData2:String, taskData2: String, genreData3:String, taskData3: String, rewardData: String, taskChecker: Int,taskChecker2: Int,taskChecker3: Int, rewardChecker: Int) {
        val values = ContentValues()
        values.put("date", dateData)
        values.put("genre", genreData)
        values.put("task", taskData)
        values.put("genre2", genreData2)
        values.put("task2", taskData2)
        values.put("genre3", genreData3)
        values.put("task3", taskData3)
        values.put("reward", rewardData)
        values.put("taskChecker", taskChecker)
        values.put("taskChecker2", taskChecker2)
        values.put("taskChecker3", taskChecker3)
        values.put("rewardChecker", rewardChecker)
        db.insert("testdb", null, values)
        db.update("testdb", values, "date = ?", arrayOf(dateData))
    }
}