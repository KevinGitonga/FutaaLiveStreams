/*
 * *
 *  * Created by Kevin Gitonga on 5/5/20 1:11 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 4/23/20 8:11 PM
 *
 */

package ke.co.ipandasoft.newsfeed.utils

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import ke.co.ipandasoft.futaasoccerlivestreams.constants.Constants
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Highlight
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Response

//Navigation helper class
class NavigationUtils {

    companion object {

        fun navigate(activity: AppCompatActivity, javaClass: Class<out Activity>) {
            val navigationIntent = Intent(activity, javaClass)
            activity.startActivity(navigationIntent)
        }


        fun navigateWithBundle(
            activity: AppCompatActivity,
            response: Response,
            position: Int,
            javaClass: Class<out Activity>
        ) {
            val navigationIntent = Intent(activity, javaClass)
            navigationIntent.putExtra(Constants.RESPONSE_DATA_PAYLOAD, Gson().toJson(response))
            activity.startActivity(navigationIntent)
        }

        fun navigateWithHighlightBundle(
            activity: AppCompatActivity,
            highlight: Highlight,
            position: Int,
            javaClass: Class<out Activity>
        ) {
            val navigationIntent = Intent(activity, javaClass)
            navigationIntent.putExtra(Constants.HIGHLIGHT_DATA_PAYLOAD, Gson().toJson(highlight))
            activity.startActivity(navigationIntent)
        }


    }

}