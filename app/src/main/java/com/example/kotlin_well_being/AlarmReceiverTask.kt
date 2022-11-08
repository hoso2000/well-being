package com.example.kotlin_well_being

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class AlarmReceiverTask : BroadcastReceiver() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getIntExtra("id", 1)
        val toast = Toast.makeText(context, "アラーム2による処理が実行されました。", Toast.LENGTH_SHORT)
        toast.show()
        val CHANNEL_ID2 = "channel_id2"
        val channel_name = "channel_name"
        val channel_description = "channel_description "
        val myApp = MyApp.getInstance()
        var notificationId = 0


        val builder = NotificationCompat.Builder(context, CHANNEL_ID2)
            .setSmallIcon(R.drawable.ic_launcher_background)    /// 表示されるアイコン
            .setContentTitle("{$id}22時です。")                  /// 通知タイトル
            .setContentText("通知が表示されたら報告してください。")           /// 通知コンテンツ
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        /// ボタンを押して通知を表示
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
            notificationId += 1
        }

    }
//    private lateinit var helper: TestOpenHelper
//    private lateinit var db: SQLiteDatabase
//    private val applicationContext: Context? = null
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onReceive(context: Context, intent: Intent) {
//        val toast = Toast.makeText(context, "アラームによる処理が実行されました。", Toast.LENGTH_SHORT)
//        toast.show()
//        val CHANNEL_ID = "channel_id"
//        val channel_name = "channel_name"
//        val channel_description = "channel_description "
//        val myApp = MyApp.getInstance()
//        var notificationId3 = myApp.notificationId
//        var contentTitle = "a"
//        var contentText = "a"
//
//        //　db関連
//        helper = TestOpenHelper(applicationContext)
//        db = helper.readableDatabase
//
//        val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)
//        val defaultDate = LocalDate.now()
//        var date = format.format(defaultDate)
//
//
//        var task:String = ""
//        var taskChecked:Int = 0
//        var rewardChecked:Int = 0
//
//        val cursor = db.query(
//            "testdb", arrayOf("date", "genre", "task", "reward", "taskChecker", "rewardChecker"),
//            null,
//            null,
//            null,
//            null,
//            null
//        )
//        //初起動時DBの要素ゼロで実行されるのを防止するため
//        if (cursor.count != 0) {
//            cursor.moveToFirst()
//            //データベース内を探索
//            for (i in 0 until cursor.count ) {
//                // 同じ日付を見つけたら呼び出して、終了
//                if(cursor.getString(0) == date){
//                    //　taskとやりたいことを挿入
//                    task = cursor.getString(1)
//                    taskChecked = cursor.getInt(4)
//                    rewardChecked = cursor.getInt(5)
//                    break
//                }
//                cursor.moveToNext()
//            }
//        }
//        //ここまで
//
//        if (task == "授業"){
//            contentTitle = "授業"
//            contentText = "授業です"
//        }else{
//            contentTitle = "タスク"
//            contentText = "課題はやった？"
//        }
//
//
//        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
//            .setSmallIcon(R.drawable.ic_launcher_background)    /// 表示されるアイコン
//            .setContentTitle(contentTitle)                  /// 通知タイトル
//            .setContentText(contentText)           /// 通知コンテンツ
//            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//
//            /// ボタンを押して通知を表示
//        with(NotificationManagerCompat.from(context)) {
//            notify(notificationId3, builder.build())
//            notificationId3 += 1
//        }



//    private fun readData() {
//        helper = TestOpenHelper(applicationContext)
//        db = helper.writableDatabase
//
//        val format = SimpleDateFormat("yyyy/MM/dd", Locale.US)
//        val defaultDate = LocalDate.now()
//        var date = format.format(defaultDate)
//
//
//        var task:String = ""
//        val taskChecked:Int = 0
//        val rewardChecked:Int = 0
//        val contentTitle:String
//        val contentText:String
//
//        val cursor = db.query(
//            "testdb", arrayOf("date", "genre", "task", "reward", "taskChecker", "rewardChecker"),
//            null,
//            null,
//            null,
//            null,
//            null
//        )
//        //初起動時DBの要素ゼロで実行されるのを防止するため
//        if (cursor.count != 0) {
//            cursor.moveToFirst()
//            //データベース内を探索
//            for (i in 0 until cursor.count ) {
//                // 同じ日付を見つけたら呼び出して、終了
//                if(cursor.getString(0) == date){
//                    //　taskとやりたいことを挿入
//                    task = cursor.getString(1)
//                    taskChecked = cursor.getInt(4)
//                    rewardChecked = cursor.getInt(5)
//                    break
//                }
//                cursor.moveToNext()
//            }
//        }
//        // ここまで
//    }
}