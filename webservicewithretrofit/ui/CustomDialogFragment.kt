package com.pankti.webservicewithretrofit.ui

import android.app.Dialog
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.pankti.webservicewithretrofit.R

class CustomDialogFragment : DialogFragment() {

    private val delayMillis: Long = 10000 // 10 ثانیه


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.fragment_get_app, null)

        // ایجاد دیالوگ با سفارشی‌سازی‌های لازم
        val dialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()


        // شروع تایمر
        val handler = Handler()
        handler.postDelayed({
            dialog.dismiss()
        }, delayMillis)


        val closeButton = view.findViewById<Button>(R.id.actionButton)
       closeButton.setOnClickListener {
           dialog.dismiss()
       }

        return dialog
    }


}