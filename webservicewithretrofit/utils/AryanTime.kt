package com.pankti.webservicewithretrofit.utils

import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import saman.zamani.persiandate.PersianDate
import saman.zamani.persiandate.PersianDateFormat
import java.sql.Time
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


object AryanTime {

    var getTime : MutableLiveData<String> = MutableLiveData(getDateString())
    var getOclock : MutableLiveData<String> = MutableLiveData(getReceiptTime())

    fun updateTime(){ getTime.value = getDateString() }
    fun updatesaat(){ getOclock.value = getCurrentDate() }

     private fun getDateString(): String {
        val date = PersianDate()
        val formatter = PersianDateFormat("j F Y")
        return formatter.format(date)
    }

    fun getCurrentDate():String{
        val sdf = SimpleDateFormat("HH:mm")
        return sdf.format(Date())
    }


    fun getTime(): String {
        val d = Date()
        val sdf = SimpleDateFormat("HHmmss", Locale.US)
        return sdf.format(d)
    }

    fun getReceiptTime(): String {
        val d = Date()
        val sdf = SimpleDateFormat("HH:mm", Locale.US)
        return sdf.format(d)
    }


    fun getDate(): String {
        val d = Date()
        val sdf = SimpleDateFormat("MMdd", Locale.US)
        return sdf.format(d)
    }


/*    fun getPersianDate(): String {
        val pdate = PersianDate()
        val pdformater1 = PersianDateFormat("Y/m/d")
        return pdformater1.format(pdate) //1396/05/20
    }*/

    fun getPersianDate(): String {
        val pdate = PersianDate()
        val pdformater1 = PersianDateFormat("j F Y")
        return pdformater1.format(pdate) //1396/05/20
    }


    fun getTimeFromString(input : String) : String {
        return try {
            val inp = SimpleDateFormat("HHmmss", Locale.US)
            val output = SimpleDateFormat("HH:mm:ss", Locale.US)
            output.format(inp.parse(input))
        } catch (e: Exception) {
            ""
        }
    }


    fun getTimeForReceipt(input : String) : String{
        return try {
            val inp = SimpleDateFormat("HHmmss", Locale.US)
            val output = SimpleDateFormat("HH:mm:ss", Locale.US)
            output.format(inp.parse(input))
        }catch (e:Exception){
            ""
        }
    }


}