package com.example.kotlin_well_being

import android.app.NotificationChannel
import android.app.NotificationManager
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
import java.util.*


class AlarmReceiver : BroadcastReceiver() {
    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onReceive(context: Context, intent: Intent) {
        val toast = Toast.makeText(context, "アラームによる処理が実行されました。", Toast.LENGTH_SHORT)
        toast.show()
        val CHANNEL_ID = "channel_id"
        val channel_name = "channel_name"
        val channel_description = "channel_description "
        //val myApp = MyApp.getInstance()
        var notificationId = 0
        var contentTitle = "テスト"
        var contentText = "テスト"

        //　db関連
        helper = TestOpenHelper(context)
        db = helper.readableDatabase

        val format = SimpleDateFormat("yyyy/MM/d", Locale.US)
        val defaultDate = Date()
        val date = format.format(defaultDate)

        var task: String = ""
        var taskChecked: Int = 0
        var rewardChecked: Int = 0

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
            for (i in 0 until cursor.count) {
                // 同じ日付を見つけたら呼び出して、終了
                if (cursor.getString(0) == date) {
                    //　taskとやりたいことを挿入
                    task = cursor.getString(1)
                    taskChecked = cursor.getInt(4)
                    rewardChecked = cursor.getInt(5)
                    break
                }
                cursor.moveToNext()
            }
        }
        cursor.close()

        if (task == "授業") {
            contentTitle = "授業"
            contentText = "午後の授業も頑張ろう！"
        } else if(task == "課題") {
            contentTitle = "課題"
            contentText = "まずは机に向かってみよう！"
        }else if(task == "勉強") {
            contentTitle = "勉強"
            contentText = "まずは1ページだけ進めよう！"
        }else if(task == "研究・ゼミ") {
            contentTitle = "研究・ゼミ"
            contentText = "焦らず毎日コツコツと進めていこう！"
        }else if(task == "部活・サークル") {
            contentTitle = "部活・サークル"
            contentText = "めいいっぱい楽しもう！"
        }else if(task == "プロジェクト活動") {
            contentTitle = "プロジェクト活動"
            contentText = "頑張った分だけ力になるよ"
        }else if(task == "就活") {
            contentTitle = "就活"
            contentText = "頑張った分だけ内定に近づくよ！"
        }else if(task == "家事") {
            contentTitle = "家事"
            contentText = "目の前の片付けから始めてみよう！"
        }else {
            contentTitle = "タスク"
            contentText = "お疲れ様！午後も頑張ろう！"
        }
        if (taskChecked == 1){
            contentTitle="お疲れ様"
            contentText="あとは好きなことを楽しもう"
        }


        if (rewardChecked != 1) {
            // 通知の表示
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val name = channel_name
                val descriptionText = channel_description
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                    description = descriptionText
                }
                /// チャネルを登録
                val notificationManager: NotificationManager =
                    context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                notificationManager.createNotificationChannel(channel)
            }

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_background)    /// 表示されるアイコン
                .setContentTitle(contentTitle)                  /// 通知タイトル
                .setContentText(contentText)           /// 通知コンテンツ
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
                notificationId += 1
            }
        }
    }
}