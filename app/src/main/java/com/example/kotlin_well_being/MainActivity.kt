package com.example.kotlin_well_being

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var helper: TestOpenHelper
    private lateinit var db: SQLiteDatabase

    private var preference: SharedPreferences? = null
    private var editor: SharedPreferences.Editor? = null

    private var alarmMgr: AlarmManager? = null
    private lateinit var alarmIntent: PendingIntent

    private var id: Int = 0
    private val FROM = arrayOf("name", "check")
    // リソースのコントロールID
    private val TO = intArrayOf(R.id.listTextView, R.id.listCheckBox)

    private class MyAdapter(
        context: Context?, data: MutableList<MutableMap<String?, Any>?>,
        resource: Int, from: Array<String>, to: IntArray?,_ids:List<Int>,_db: SQLiteDatabase,
        _showDialogFunc:() ->Unit
    ) :
        SimpleAdapter(context, data, resource, from, to) {
        // 外部から呼び出し可能なマップ
        var checkList: MutableMap<Int, Boolean?> = HashMap()

        val ids:kotlin.collections.List<Int>
        val db:SQLiteDatabase

        var showDialogFunc:()->Unit

        init {
            ids = _ids
            db=_db
            showDialogFunc = _showDialogFunc
            // 初期値を設定する
            for (i in data.indices) {
                val map = data[i] as Map<*, *>?
                checkList[i] = map!!["check"] as Boolean?
            }
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            val view = super.getView(position, convertView, parent)
            val ch = view.findViewById<CheckBox>(R.id.listCheckBox)

            // チェックの状態が変化した場合はマップに記憶する
            ch.setOnCheckedChangeListener { buttonView, isChecked ->
                checkList[position] = isChecked
                //Toast.makeText(convertView!!.context,ids[position],Toast.LENGTH_SHORT).show()
                val id:Int = ids[position]
                val values = ContentValues()
                var taskChecker: Int
                if (isChecked) {
                    taskChecker = 1
                    showDialogFunc()
                }else{
                    taskChecker = 0
                }
                values.put("taskChecker", taskChecker)
                db.update("taskdb", values, "_id = ?", arrayOf(id.toString()))
            }
            return view
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        preference = getSharedPreferences("Preference Name", MODE_PRIVATE)
        editor = preference?.edit()

        // 初回起動時のみの処理
        if (preference?.getBoolean("Launched", false)==false) {
            //初回起動時の処理
            // 指定時間でアラーム起動。通知を飛ばす。
            alarmMgr = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmIntent = Intent(this, AlarmReceiver::class.java).let { intent ->
                PendingIntent.getBroadcast(this, 0, intent, 0)
            }

            // 時間指定で通知する
            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.HOUR_OF_DAY, 15)
                set(Calendar.MINUTE, 15)
            }
            alarmMgr?.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                //AlarmManager.INTERVAL_DAY,
                1000 * 60 * 15,
                alarmIntent
            )
            //プリファレンスの書き変え
            editor?.putBoolean("Launched", true);
            editor?.commit();
        }

        // カレンダー関連
        val format = SimpleDateFormat("yyyy/MM/d", Locale.US)
        val calendarView:CalendarView = findViewById(R.id.calendarView)
        val defaultDate = calendarView.date
        var date = format.format(defaultDate)

        val reward:CheckBox = findViewById(R.id.reward)
        val btnSend:Button = findViewById(R.id.btnSend)
        val btnMemory:Button = findViewById(R.id.btnMemory)

        var taskChecked: Int
        var rewardChecked: Int

        // alertのランダムで表示されるメッセージと画像の配列
        val rewardMessage = arrayOf("楽しい一日になったね","充実した一日になったね","明日もこの調子で頑張ろう！")
        val rewardImage = arrayOf(R.drawable.good1,R.drawable.good2,R.drawable.good3,R.drawable.good4,R.drawable.good5,R.drawable.good6,R.drawable.good7)

        readData(date)
        reward.isClickable = reward.text != "登録してください"
        //reward.isClickable = task.isChecked //taskが終わらないとrewardを押せない

        //changeChar(task.isChecked, reward.isChecked)

//        alarmMgr?.setRepeating(
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
            //task.isClickable = task.text != "登録してください"  //初期状態でチェックは付けられない
            reward.isClickable = reward.text != "登録してください"
            //reward.isClickable = task.isChecked
            //changeChar(task.isChecked, reward.isChecked)
        }

        //タスクが終わったら褒めるアラート
