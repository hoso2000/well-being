package com.example.kotlin_well_being

import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import java.security.AccessController.getContext
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // カレンダー関連
        val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        val calendarView:CalendarView = findViewById(R.id.calendarView)
        val defaultDate = calendarView.date
        var date = format.format(defaultDate)
        calendarView.setFocusedMonthDateColor(Color.BLUE)

        val task:CheckBox = findViewById(R.id.task)
        val reward:CheckBox = findViewById(R.id.reward)
        val btnSend:Button = findViewById(R.id.btnSend)

        var taskChecked: Int
        var rewardChecked: Int

        // alertのランダムで表示されるメッセージと画像の配列
        val taskMessage = arrayOf("よく頑張ったね","えらい","最高")
        val rewardMessage = arrayOf("楽しい一日になったね","すてきな一日","パーフェクト")
        val taskImage = arrayOf(R.drawable.good1,R.drawable.good2)
        val rewardImage = arrayOf(R.drawable.good1,R.drawable.good2)

        readData(date)
        reward.isClickable = task.isChecked //taskが終わらないとrewardを押せない

        // 日付を取得
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var fixMonth = (month % 12) + 1
            date = "$year/$fixMonth/$dayOfMonth"
            readData(date)
            reward.isClickable = task.isChecked
        }

        //タスクが終わったら褒めるアラート
        task.setOnClickListener{
            if (task.isChecked) {
                taskChecked = 1
                val image = ImageView(this)
                image.setImageResource(taskImage.random())
                AlertDialog.Builder(this).apply {
                    setTitle("お疲れ様")
                    setMessage(taskMessage.random())
                    setView(image)
                    setNegativeButton("OK",null)
                    show()
                }
            }else{
                taskChecked = 0
            }
            insertTaskChecker(db,date,taskChecked)
            reward.isClickable = task.isChecked
        }

        //ご褒美が終わったら褒めるアラート
        reward.setOnClickListener{
            if (reward.isChecked) {
                rewardChecked = 1
                val image = ImageView(this)
                image.setImageResource(rewardImage.random())
                AlertDialog.Builder(this)
                    .setTitle("パーフェクト")
                    .setMessage(rewardMessage.random())
                    .setView(image)
                    .setNegativeButton("OK", null)
                    .show()
            }else{
                rewardChecked = 0
            }
            insertRewardChecker(db,date,rewardChecked)
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

        var taskChecked = 0
        var rewardChecked = 0

        val cursor = db.query(
            "testdb", arrayOf("date", "task", "reward", "taskChecker", "rewardChecker"),
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
                    taskChecked = cursor.getInt(3)
                    rewardChecked = cursor.getInt(4)
                    break
                }
                cursor.moveToNext()
            }
            task.isChecked = taskChecked == 1
            reward.isChecked = rewardChecked == 1
        }
        cursor.close()
    }

    // task checkboxを登録
    private fun insertTaskChecker(db: SQLiteDatabase, dateData: String?, taskChecker: Int) {
        val values = ContentValues()

        values.put("taskChecker", taskChecker)
        db.insert("testdb", null, values)
        db.update("testdb", values, "date = ?", arrayOf(dateData))
    }

    // reward checkboxを登録
    private fun insertRewardChecker(db: SQLiteDatabase, dateData: String?, rewardChecker: Int) {
        val values = ContentValues()

        values.put("rewardChecker", rewardChecker)
        db.insert("testdb", null, values)
        db.update("testdb", values, "date = ?", arrayOf(dateData))
    }
}