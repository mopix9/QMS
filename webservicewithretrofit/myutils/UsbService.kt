/*
package com.pankti.webservicewithretrofit.myutils

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.pankti.webservicewithretrofit.ui.MainActivity
import com.pankti.webservicewithretrofit.utils.msprintsdk.UsbDriver
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UsbDriverHandler @Inject constructor(
    private val context: Context,
    private val usbManager: UsbManager,
    private val snackbarHandler: SnackbarHandler
) {
    private var mUsbDriver: UsbDriver? = null
    private var mUsbDevice: UsbDevice? = null

    fun initializeUsbDriver() {
        mUsbDriver = UsbDriver(usbManager, context)
        val permissionIntent = PendingIntent.getBroadcast(
            context, 0, Intent(MainActivity.ACTION_USB_PERMISSION),
            PendingIntent.FLAG_IMMUTABLE
        )
        mUsbDriver!!.setPermissionIntent(permissionIntent)
        val filter = IntentFilter()
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED)
        filter.addAction(MainActivity.ACTION_USB_PERMISSION)
        context.registerReceiver(mUsbReceiver, filter)
    }

    fun checkDriver(): Boolean {
        return try {
            checkDevices()
            val iDriverCheck = usbDriverCheck()
            when {
                iDriverCheck == -1 -> {
                    snackbarHandler.showSnackbar(" پرینتر وصل نیست ")
                    false
                }
                iDriverCheck == 1 -> {
                    snackbarHandler.showSnackbar(" در حال شناسایی پرینتر ")
                    true
                }
                else -> true
            }
        } catch (r: Exception) {
            snackbarHandler.showSnackbar(" پرینتر وصل نیست ")
            false
        }
    }

    private fun usbDriverCheck(): Int {
        var iResult = -1
        try {
            if (!mUsbDriver!!.isUsbPermission) {
                val deviceList = usbManager.deviceList
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

    private fun checkDevices() {
        var strValue = ""
        var iIndex = 0
        val deviceList = usbManager.deviceList
        val deviceIterator: Iterator<UsbDevice> = deviceList.values.iterator()
        while (deviceIterator.hasNext()) {
            val device = deviceIterator.next()
            iIndex++
            strValue =
                strValue + iIndex.toString() + " DeviceClass:" + device.deviceClass + "; DeviceId:" + device.deviceId + "; " +
                        "DeviceName:" + device.deviceName + "; DeviceProtocol:" + device.deviceProtocol + "; " +
                        "ProductId:" + device.productId + "; VendorId:" + device.vendorId
        }
    }

    fun printStatus(): Int {
        return mUsbDriver!!.status
    }

    fun closeUsbDevice() {
        mUsbDriver!!.closeUsbDevice()
    }

    fun viewToBitmap(view: View): Bitmap {
        val bitmap = Bitmap.createBitmap(view.width, view.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        return bitmap
    }

    fun rotateBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(90F)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun convertToBlackWhite(src: Bitmap): Bitmap {
        val width = src.width
        val height = src.height
        val pixels = IntArray(width * height)
        src.getPixels(pixels, 0, width, 0, 0, width, height)
        for (i in pixels.indices) {
            val r = pixels[i] shr 16 and 0xFF
            val g = pixels[i] shr 8 and 0xFF
            val b = pixels[i] and 0xFF
            val gray = (0.299 * r + 0.587 * g + 0.114 * b).toInt()
            pixels[i] = -0x1000000 or (gray shl 16) or (gray shl 8) or gray
        }
        return Bitmap.createBitmap(pixels, width, height, Bitmap.Config.ARGB_8888)
    }

    fun printBitmap(bitmap: Bitmap) {
        mUsbDriver!!.printBitmap(bitmap)
    }

    private val mUsbReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (MainActivity.ACTION_USB_PERMISSION == action) {
                synchronized(this) {
                    val device = intent.getParcelableExtra<Parcelable>(UsbManager.EXTRA_DEVICE) as UsbDevice?
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        if (device != null) {
                            mUsbDriver!!.openUsbDevice(device)
                        }
                    } else {
                        snackbarHandler.showSnackbar("پرینتر به usb متصل نیست")
                    }
                }
            } else if (UsbManager.ACTION_USB_DEVICE_DETACHED == action) {
                synchronized(this) {
                    mUsbDriver!!.closeUsbDevice()
                }
            }
        }
    }
}
*/
