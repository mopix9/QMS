/*
package com.pankti.webservicewithretrofit.myutils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.view.View

class PrintService(private val context: Context, private val usbService: UsbService) {
    fun checkPrinter() {
        if (usbService.isConnected() && usbService.isUsbPermission()) {
            val statusCommand = byteArrayOf(0x10, 0x04, 0x14)
            val buffer = ByteArray(1)
            val result = usbService.read(buffer, statusCommand)
            if (result == 0) {
                showPrinterStatus(buffer[0])
            }
        }
    }

    private fun showPrinterStatus(status: Byte) {
        val message = when (status) {
            18.toByte() -> " پرینتر آماده است "
            20.toByte() -> " پرینتر آماده نیست "
            21.toByte() -> " کاغذ موجود نیست "
            22.toByte() -> " ریبون موجود نیست "
            23.toByte() -> " کاغذ و ریبون موجود نیست "
            else -> " وضعیت نامعلوم "
        }
//        Snackbar.make((context as MainActivity).binding.root, message, Snackbar.LENGTH_LONG).show()
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

    private fun createBitmapFromView(view: View): Bitmap? {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED)
        val width = view.measuredWidth
        val height = view.measuredHeight
        if (width > 0 && height > 0) {
            val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
            val canvas = Canvas(bitmap)
            view.layout(0, 0, width, height)
            view.draw(canvas)
            return bitmap
        }
        return null
    }

    fun printStatus(): Int {
        return if (usbService.isConnected() && usbService.isUsbPermission()) {
            val statusCommand = byteArrayOf(0x10, 0x04, 0x14)
            val buffer = ByteArray(1)
            usbService.read(buffer, statusCommand)
        } else {
            -1
        }
    }
}
*/
