package ke.co.ipandasoft.futaasoccerlivestreams.ui.home.fragments

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import com.orhanobut.hawk.Hawk
import ke.co.ipandasoft.futaasoccerlivestreams.R
import ke.co.ipandasoft.futaasoccerlivestreams.constants.Constants
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.LocationInfo
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Response
import ke.co.ipandasoft.futaasoccerlivestreams.ui.base.BaseFragment
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.LiveStreamActivity
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.adapters.OnGameClickListener
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.adapters.TodayGamesAdapter
import ke.co.ipandasoft.futaasoccerlivestreams.utils.AppUtils
import ke.co.ipandasoft.newsfeed.utils.NavigationUtils
import kotlinx.android.synthetic.main.games_layout_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber
import java.util.*


class TodayGamesFragment :BaseFragment(), SwipeRefreshLayout.OnRefreshListener,
    OnGameClickListener, InterstitialAdListener {

    private val gamesViewModel by viewModel<GamesViewModel>()
    private lateinit var todayGamesAdapter: TodayGamesAdapter
    private lateinit var interstitialAd: InterstitialAd

    companion object {
        fun getInstance(): TodayGamesFragment {
            val fragment =
                TodayGamesFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int {
     return R.layout.games_layout_fragment
    }

    override fun initView() {
        mRefreshLayout.setOnRefreshListener(this)
        multipleStatusView.showLoading()
        mRecyclerView.layoutManager=LinearLayoutManager(requireContext())

    }

    override fun lazyLoad() {
        interstitialAd = InterstitialAd(requireContext(), getString(R.string.main_interstitial_ad))
        interstitialAd.setAdListener(this)
        gamesViewModel.loadTodayGames()
        gamesViewModel.todayGamesData.observe(viewLifecycleOwner, Observer {
            if (it.response != null){
                if (mRefreshLayout.isRefreshing){
                    mRefreshLayout.isRefreshing=false
                }
                val deviceTimeZone= AppUtils.getDeviceTimeZone()
                Timber.e("DEVICE TIMEZONE $deviceTimeZone")
                todayGamesAdapter= TodayGamesAdapter(requireContext(),this,it.response)
                mRecyclerView.adapter=todayGamesAdapter
                multipleStatusView.showContent()
            }
        })
    }

    override fun onRefresh() {
        mRefreshLayout.isRefreshing=true
        gamesViewModel.loadTodayGames()
    }


    override fun onGameClicked(response: Response, position: Int) {
        when (position) {
            0 -> {
                interstitialAd.loadAd()
                response.channel.clickPosition=position
                NavigationUtils.navigateWithBundle(this.activity as AppCompatActivity, response,
                    0,LiveStreamActivity::class.java)
            }
            1 -> {
                interstitialAd.loadAd()
                response.channel.clickPosition=position
                NavigationUtils.navigateWithBundle(this.activity as AppCompatActivity, response,
                    1,LiveStreamActivity::class.java)
            }
            2 -> {
                response.channel.clickPosition=position
                interstitialAd.loadAd()
                NavigationUtils.navigateWithBundle(this.activity as AppCompatActivity, response,
                    2,LiveStreamActivity::class.java)
            }
            3->{
                interstitialAd.loadAd()

                var locationData: LocationInfo?=null
                if (Hawk.contains(Constants.LOCATION_DATA_RESP_LOCAL)){
                    locationData= Hawk.get(Constants.LOCATION_DATA_RESP_LOCAL)
                }
                if(response.is_live==0 && locationData!!.timezone!!.contains("Africa")){
                    Toast.makeText(requireContext(),"This Game is not Available Now..You will receive notification when Game Starts...",Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onInterstitialDisplayed(p0: Ad?) {
        Timber.e("AD DISPLAYED")
    }

    override fun onAdClicked(p0: Ad?) {
        Timber.e("AD CLICKED")
    }

    override fun onInterstitialDismissed(p0: Ad?) {
        Timber.e("AD DISMISSED")
    }

    override fun onError(p0: Ad?, p1: AdError?) {
        Timber.e("AD LOAD ERROR"+ p1?.errorMessage)
    }

    override fun onAdLoaded(p0: Ad?) {
        Timber.e("AD LOADED")
        interstitialAd.show()
    }

    override fun onLoggingImpression(p0: Ad?) {

    }

}