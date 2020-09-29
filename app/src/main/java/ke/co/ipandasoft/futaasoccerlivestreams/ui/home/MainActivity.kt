package ke.co.ipandasoft.futaasoccerlivestreams.ui.home

import android.provider.SyncStateContract
import android.view.View
import android.widget.FrameLayout
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import com.google.gson.Gson
import com.mikhaellopez.ratebottomsheet.RateBottomSheet
import com.mikhaellopez.ratebottomsheet.RateBottomSheetManager
import com.orhanobut.hawk.Hawk
import ke.co.ipandasoft.futaasoccerlivestreams.R
import ke.co.ipandasoft.futaasoccerlivestreams.constants.Constants
import ke.co.ipandasoft.futaasoccerlivestreams.data.repository.LocationRepository
import ke.co.ipandasoft.futaasoccerlivestreams.di.LocationNetModule
import ke.co.ipandasoft.futaasoccerlivestreams.ui.base.BaseActivity
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.fragments.TodayGamesFragment
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.fragments.UpcomingGamesFragment
import ke.co.ipandasoft.futaasoccerlivestreams.ui.settings.SettingsFragment
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber


class MainActivity:BaseActivity() {

    private var todayGamesFragment:TodayGamesFragment? = null
    private var upcomingGamesFragment:UpcomingGamesFragment? =null
    private var settingsFragment:SettingsFragment? =null
    private lateinit var viewModel :MainViewModel
    private lateinit var locationRepository: LocationRepository

    override fun layoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        locationRepository= LocationRepository(LocationNetModule.service)
        viewModel=MainVmFactory(locationRepository).create(MainViewModel::class.java)
        viewModel.getLocationInfo()
        viewModel.locationLiveData.observe(this, Observer {
            if (it!=null){
                Timber.e("LOCATION DATA ${Gson().toJson(it)}")
                val locationInfo=it
                Hawk.put(Constants.LOCATION_DATA_RESP_LOCAL,locationInfo)
            }
        })

    }

    override fun initView() {
     switchFragment(0)
     initBottomNav()
     initAppRater()
    }

    private fun initAppRater() {
        RateBottomSheetManager(this)
            //.setDebugForceOpenEnable(true)
            .setInstallDays(1)
            .setLaunchTimes(2)
            .setRemindInterval(2)
            .setShowAskBottomSheet(false)
            .setShowLaterButton(true)
            .setShowCloseButtonIcon(false)
            .monitor()

        RateBottomSheet.showRateBottomSheetIfMeetsConditions(this)
    }

    override fun start() {

    }

    private fun initBottomNav() {
        bottomNavBar.onItemSelected = {
            when (it) {
                0 -> {
                    Timber.e("BUTTON SELECTED $it")
                    switchFragment(0)
                }
                1 -> {
                    Timber.e("BUTTON SELECTED $it")
                    switchFragment(1)
                }
                2 -> {
                    Timber.e("BUTTON SELECTED $it")
                    switchFragment(2)
                }
            }
        }

    }


    private fun switchFragment(position: Int) {
        val transaction = supportFragmentManager.beginTransaction()
        hideFragments(transaction)
        when (position) {
            0 -> todayGamesFragment?.let {
                transaction.show(it)
            } ?: TodayGamesFragment.getInstance().let {
                todayGamesFragment = it
                transaction.add(R.id.mainContainer, it, "home")
            }
            1 -> upcomingGamesFragment?.let {
                transaction.show(it)
            }?: UpcomingGamesFragment.getInstance().let {
                upcomingGamesFragment=it
                transaction.add(R.id.mainContainer,it,"upcoming games")
            }
            2 -> settingsFragment?.let {
                transaction.show(it)
            }?: SettingsFragment.getInstance().let {
                settingsFragment=it
                transaction.add(R.id.mainContainer,it,"setting fragment")
            }

            else -> {

            }
        }

        transaction.commitAllowingStateLoss()
    }

    private fun hideFragments(transaction: FragmentTransaction) {
        todayGamesFragment?.let { transaction.hide(it) }
        upcomingGamesFragment?.let { transaction.hide(it) }
        settingsFragment?.let { transaction.hide(it) }
    }
}