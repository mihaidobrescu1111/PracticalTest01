package ro.pub.cs.systems.eim.practicaltest01var08

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder

class PracticalTest01Var08Service : Service() {
    private var processingThread: ProcessingThread? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val answer = intent?.getStringExtra("answer") ?: ""
        processingThread = ProcessingThread(this, answer)
        processingThread?.start()
        return START_REDELIVER_INTENT
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        processingThread?.stopThread()
        super.onDestroy()
    }
}

class ProcessingThread(private val context: Context, private val answer: String) : Thread() {
    private var isRunning = true

    override fun run() {
        while (isRunning) {
            val len = answer.length
            val randomPos = (0..<len).random()
            var newAnswer = ""

            for (i in (0..<len)) {
                if (i==randomPos)
                    newAnswer += answer[i]
                else
                    newAnswer += "*"
            }

            val intent = Intent()
            intent.action = "ro.pub.cs.systems.eim.practicaltest01var08.SOME_SERVICE_PERMISSION"
            intent.putExtra("message", newAnswer)
            context.sendBroadcast(intent)

            try {
                sleep(5000)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    fun stopThread() {
        isRunning = false
    }
}