//        task.setOnClickListener{
//            if (task.isChecked) {
//                taskChecked = 1
//                val image = ImageView(this)
//                image.setImageResource(taskImage.random())
//                AlertDialog.Builder(this).apply {
//                    setTitle("お疲れ様")
//                    setMessage(taskMessage.random())
//                    setView(image)
//                    setNegativeButton("OK",null)
//                    show()
//                }
//            }else{
//                taskChecked = 0
//            }
//            insertTaskChecker(db,id,taskChecked)
//            reward.isClickable = task.isChecked
//            changeChar(task.isChecked, reward.isChecked)
//        }

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
            insertRewardChecker(db,id,rewardChecked)
            //changeChar(task.isChecked, reward.isChecked)
        }

        //編集画面へ遷移
        btnSend.setOnClickListener {
            val intent = Intent(application, SubActivity::class.java)
            intent.putExtra("DATE_KEY",date)
            startActivity(intent)
        }

        //記録画面へ遷移
        btnMemory.setOnClickListener {
            val intent2 = Intent(this,Sub2Activity::class.java)
            startActivity(intent2)
        }
    }

    // SQLiteのデータを読み込む
    @SuppressLint("SuspiciousIndentation")
    private fun readData(date: String) {
        helper = TestOpenHelper(applicationContext)
        db = helper.readableDatabase

        // リスト関連
        val lv: ListView = findViewById(R.id.listView)
        val list: MutableList<MutableMap<String?, Any>?> = ArrayList()
        //var map: MutableMap<String?, Any> = HashMap() イラン候補

        val test:TextView = findViewById(R.id.testView)
        val reward:CheckBox = findViewById(R.id.reward)
        test.text = date
        reward.text = "登録してください"

        var taskChecked = 0
        var rewardChecked = 0

        val cursor = db.query(
            "taskdb", arrayOf("_id", "dateId", "genre", "task", "taskChecker"),
            "dateId == ?",
            //null,
            arrayOf(date),
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
        val ids = mutableListOf<Int>()
        if (cursor.count != 0) {
            cursor.moveToFirst()
            test.text = cursor.getString(1)
            //データベース内を探索
            for (i in 0 until cursor.count ) {
                // 同じ日付を見つけたら呼び出して、終了
                id  =cursor.getInt(0)
                ids.add(id)
                var vmap: MutableMap<String?, Any> = HashMap()
                vmap["name"] =cursor.getString(2) + "　" + cursor.getString(3)
                taskChecked = cursor.getInt(4)
                vmap["check"] = taskChecked == 1
                list.add(vmap)
                if (i != cursor.count) {
                    cursor.moveToNext()
                }
            }
        }
        cursor.close()

        val adapter = MyAdapter(
            this@MainActivity,
            list, R.layout.list, FROM, TO,ids,db,::showDialog
        )
        lv.adapter = adapter

        if (cursor2.count != 0) {
            cursor2.moveToFirst()
            if(cursor2.getString(1) == date) {
                //データベース内を探索
                for (i in 0 until cursor2.count) {
                    id = cursor2.getInt(0)
                    reward.text = cursor2.getString(    2)
                    rewardChecked = cursor2.getInt(3)
                    break
                }
                cursor2.moveToNext()
            }
            reward.isChecked = rewardChecked == 1
        }
        cursor2.close()
    }

    // task checkboxを登録
//    private fun insertTaskChecker(db: SQLiteDatabase, id: Int, taskChecker: Int) {
//        val values = ContentValues()
//
//        values.put("taskChecker", taskChecker)
//        //db.insert("testdb", null, values)
//        db.update("taskdb", values, "_id = ?", arrayOf(id.toString()))
//    }

    // reward checkboxを登録
    private fun insertRewardChecker(db: SQLiteDatabase, id: Int, rewardChecker: Int) {
        val values = ContentValues()

        values.put("rewardChecker", rewardChecker)
        //db.insert("testdb", null, values)
        db.update("rewarddb", values, "date = ?", arrayOf(id.toString()))
    }

    // task完了度によりキャラクターが変化
    private fun changeChar(taskChecked: Boolean, rewardChecked: Boolean){
        val myImage: ImageView = findViewById(R.id.imageView)
//        if (taskChecked && rewardChecked){
//            myImage.setImageResource(R.drawable.good1)
//        }else if (taskChecked && !rewardChecked){
//            myImage.setImageResource(R.drawable.reward)
//        }else{
//            myImage.setImageResource(R.drawable.task)
//        }
    }

    private fun showDialog(){
//        val image = ImageView(this@MyAdapter)
//        image.setImageResource(taskImage.random())
        val taskMessage = arrayOf("お疲れ様！よく頑張ったね","流石だね！","今日も目標達成できてえらい！")
        val taskImage = arrayOf(R.drawable.good1,R.drawable.good2,R.drawable.good3,R.drawable.good4,R.drawable.good5,R.drawable.good6,R.drawable.good7)
        AlertDialog.Builder(this).apply {
            setTitle("お疲れ様")
            setMessage(taskMessage.random())
            //setView(image)
            setNegativeButton("OK",null)
            show()
        }
    }
}