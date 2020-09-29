/*
 * *
 *  * Created by Kevin Gitonga on 5/5/20 1:12 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 3/24/20 7:18 PM
 *
 */

package ke.co.ipandasoft.futaasoccerlivestreams.utils

import android.content.Context
import android.content.pm.PackageManager
import android.text.TextUtils
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

//utils to provide app functionalities

class AppUtils private constructor() {


    init {
        throw Error("Do not need instantiate!")
    }

    companion object {

        private val DEBUG = true
        private val TAG = "AppUtils"
        private const val TIME_FORMAT_WITH_TIMEZONE = "yyyy-MM-dd'T'HH:mm:ssZ"


        fun convertToLocalTime(serverTime: String):String{
            val calender=Calendar.getInstance()
            val localTimeZone=calender.timeZone
            val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            simpleDateFormat.timeZone=TimeZone.getTimeZone("Asia/Karachi")
            val serverDate=simpleDateFormat.parse(serverTime)

            val simpleDateFormat2=SimpleDateFormat("d MMMM yyyy")
            simpleDateFormat2.timeZone=localTimeZone
            return simpleDateFormat2.format(serverDate)
        }

        fun getDeviceTimeZone():String{
            return TimeZone.getDefault().displayName
        }


        fun convertToLocalTimeVenue(serverTime: String):String{
            val calender=Calendar.getInstance()
            val localTimeZone=calender.timeZone
            val simpleDateFormat=SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            simpleDateFormat.timeZone=TimeZone.getTimeZone("Asia/Karachi")
            val serverDate=simpleDateFormat.parse(serverTime)

            val simpleDateFormat2=SimpleDateFormat("HH:mm, d MMMM")
            simpleDateFormat2.timeZone=localTimeZone
            return simpleDateFormat2.format(serverDate)
        }


        fun getVerCode(context: Context): Int {
            var verCode = -1
            try {
                val packageName = context.packageName
                verCode = context.packageManager
                    .getPackageInfo(packageName, 0).versionCode
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return verCode
        }


        fun getVerName(context: Context): String {
            var verName = ""
            try {
                val packageName = context.packageName
                verName = context.packageManager
                    .getPackageInfo(packageName, 0).versionName
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }

            return verName
        }


        fun getDateIso(str: String): Date {
            if (TextUtils.isEmpty(str)) {
                return Date()
            }
            try {
                return SimpleDateFormat(TIME_FORMAT_WITH_TIMEZONE, Locale.ENGLISH).parse(str)
            } catch (e: ParseException) {
                e.printStackTrace()
                return Date()
            }
        }
    }


}