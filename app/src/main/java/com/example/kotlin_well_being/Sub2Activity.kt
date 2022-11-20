package com.example.kotlin_well_being

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity


class Sub2Activity : AppCompatActivity()  {
    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sub2)

        val btnBack2 :Button = findViewById(R.id.btnBack2)
        helper = TestOpenHelper(applicationContext)
        db = helper.readableDatabase

        val sumText: TextView =  findViewById(R.id.sumText)
        var taskChecked = 0
        var taskChecked2 = 0
        var taskChecked3 = 0
        var rewardChecked = 0
        var sumTask = 0
        var sumReward = 0
        var sum = 0
        val cursor = db.query(
            "testdb", arrayOf("date", "genre", "task","genre2", "task2","genre3", "task3", "reward", "taskChecker","taskChecker2","taskChecker3", "rewardChecker"),
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
            for (i in 0 until cursor.count  ) {
                // 同じ日付を見つけたら呼び出して、終了
                taskChecked = cursor.getInt(8)
                sumTask += taskChecked
                taskChecked2 = cursor.getInt(9)
                sumTask += taskChecked2
                taskChecked3 = cursor.getInt(10)
                sumTask += taskChecked3
                rewardChecked = cursor.getInt(11)
                sumReward += rewardChecked
                cursor.moveToNext()
            }
        }
        cursor.close()
        sum = sumTask + sumReward
        sumText.text = sum.toString()

        val myImage: ImageView = findViewById(R.id.imageView)
        // 2週間パターン
//        if (sum <= 4){
//            myImage.setImageResource(R.drawable.f0)
//        }else if (5 <= sum && sum <= 8){
//            myImage.setImageResource(R.drawable.f1)
//        }else if (9 <= sum && sum <= 14) {
//            myImage.setImageResource(R.drawable.f2)
//        }else if (13 <= sum && sum <= 20) {
//            myImage.setImageResource(R.drawable.f3)
//        }
//        else{
//            myImage.setImageResource(R.drawable.f4)
//        }

        // 1週間パターン
        if (sum <= 2){
            myImage.setImageResource(R.drawable.f0)
        }else if (4 <= sum && sum <= 5){
            myImage.setImageResource(R.drawable.f1)
        }else if (6 <= sum && sum <= 7) {
            myImage.setImageResource(R.drawable.f2)
        }else if (8 <= sum && sum <= 11) {
            myImage.setImageResource(R.drawable.f3)
        }
        else{
            myImage.setImageResource(R.drawable.f4)
        }

        btnBack2.setOnClickListener {
            finish()
        }

    }
}