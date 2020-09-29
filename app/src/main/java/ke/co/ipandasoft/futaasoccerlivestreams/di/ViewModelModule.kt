/*
 * *
 *  * Created by Kevin Gitonga on 5/5/20 1:03 PM
 *  * Copyright (c) 2020 . All rights reserved.
 *  * Last modified 5/5/20 12:27 PM
 *
 */

package ke.co.ipandasoft.futaasoccerlivestreams.di

import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.MainViewModel
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.fragments.GamesViewModel
import ke.co.ipandasoft.futaasoccerlivestreams.ui.splash.SplashViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module


val viewModelModule = module {
    viewModel { SplashViewModel(get()) }
    viewModel { GamesViewModel(get()) }
}