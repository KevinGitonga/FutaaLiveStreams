/*
 * *
 *  * Created by Kevin Gitonga on 5/5/20 1:05 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 5/5/20 12:30 PM
 *
 */

package ke.co.ipandasoft.futaasoccerlivestreams

import androidx.multidex.MultiDexApplication
import com.facebook.ads.AudienceNetworkAds
import com.orhanobut.hawk.Hawk
import ke.co.ipandasoft.futaasoccerlivestreams.di.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class FutaaSoccerLiveApp : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        initLogger()
        initKoinDi()
        initHawkDi()
        AudienceNetworkAds.initialize(this)
    }

    private fun initHawkDi() {
        Hawk.init(this).build();
    }


    //init koin modules
    private fun initKoinDi() {
        startKoin {
            androidContext(this@FutaaSoccerLiveApp)
            modules(NetModule.networkModule)
            modules(persistenceModule)
            modules(repositoryModule)
            modules(appModule)
            modules(viewModelModule)
        }
    }

    //init timber for logging
    private fun initLogger() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}