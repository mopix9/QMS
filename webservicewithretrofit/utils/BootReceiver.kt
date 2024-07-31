package com.pankti.webservicewithretrofit.utils


import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pankti.webservicewithretrofit.ui.MainActivity

class MyBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == Intent.ACTION_PACKAGE_REPLACED) {
            // اگر دستگاه روشن شد یا برنامه دیگری باز شد، برنامه‌ی ما را دوباره اجرا کنید
            val startIntent = Intent(context, MainActivity::class.java)
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context?.startActivity(startIntent)
        }
    }
}

/*import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.pankti.webservicewithretrofit.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyBroadcastReceiver : BroadcastReceiver() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == Intent.ACTION_PACKAGE_REPLACED) {
            // اگر دستگاه روشن شد یا برنامه دیگری باز شد، برنامه‌ی ما را دوباره اجرا کنید
            val startIntent = Intent(context, MainActivity::class.java)
            startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context?.startActivity(startIntent)
            startCoroutine(context)
        }
    }

    private fun startCoroutine(context: Context?) {
        coroutineScope.launch {
            while (true) {
                // کاری که می‌خواهید هر 10 ثانیه انجام شود را اینجا قرار دهید
                // به عنوان مثال، می‌توانید یک Intent برای فعال کردن MainActivity ارسال کنید
                val startIntent = Intent(context, MainActivity::class.java)
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context?.startActivity(startIntent)

                // منتظر بمانید تا 10 ثانیه بگذرد
                delay(2500)
            }
        }
    }


}*/




/*
import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.pankti.webservicewithretrofit.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyBroadcastReceiver : BroadcastReceiver() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == Intent.ACTION_PACKAGE_REPLACED) {
            // برنامه‌ی ما در حال اجرا است؟
            if (!isAppRunning(context)) {
                // برنامه در حال اجرا نیست، بنابراین متد startCoroutine را اجرا کنید
                startCoroutine(context)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun isAppRunning(context: Context?): Boolean {
        val packageName = context?.packageName
        val usageStatsManager = context?.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager

        if (usageStatsManager != null) {
            val endTime = System.currentTimeMillis()
            val beginTime = endTime - 1000 * 60 // یک دقیقه پیش

            val appList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                beginTime,
                endTime
            )

            appList?.let {
                for (usageStats in it) {
                    if (usageStats.packageName == packageName && usageStats.totalTimeInForeground > 0) {
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun startCoroutine(context: Context?) {
        coroutineScope.launch {
            while (true) {
                // کاری که می‌خواهید هر 10 ثانیه انجام شود را اینجا قرار دهید
                // به عنوان مثال، می‌توانید یک Intent برای فعال کردن MainActivity ارسال کنید
                val startIntent = Intent(context, MainActivity::class.java)
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context?.startActivity(startIntent)

                // منتظر بمانید تا 10 ثانیه بگذرد
                delay(10000)
            }
        }
    }
}*/


/*import android.app.usage.UsageStatsManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.pankti.webservicewithretrofit.ui.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MyBroadcastReceiver : BroadcastReceiver() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED || intent?.action == Intent.ACTION_PACKAGE_REPLACED) {
            // برنامه‌ی ما در حال اجرا است؟
            if (!isAppRunning(context)) {
                // برنامه در حال اجرا نیست، بنابراین متد startCoroutine را اجرا کنید
                startCoroutine(context)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP_MR1)
    private fun isAppRunning(context: Context?): Boolean {
        val packageName = context?.packageName
        val usageStatsManager = context?.getSystemService(Context.USAGE_STATS_SERVICE) as? UsageStatsManager

        if (usageStatsManager != null) {
            val endTime = System.currentTimeMillis()
            val beginTime = endTime - 1000 * 60 // یک دقیقه پیش

            val appList = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_DAILY,
                beginTime,
                endTime
            )

            appList?.let {
                for (usageStats in it) {
                    if (usageStats.packageName == packageName && usageStats.totalTimeInForeground > 0) {
                        return true
                    }
                }
            }
        }

        return false
    }

    private fun startCoroutine(context: Context?) {
        coroutineScope.launch {
            while (true) {
                // کاری که می‌خواهید هر 10 ثانیه انجام شود را اینجا قرار دهید
                // به عنوان مثال، می‌توانید یک Intent برای فعال کردن MainActivity ارسال کنید
                val startIntent = Intent(context, MainActivity::class.java)
                startIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP)
                context?.startActivity(startIntent)

                // منتظر بمانید تا 10 ثانیه بگذرد
                delay(10000)
            }
        }
    }
}*/


