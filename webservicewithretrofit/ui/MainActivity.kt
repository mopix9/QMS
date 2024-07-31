package com.pankti.webservicewithretrofit.ui

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Typeface
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Message
import android.os.Parcelable
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.animation.AnimationUtils
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.annotation.FontRes
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.pankti.webservicewithretrofit.R
import com.pankti.webservicewithretrofit.databinding.ActivityMainBinding
import com.pankti.webservicewithretrofit.domain.entities.GetNum
import com.pankti.webservicewithretrofit.domain.entities.NetworkDataViewModel
import com.pankti.webservicewithretrofit.utils.AryanTime
import com.pankti.webservicewithretrofit.utils.msprintsdk.PrintCmd
import com.pankti.webservicewithretrofit.utils.msprintsdk.PrintCmd.PrintDiskImagefile
import com.pankti.webservicewithretrofit.utils.msprintsdk.UsbDriver
import com.pankti.webservicewithretrofit.utils.msprintsdk.UtilsTools.convertToBlackWhite
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val _viewModel: NetworkDataViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private var _postList = arrayListOf<GetNum>()
    private var mediaPlayer: MediaPlayer? = null
    private var isOperationInProgress = false
    private var mUsbDriver: UsbDriver? = null
    private var mUsbDevice: UsbDevice? = null
    private lateinit var backPressedCallback: OnBackPressedCallback
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    companion object {
        const val ACTION_USB_PERMISSION = "com.usb.sample.USB_PERMISSION"
    }

    @RequiresApi(Build.VERSION_CODES.R)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setClickListeners()
        initializeUsbDriver()
        startUpdatingTime()
        playGiph()
        hideSystemUI()
        playSound()
        driver()
        hideSystemUI()


        // Handle back button press (already explained)
        backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
        showSnackbar("عدم امکان خروج از برنامه")
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        editor = sharedPreferences.edit()

//        فراخوانی Reciver
 /*       val serviceIntent = Intent(this, QueReciver::class.java)
        startService(serviceIntent)*/
    }


    override fun onResume() {
        super.onResume()
     /*   val isLaunchedFromBroadcast = intent.getBooleanExtra("launchedFromBroadcast", false)
        if (isLaunchedFromBroadcast) {
            // برنامه از روی یک Broadcast Receiver راه‌اندازی شده است، بنابراین انجام عملیات مورد نیاز
            // مثلاً نمایش یک پیام به کاربر یا اجرای یک عملیات خاص
        }*/
        driver()


        // به محض بازگشت به اکتیویتی، recent فعال می‌شود
        editor.putBoolean("isRecentDisabled", false)
        editor.apply()

//        PrintStatus()

//    showSnackbar("خوش آمدید")


    }




