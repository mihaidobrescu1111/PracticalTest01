package ro.pub.cs.systems.eim.practicaltest01var08

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity()  {
    val buttonClickListener = ButtonClickListener()
    var serviceStarted = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.secondary_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.secondary)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val checkButton = findViewById<Button>(R.id.checkButton)
        checkButton.setOnClickListener(buttonClickListener)
        val backButton = findViewById<Button>(R.id.backButton)
        backButton.setOnClickListener(buttonClickListener)

        val intentFromParent = intent
        val data = intentFromParent.extras

        val riddle = findViewById<TextView>(R.id.userRiddle)
        riddle.text = data?.getString("riddle")

        checkAndStartService()
    }

    private fun checkAndStartService() {
        val intentFromParent = intent
        val data = intentFromParent.extras
        val answer = data?.getString("answer").toString()
        val intent = Intent(this, PracticalTest01Var08Service::class.java)
        intent.putExtra("answer", answer)
        startService(intent)
        serviceStarted = true
    }

    private val messageBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.d("PracticalTest01Broadcast", intent?.getStringExtra("message") ?: "Empty")
        }
    }

    private val intentFilter = IntentFilter().apply {
        addAction("ro.pub.cs.systems.eim.practicaltest01var08.SOME_SERVICE_PERMISSION")
    }


    override fun onResume() {
        super.onResume()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            registerReceiver(messageBroadcastReceiver, intentFilter,RECEIVER_EXPORTED)
        } else {
            registerReceiver(messageBroadcastReceiver, intentFilter)
        }
    }

    override fun onPause() {
        unregisterReceiver(messageBroadcastReceiver)
        super.onPause()
    }




    override fun onDestroy() {
        val intent = Intent(this, PracticalTest01Var08Service::class.java)
        serviceStarted = false
        stopService(intent)
        super.onDestroy()
    }


    inner class ButtonClickListener: View.OnClickListener {
        override fun onClick(v: View?) {
            val intentToParent = Intent()
            if (v?.id == R.id.checkButton) {
                val intentFromParent = intent
                val data = intentFromParent.extras
                val answer = data?.getString("answer").toString()
                val userAnswer = findViewById<EditText>(R.id.userAnswer)
                if (answer == userAnswer.text.toString()) {
                    setResult(RESULT_OK, intentToParent)
                    finish()
                } else {
                    setResult(RESULT_CANCELED, intentToParent)
                    finish()
                }

            } else if (v?.id == R.id.backButton) {
                setResult(RESULT_CANCELED, intentToParent)
                finish()
            }
        }
    }
}