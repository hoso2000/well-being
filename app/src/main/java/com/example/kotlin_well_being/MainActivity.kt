package com.example.kotlin_well_being

import android.content.DialogInterface
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {

    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val task:CheckBox = findViewById(R.id.task)
        val reward:CheckBox = findViewById(R.id.reward)
        val btnSend:Button = findViewById(R.id.btnSend)

        readData()

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
            startActivity(intent)
        }
    }

    // SQLiteのデータを読み込む
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
        //現状はリストの後ろまでfor文で回して最新のタスクを表示させるよう仮置き
        cursor.moveToFirst()
//        val sbuilder = StringBuilder()
        for (i in 0 until cursor.count -1) {
//            sbuilder.append(cursor.getString(0))
//            sbuilder.append(": ")
//            sbuilder.append(cursor.getString(1))
//            sbuilder.append("\n")
           cursor.moveToNext()
        }

        // 忘れずに！　taskとやりたいことを挿入
        val task:CheckBox = findViewById(R.id.task)
        val reward:CheckBox = findViewById(R.id.reward)
        task.text = cursor.getString(0)
        reward.text = cursor.getString(1)
        cursor.close()
    }
}