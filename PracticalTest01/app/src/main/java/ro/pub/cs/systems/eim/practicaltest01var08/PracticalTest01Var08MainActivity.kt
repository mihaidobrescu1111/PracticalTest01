package ro.pub.cs.systems.eim.practicaltest01var08

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class PracticalTest01Var08MainActivity : AppCompatActivity() {
    val buttonClickListener = ButtonClickListener()
    var lastAnswer = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_practical_test01_var08_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val playButton = findViewById<Button>(R.id.playButton)
        playButton.setOnClickListener(buttonClickListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when (requestCode) {
            Constants.ANOTHER_ACTIVITY_REQUEST_CODE -> {
                if (resultCode == RESULT_OK) {
                    lastAnswer = 1
                    Toast.makeText(this@PracticalTest01Var08MainActivity, "Yes", Toast.LENGTH_SHORT).show()
                } else if (resultCode == RESULT_CANCELED) {
                    lastAnswer = 2
                    Toast.makeText(this@PracticalTest01Var08MainActivity, "No", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }


    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        if (lastAnswer != 0) {
            super.onSaveInstanceState(savedInstanceState)
            savedInstanceState.putInt("lastAnswer", lastAnswer)
        }
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val restoreLastAnswer = savedInstanceState.getInt("lastAnswer")
        lastAnswer = restoreLastAnswer
            Toast.makeText(
                this@PracticalTest01Var08MainActivity,
                lastAnswer.toString(),
                Toast.LENGTH_SHORT
            ).show()
            Log.d(Constants.LOG_TAG, "Last answer is $lastAnswer")
    }


    inner class ButtonClickListener: View.OnClickListener {
        override fun onClick(v: View?) {
            if (v?.id == R.id.playButton) {
                val riddle = findViewById<EditText>(R.id.riddle)
                val answer = findViewById<EditText>(R.id.answer)
                val intent =
                    Intent(this@PracticalTest01Var08MainActivity, SecondActivity::class.java)
                intent.putExtra("riddle", riddle.text.toString())
                intent.putExtra("answer", answer.text.toString())
                startActivityForResult(intent, Constants.ANOTHER_ACTIVITY_REQUEST_CODE)
            }
        }
    }
}