package com.example.kotlin_well_being

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat.getSystemService

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val toast = Toast.makeText(context, "アラームによる処理が実行されました。", Toast.LENGTH_SHORT)
        toast.show()
        val CHANNEL_ID = "channel_id"
        val channel_name = "channel_name"
        val channel_description = "channel_description "
        val myApp = MyApp.getInstance()
        var notificationId = myApp.notificationId


        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)    /// 表示されるアイコン
            .setContentTitle("22時です。")                  /// 通知タイトル
            .setContentText("今日の振り返りと明日の目標を決めましょう。")           /// 通知コンテンツ
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        /// ボタンを押して通知を表示
        with(NotificationManagerCompat.from(context)) {
            notify(notificationId, builder.build())
            notificationId += 1
        }

    }
}