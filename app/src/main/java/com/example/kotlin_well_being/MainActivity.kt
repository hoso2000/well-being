package com.example.kotlin_well_being

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnAdd:Button = findViewById(R.id.btnAdd)
        val task:CheckBox = findViewById(R.id.task)
        val btnAdd2:Button = findViewById(R.id.btnAdd2)
        val reward:CheckBox = findViewById(R.id.reward)

        //タスクを入力
        btnAdd.setOnClickListener{
            val et =EditText(this)

            AlertDialog.Builder(this)
                .setTitle("タスク")
                .setMessage("今日のやらなければならないことを入力してください")
                .setView(et)
                .setPositiveButton("追加", DialogInterface.OnClickListener{ dialogInterface, i ->
                    val myTodo = et.text.toString()
                    task.text = myTodo
                })
                .setNegativeButton("キャンセル",null)
                .show()
        }

        //やりたいことを入力
        btnAdd2.setOnClickListener{
            val et =EditText(this)

            AlertDialog.Builder(this)
                .setTitle("ご褒美")
                .setMessage("今日のやりたいことを入力してください")
                .setView(et)
                .setPositiveButton("追加", DialogInterface.OnClickListener{ dialogInterface, i ->
                    val myTodo2 = et.text.toString()
                    reward.text = myTodo2
                })
                .setNegativeButton("キャンセル",null)
                .show()
        }

        task.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                AlertDialog.Builder(this)
                    .setTitle("お疲れ様")
                    .setMessage("よく頑張ったね")
                    .setNegativeButton("OK", null)
                    .show()
            }
        }

        reward.setOnCheckedChangeListener{ _, isChecked ->
            if (isChecked) {
                AlertDialog.Builder(this)
                    .setTitle("パーフェクト")
                    .setMessage("楽しい一日になったね！")
                    .setNegativeButton("OK", null)
                    .show()
            }
        }
    }
}