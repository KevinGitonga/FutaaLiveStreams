/*
 * *
 *  * Created by Kevin Gitonga on 5/5/20 1:06 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 5/5/20 12:20 PM
 *
 */

package ke.co.ipandasoft.futaasoccerlivestreams.ui.splash

import android.os.Handler
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.get
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import ke.co.ipandasoft.futaasoccerlivestreams.R
import ke.co.ipandasoft.futaasoccerlivestreams.constants.Constants
import ke.co.ipandasoft.futaasoccerlivestreams.ui.base.BaseActivity
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.MainActivity
import ke.co.ipandasoft.futaasoccerlivestreams.utils.AppUtils
import ke.co.ipandasoft.newsfeed.utils.NavigationUtils
import kotlinx.android.synthetic.main.activity_splash.*
import timber.log.Timber

class SplashActivity : BaseActivity() {

    override fun layoutId(): Int {
        return R.layout.activity_splash
    }

    //load data from bundles here
    override fun initData() {

    }

    private fun fetchRemoteConfigData(firebaseRemoteConfig: FirebaseRemoteConfig) {
        val cacheExpiration: Long = if (firebaseRemoteConfig.info.configSettings.isDeveloperModeEnabled) 0 else 3600

        firebaseRemoteConfig.fetchAndActivate()
            .addOnCompleteListener { task ->
                run {
                    if (task.isSuccessful) {
                        val result=task.result
                        Timber.e("DATA FETCHED $result")
                        initHomeLauch(firebaseRemoteConfig)
                    } else {
                        Timber.e("DATA FETCHED FAILED" + "Fetched")

                    }
                }
            }
    }

    private fun initHomeLauch(firebaseRemoteConfig: FirebaseRemoteConfig) {
        Handler().postDelayed({
            val serverBaseUrl=firebaseRemoteConfig[Constants.SERVER_BASE_URL].asString()
            Timber.e("SERVER_BASE_URL $serverBaseUrl")
            Hawk.put(Constants.SERVER_BASE_URL,serverBaseUrl)
            finish()
            NavigationUtils.navigate(this, MainActivity::class.java)

        }, 3000)
    }

    //bind view elements here
    override fun initView() {
        splashAppVersionText.text="V.0.0."+AppUtils.getVerCode(this).toString()

        val firebaseRemoteConfig = Firebase.remoteConfig
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }

        firebaseRemoteConfig.setConfigSettingsAsync(configSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        fetchRemoteConfigData(firebaseRemoteConfig)

    }

    //bind to business logic load data
    override fun start() {

    }


}