/*
    @RequiresApi(Build.VERSION_CODES.R)
    override fun onPause() {
        super.onPause()
//        isActivityPaused = true

        // اگر کاربر از برنامه خارج شد و بازنگشت به اکتیویتی دیگر انجام نشد، اقدامات مناسب را انجام دهید
        // اینجا می‌توانید به اکتیویتی دیگر بروید یا اقدامات مناسب را انجام دهید
        if (isRecentButtonClicked) {
            val intent = Intent(this, GetAppLink::class.java)
            startActivity(intent)
        }
    }
*/


    override fun onDestroy() {
        super.onDestroy()
        _viewModel.stopRefreshing()
        mediaPlayer?.release()
        mUsbDriver?.closeUsbDevice()
    }

    override fun onStop() {
        super.onStop()
        // Close the app completely when the user presses the Recent Apps button
        onUserLeaveHint()
    }

    private fun startUpdatingTime() {
        CoroutineScope(Dispatchers.Main).launch {
            while (true) {
                binding.txtTime.text = AryanTime.getReceiptTime()
                binding.txtDate.text = AryanTime.getPersianDate()
                delay(1000)
            }
        }
    }

    private fun playGiph() {
        val videoUri = Uri.parse("android.resource://" + packageName + "/" + R.raw.company)
        binding.website.setVideoURI(videoUri)
        binding.website.setOnPreparedListener { mediaPlayer ->
            mediaPlayer.isLooping = true
            mediaPlayer.start()
        }
    }

    private fun playSound() {
        _viewModel.soundData.observe(this) { sound ->
            sound?.sound?.let { url ->
                if (url.isNotBlank()) {
                    val partOfInterest = extractPartOfInterest(url)
                    if (partOfInterest != _viewModel.previousPartOfInterest) {
                        _viewModel.previousPartOfInterest = partOfInterest
                        mediaPlayer?.release()
                        mediaPlayer = MediaPlayer().apply {
                            setDataSource(url)
                            prepareAsync()
                            setOnPreparedListener { mp ->
                                mp.start()
                            }
                            setOnErrorListener { mp, _, _ ->
                                mp.release()
                                false
                            }
                        }
                    }
                } else {
                   /* Toast.makeText(
                        this@MainActivity,
                        "",
                        Toast.LENGTH_SHORT
                    ).show()*/
                }
            }
        }
        _viewModel.startRefreshing()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun setClickListeners() {



             binding.btnFetchPosts.setOnClickListener {
                  if (isOperationInProgress) {
                      showSnackbar(" لطفا بلافاصله دکمه دریافت نوبت را فشار ندهید ")
                      return@setOnClickListener
                  }

                  isOperationInProgress = true
                  if (!_viewModel.isNetworkConnected(this)) {
                      showSnackbar("                لطفا ارتباط شبکه را بررسی بفرمایید                 ")
                      isOperationInProgress = false
                      return@setOnClickListener
                  }



                  val printStatus = PrintStatus()
                  if (printStatus == 0) {
                  CoroutineScope(Dispatchers.Main).launch {
                      try {
      //                    driver()
                          //*     // ابتدا متد driver() را فراخوانی کرده و وضعیت پرینتر را بررسی می‌کنیم
                       /*  if (!driver() ) {
                             isOperationInProgress = false
                             return@launch // اگر پرینتر خاموش بود، از ادامه اجرای کدها جلوگیری می‌کنیم
                         }*/

                    // بقیه کارها انجام می‌شونداگر روشن بود
                    val animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.button_animation)
                    binding.btnFetchPosts.startAnimation(animation)
                    observeData()
                    delay(1000)
                    val bitmap = viewToBitmap(binding.rvUserData)
                    val rotatedBitmap = rotateBitmap(bitmap)
                    val bwBitmap = rotatedBitmap.let { convertToBlackWhite(it) }

                    bwBitmap?.let {
                        val width = it.width
                        val height = it.height
                        val pixels = IntArray(width * height)
                        it.getPixels(pixels, 0, width, 0, 0, width, height)
                        mUsbDriver?.apply {
                            write(PrintDiskImagefile(pixels, width, height))
                            write(PrintCmd.PrintFeedline(4))
                            write(PrintCmd.PrintCutpaper(1))
                        }
                    }
                } catch (e: Exception) {
                    showSnackbar("+  مشکل در دیتا  " + e.message)
                } finally {
                    isOperationInProgress = false
                }
            }

        }else     isOperationInProgress = false
        }

        binding.btnapp.setOnClickListener {

//            val isRecentDisabled = sharedPreferences.getBoolean("isRecentDisabled", false)

            // تغییر وضعیت
            editor.putBoolean("isRecentDisabled", true)
            editor.apply()

            // شروع اکتیویتی جدید
            startActivity(Intent(this, GetAppLink::class.java))


//            برای زمانی است که بجای Dialog اکتیویتی صدا زده شود
//         startActivity(Intent(this, GetAppLink::class.java))



//            جهت استفاده از دیالوگ
        /*    val dialog = CustomDialogFragment()
            dialog.show(supportFragmentManager, "image_dialog")
*/

        }
    }


    private fun observeData() {
        _viewModel.getPosts()
        _viewModel.postList.observe(this) {
            _postList.clear()
            _postList.addAll(it)
            if (_postList.isNotEmpty()) {
//                findViewById<RecyclerView>(R.id.rvUserData).visibility = View.VISIBLE
                setPostAdapter()
            } else {
                findViewById<RecyclerView>(R.id.rvUserData).visibility = View.GONE
//                findViewById<ImageView>(R.id.ivNoDataFound2).visibility = View.VISIBLE
            }
        }

        _viewModel.isLoading.observe(this) { _isLoading ->
            findViewById<ProgressBar>(R.id.progressbar2).visibility = if (_isLoading) View.VISIBLE else View.GONE
        }
    }

    private fun setPostAdapter() {
        val adapter = PostListAdapter(_postList)
        findViewById<RecyclerView>(R.id.rvUserData).adapter = adapter
    }

    private fun initializeUsbDriver() {
        mUsbDriver = UsbDriver(getSystemService(USB_SERVICE) as UsbManager, this)
        val permissionIntent = PendingIntent.getBroadcast(
            this, 0, Intent(ACTION_USB_PERMISSION),
            PendingIntent.FLAG_IMMUTABLE
        )
        mUsbDriver!!.setPermissionIntent(permissionIntent)
        val filter = IntentFilter()
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction(ACTION_USB_PERMISSION)
        this.registerReceiver(mUsbReceiver, filter)
    }

