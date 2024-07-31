/*
package com.pankti.webservicewithretrofit.myutils

import com.pankti.webservicewithretrofit.databinding.ActivityMainBinding
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.Timer
import java.util.TimerTask
import javax.inject.Inject

class TimeService @Inject constructor(
    private val binding: ActivityMainBinding
) {    fun startClock() {
        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())

        Timer().schedule(object : TimerTask() {
            override fun run() {
                val now = Date()
                binding.txDate.text = dateFormat.format(now)
                binding.txTime.text = timeFormat.format(now)
            }
        }, 0, 1000)
    }
}
*/
