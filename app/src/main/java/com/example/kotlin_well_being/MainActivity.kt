package com.example.kotlin_well_being

import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        val calendarView:CalendarView = findViewById(R.id.calendarView)
        val defaultDate = calendarView.date
        var date = format.format(defaultDate)

        val task:CheckBox = findViewById(R.id.task)
        val reward:CheckBox = findViewById(R.id.reward)
        val btnSend:Button = findViewById(R.id.btnSend)

        readData(date)

        // 日付を取得
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var fixMonth = (month % 12) + 1
            date = "$year/$fixMonth/$dayOfMonth"
            readData(date)
        }

        //タスクが終わったら褒めるアラート
        task.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                AlertDialog.Builder(this)
                    .setTitle("お疲れ様")
                    .setMessage("よく頑張ったね")
                    .setNegativeButton("OK", null)
                    .show()
            }
        }

        //ご褒美が終わったら褒めるアラート
        reward.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                AlertDialog.Builder(this)
                    .setTitle("パーフェクト")
                    .setMessage("楽しい一日になったね！")
                    .setNegativeButton("OK", null)
                    .show()
            }
        }

        //編集画面へ遷移
        btnSend.setOnClickListener {
            val intent = Intent(application, SubActivity::class.java)
            intent.putExtra("DATE_KEY",date)
            startActivity(intent)
        }
    }

    // SQLiteのデータを読み込む
    private fun readData(date: String) {
        helper = TestOpenHelper(applicationContext)
        db = helper.readableDatabase

        val test:TextView = findViewById(R.id.testView)
        val task:CheckBox = findViewById(R.id.task)
        val reward:CheckBox = findViewById(R.id.reward)
        test.text = date
        task.text = "登録してください"
        reward.text = "登録してください"

        val cursor = db.query(
            "testdb", arrayOf("date", "task", "reward"),
            null,
            null,
            null,
            null,
            null
        )
        //初起動時DBの要素ゼロで実行されるのを防止するため
        if (cursor.count != 0) {
            cursor.moveToFirst()
            //データベース内を探索
            for (i in 0 until cursor.count ) {
                // 同じ日付を見つけたら呼び出して、終了
                if(cursor.getString(0) == date){
                    //　taskとやりたいことを挿入
                    test.text = cursor.getString(0)
                    task.text = cursor.getString(1)
                    reward.text = cursor.getString(2)
                    break
                }
                cursor.moveToNext()
            }
        }
        cursor.close()
    }
}