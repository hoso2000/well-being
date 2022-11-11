package com.example.kotlin_well_being

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // カレンダー関連
        val format = SimpleDateFormat("yyyy/MM/d", Locale.US)
        val calendarView:CalendarView = findViewById(R.id.calendarView)
        val defaultDate = calendarView.date
        var date = format.format(defaultDate)

        val task:CheckBox = findViewById(R.id.task)
        val reward:CheckBox = findViewById(R.id.reward)
        //val btnSend:Button = findViewById(R.id.btnSend)
        val ACbtn:View = findViewById(R.id.AcButton)

        var taskChecked: Int
        var rewardChecked: Int

        // alertのランダムで表示されるメッセージと画像の配列
        val taskMessage = arrayOf("よく頑張ったね","お疲れ様","最高")
        val rewardMessage = arrayOf("楽しい一日になったね","すてきな一日","パーフェクト")
        val taskImage = arrayOf(R.drawable.good1,R.drawable.good2,R.drawable.good3,R.drawable.good4,R.drawable.good5,R.drawable.good6,R.drawable.good7)
        val rewardImage = arrayOf(R.drawable.good1,R.drawable.good2,R.drawable.good3,R.drawable.good4,R.drawable.good5,R.drawable.good6,R.drawable.good7)

//        // push通知関連
//        val CHANNEL_ID = "channel_id"
//        val channel_name = "channel_name"
//        val channel_description = "channel_description "

        readData(date)
        task.isClickable = task.text != "登録してください"  //初期状態でチェックは付けられない
        reward.isClickable = reward.text != "登録してください"
        reward.isClickable = task.isChecked //taskが終わらないとrewardを押せない

        backColor(task.isChecked, reward.isChecked)

        // 22時に表示されない
        val alarmMgr: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent: PendingIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
            PendingIntent.getBroadcast(this, 0, intent, 0)
        }
        //2つ目の通知で使う予定だった。
//        val alarmMgrTask: AlarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
//        val intent = Intent(this, AlarmReceiverTask::class.java)
//        intent.putExtra("id",1)
//        val alarmIntentTask = PendingIntent.getBroadcast(this, 1, intent, 0)

        // 時間指定で通知する
        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 13)
            set(Calendar.MINUTE, 30)
        }
        alarmMgr.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )

//        alarmMgr.setRepeating(
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            SystemClock.elapsedRealtime() + 1 * 1000,
//            3 * 1000,
//            alarmIntent
//        )
//        alarmMgrTask.setRepeating(
//            AlarmManager.ELAPSED_REALTIME_WAKEUP,
//            SystemClock.elapsedRealtime() + 2 * 1000,
//            5 * 1000,
//            alarmIntentTask
//        )

        // 日付を取得
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            var fixMonth = (month % 12) + 1
            date = "$year/$fixMonth/$dayOfMonth"
            readData(date)
            task.isClickable = task.text != "登録してください"  //初期状態でチェックは付けられない
            reward.isClickable = reward.text != "登録してください"
            reward.isClickable = task.isChecked
            backColor(task.isChecked, reward.isChecked)
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
            backColor(task.isChecked, reward.isChecked)
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
            backColor(task.isChecked, reward.isChecked)
        }

        //編集画面へ遷移
        ACbtn.setOnClickListener {
            val intent = Intent(application, SubActivity::class.java)
            intent.putExtra("DATE_KEY",date)
            startActivity(intent)
        }

//        // 通知の表示
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            val name = channel_name
//            val descriptionText = channel_description
//            val importance = NotificationManager.IMPORTANCE_DEFAULT
//            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
//                description = descriptionText
//            }
//            /// チャネルを登録
//            val notificationManager: NotificationManager =
//                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            notificationManager.createNotificationChannel(channel)
//    }
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
            "testdb", arrayOf("date", "genre", "task", "reward", "taskChecker", "rewardChecker"),
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
                    task.text = cursor.getString(1) + "　" + cursor.getString(2)
                    reward.text = cursor.getString(3)
                    taskChecked = cursor.getInt(4)
                    rewardChecked = cursor.getInt(5)
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

    // task完了度により背景色を変化
    private fun backColor(taskChecked: Boolean, rewardChecked: Boolean){
        val constraint: View =findViewById(R.id.constraint)
        if (taskChecked && rewardChecked){
            constraint.background = getDrawable(R.color.check2)
        }else if (taskChecked || rewardChecked){
            constraint.background = getDrawable(R.color.check1)
        }else{
            constraint.background = getDrawable(R.color.check0)
        }
    }

}