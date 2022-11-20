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
        var rewardChecked = 0
        var sumTask = 0
        var sumReward = 0
        var sum = 0
        val cursor = db.query(
            "taskdb", arrayOf("_id", "dateId", "genre", "task", "taskChecker"),
            //"dateId = ${date}",
            null,
            null,
            null,
            null,
            null
        )
        val cursor2 = db.query(
            "rewarddb", arrayOf("_id2", "dateId", "reward", "rewardChecker"),
//            "dateId = ${date}",
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
                taskChecked = cursor.getInt(4)
                sumTask += taskChecked
                cursor.moveToNext()
            }
        }
        cursor.close()
        if (cursor2.count != 0) {
            cursor2.moveToFirst()
            //データベース内を探索
            for (i in 0 until cursor2.count  ) {
                // 同じ日付を見つけたら呼び出して、終了
                rewardChecked = cursor2.getInt(3)
                sumReward += rewardChecked
                cursor2.moveToNext()
            }
        }
        cursor2.close()
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