//    check for found change voice api
    private fun extractPartOfInterest(url: String): String {
        return url.substringAfterLast('/').substringBeforeLast('.')
    }

    private val mUsbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {
                val parcelableDevice = intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE)
                val device = parcelableDevice as? UsbDevice
                if (device!!.productId == 8211 && device.vendorId == 1305 || device.productId == 8213 && device.vendorId == 1305) {
                    mUsbDriver!!.closeUsbDevice(device)
                }
            } else if (ACTION_USB_PERMISSION == action) synchronized(this) {
//                val device = intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice
                val parcelableDevice = intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE)
                val device = parcelableDevice as? UsbDevice
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    if (device!!.productId == 8211 && device.vendorId == 1305 || device.productId == 8213 && device.vendorId == 1305) {
                        // Do something after permission granted
                    }
                } else {
                }
            }
        }
    }

    private fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    private fun rotateBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(180f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

private fun driver(): Boolean {
    return try {
        CheckDevices()
        val iDriverCheck = usbDriverCheck()
        when {
            iDriverCheck == -1 -> {
                showSnackbar(" پرینتر وصل نیست ")
                false // برگرداندن مقدار false به عنوان نشان دهنده خاموش بودن پرینتر
            }
            iDriverCheck == 1 -> {
                showSnackbar(" در حال شناسایی پرینتر ")
                true // برگرداندن مقدار true به عنوان نشان دهنده روشن بودن پرینتر
            }
            else -> true // اگر مقدار دیگری برگردانده شود، به عنوان نشان دهنده روشن بودن پرینتر در نظر گرفته می‌شود
        }
    } catch (r: Exception) {
        showSnackbar(" پرینتر وصل نیست ")
        false // برگرداندن مقدار false به عنوان نشان دهنده خاموش بودن پرینتر
    }
}

    private fun usbDriverCheck(): Int {
        var iResult = -1
        try {
            if (!mUsbDriver!!.isUsbPermission) {
                val manager = getSystemService(USB_SERVICE) as UsbManager
                val deviceList = manager.deviceList
                mUsbDevice = null
                val deviceIterator: Iterator<UsbDevice> = deviceList.values.iterator()
                while (deviceIterator.hasNext()) {
                    val device = deviceIterator.next()
                    if (device.productId == 8211 && device.vendorId == 1305 || device.productId == 8213 && device.vendorId == 1305) {
                        mUsbDevice = device
                    }
                }
                if (mUsbDevice != null) {
                    iResult = 1
                    if (mUsbDriver!!.usbAttached(mUsbDevice)) {
                        if (mUsbDriver!!.openUsbDevice(mUsbDevice)) iResult = 0
                    }
                }
            } else {
                if (!mUsbDriver!!.isConnected) {
                    if (mUsbDriver!!.openUsbDevice(mUsbDevice)) iResult = 0
                } else {
                    iResult = 0
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return iResult
    }

    private fun CheckDevices() {
        var strValue = ""
        var iIndex = 0
        val manager = getSystemService(USB_SERVICE) as UsbManager
        val deviceList = manager.deviceList
        val deviceIterator: Iterator<UsbDevice> = deviceList.values.iterator()
        while (deviceIterator.hasNext()) {
            val device = deviceIterator.next()
            iIndex++
            strValue =
                strValue + iIndex.toString() + " DeviceClass:" + device.deviceClass + "; DeviceId:" + device.deviceId + "; " +
                        "DeviceName:" + device.deviceName + "; DeviceProtocol:" + device.deviceProtocol + ";" +
                        " DeviceSubclass:" + device.deviceSubclass + "; InterfaceCount:" + device.interfaceCount + ";" +
                        " ProductId:" + device.productId + "; VendorId:" + device.vendorId + "; \r\n"
        }
    }


//    چک کردن وضعیت کاغذ

    private fun PrintStatus(): Int {
//        driver()
        var iResult = 1
        try {
            var iValue = -1
            val bRead1 = ByteArray(1)
            var strValue: String? = ""

            if (mUsbDriver!!.read(bRead1, PrintCmd.GetStatus1()) > 0) {
                iValue = PrintCmd.CheckStatus1(bRead1[0])
                if (iValue != 0) {
                    strValue = PrintCmd.getStatusDescriptionEn(iValue)
                    showSnackbar(strValue)
                }

            }
            if (iValue == 0) {
                iValue = -1
                if (mUsbDriver!!.read(bRead1, PrintCmd.GetStatus2()) > 0) {
                    iValue = PrintCmd.CheckStatus2(bRead1[0])
                    if (iValue != 0) {
                        strValue = PrintCmd.getStatusDescriptionEn(iValue)
                        showSnackbar(strValue)

                    }
                }
            }
            if (iValue == 0) {
                iValue = -1
                if (mUsbDriver!!.read(bRead1, PrintCmd.GetStatus3()) > 0) {
                    iValue = PrintCmd.CheckStatus3(bRead1[0])
                    if (iValue != 0) {
                        strValue = PrintCmd.getStatusDescriptionEn(iValue)
                        showSnackbar(strValue)

                    }
                }
            }
            if (iValue == 0) {
                iValue = -1
                if (mUsbDriver!!.read(bRead1, PrintCmd.GetStatus4()) > 0) {
                    iValue = PrintCmd.CheckStatus4(bRead1[0])
                    if (iValue != 0) {
                        strValue = PrintCmd.getStatusDescriptionEn(iValue)
                        showSnackbar(strValue)

                    }
                }
            }
            if (iValue == 0) {
                strValue = PrintCmd.getStatusDescriptionEn(iValue)
//                showSnackbar(strValue)

            }
            iResult = iValue
        } catch (e: Exception) {
            val message = Message.obtain()
            message.what = 4
            message.obj = e.message
//            handler.sendMessage(message)
            showSnackbar(e.message!!)

            Log.e("printerStatus", "PrintStatus:" + e.message)
        }
        return iResult
    }

    //    برای فراخوانی برنامه در صورت کلیک روی دکمه های پیمایشی اندروید
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()

        // بررسی وضعیت ذخیره شده
        val isRecentDisabled = sharedPreferences.getBoolean("isRecentDisabled", false)

        if (!isRecentDisabled) {
            // بستن فعالیت‌های دیگر و بازگشت سریع به MainActivity
//            finishAffinity()
            // ایجاد یک Intent برای شروع MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)
        }
    }

/*
//    برای فراخوانی برنامه در صورت کلیک روی دکمه های پیمایشی اندروید
    override fun onUserLeaveHint() {
        super.onUserLeaveHint()
    // Create an Intent to start the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            startActivity(intent)

    }*/

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

/*// فراخوانی مجدد برنامه در صورت استفاده از دکمه home
    @RequiresApi(Build.VERSION_CODES.M)
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


    @RequiresApi(api = Build.VERSION_CODES.R)
    private fun hideSystemUI() {
        val controller = window.insetsController
        if (controller != null) {
            controller.systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            controller.hide(WindowInsets.Type.statusBars() or WindowInsets.Type.navigationBars())
        }
    }

    private fun showSnackbar(message: String) {
        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            message,
            Snackbar.LENGTH_LONG
        )
        val view = snackbar.view
        val params = view.layoutParams as FrameLayout.LayoutParams
        params.gravity = Gravity.TOP or Gravity.CENTER
        view.layoutParams = params
        val textView: TextView = view.findViewById(com.google.android.material.R.id.snackbar_text)
        textView.apply {
            textSize = 24f
            typeface = getFont(R.font.iransansbold)
            textAlignment = View.TEXT_ALIGNMENT_CENTER // تنظیم تراز متن به وسط
        }
        snackbar.setBackgroundTint(ContextCompat.getColor(this, R.color.dark_sky_blue)) // تنظیم رنگ پس‌زمینه
        snackbar.show()
    }


    private fun getFont(@FontRes fontRes: Int): Typeface? {
        return ResourcesCompat.getFont(this, fontRes)
    }

}
