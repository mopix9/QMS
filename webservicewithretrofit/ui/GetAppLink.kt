package com.pankti.webservicewithretrofit.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.pankti.webservicewithretrofit.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GetAppLink : AppCompatActivity() {

    private var countdownJob: Job? = null


    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_app_link)

        val countdownDurationMillis = 10000L // Example: 10 seconds
        startCountdownTimer(countdownDurationMillis)
        hideSystemUI()



       findViewById<Button>(R.id.actionButton)
           .setOnClickListener {
finish()
        /*    val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)*/
        }
    }

    // full screen show
    @RequiresApi(Build.VERSION_CODES.R)
    private fun hideSystemUI() {
        window.setDecorFitsSystemWindows(false)
        val controller = window.insetsController
        controller?.let {
            // Hide the status bar and navigation bar
            it.hide(WindowInsets.Type.systemBars())
            // Set the behavior of the system bars to show/hide when swiping
            it.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun startCountdownTimer(countdownDurationMillis: Long) {
        countdownJob = CoroutineScope(Dispatchers.Main).launch {
            var remainingMillis = countdownDurationMillis
            while (remainingMillis > 0) {
                // Update UI (e.g., display remaining time)
                val secondsRemaining = remainingMillis / 1000
                // Update your TextView or other UI elements here

                delay(1000) // Delay for 1 second

                remainingMillis -= 1000
            }
            // Countdown finished, navigate back to the previous activity
                finish()
        }
    }

    private fun stopCountdownTimer() {
        countdownJob?.cancel()
    }

    //    کد عدم امکان استفاده از recent
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (!hasFocus) {
            hideSystemUI()
            // If the window loses focus, it might be due to recent apps button press
//            showSnackbar("مسولیت فشردن این دکمه با خودتان می باشد")
        }
    }

    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
//        isRecentButtonClicked = false

        // اگر اکتیویتی دوم صدا زده شده بود، این متد را نادیده بگیر
        if (javaClass.simpleName != "GetAppLink::class.java") {
            // Create an Intent to start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)

        }
//            javaClass.simpleName != "GetAppLink::class.java"

    }


/*    @RequiresApi(Build.VERSION_CODES.M)
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (intent != null) {
            // اگر برنامه به حالت تعلیق در زمان قبلی بوده است، آن را به حالت فعال برگردانید
            if (intent.flags and Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY == 0) {
                // بستن برنامه‌های دیگری که در حال حاضر اجرا هستند
                val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                val runningApps = activityManager.runningAppProcesses
                for (runningApp in runningApps) {
                    if (runningApp.processName != packageName) {
                        activityManager.killBackgroundProcesses(runningApp.processName)
                    }
                }

                // برنامه از حالت تعلیق بیرون آمده است، به حالت فعال برگردانید
                val appTasks = activityManager.appTasks
                for (appTask in appTasks) {
                    if (appTask.taskInfo.baseActivity?.packageName == packageName) {
                        appTask.moveToFront()
                        break
                    }
                }
            }
        }
    }*/


    override fun onDestroy() {
        super.onDestroy()
        stopCountdownTimer()
    }
}