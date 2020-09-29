package ke.co.ipandasoft.futaasoccerlivestreams.ui.home.fragments

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.facebook.ads.Ad
import com.facebook.ads.AdError
import com.facebook.ads.InterstitialAd
import com.facebook.ads.InterstitialAdListener
import ke.co.ipandasoft.futaasoccerlivestreams.R
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Highlight
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Response
import ke.co.ipandasoft.futaasoccerlivestreams.ui.base.BaseFragment
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.HighlightsStreamActivity
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.LiveStreamActivity
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.adapters.OnGameClickListener
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.adapters.OnHighlightClickListener
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.adapters.TodayGamesAdapter
import ke.co.ipandasoft.futaasoccerlivestreams.ui.home.adapters.UpcomingGamesAdapter
import ke.co.ipandasoft.futaasoccerlivestreams.utils.AppUtils
import ke.co.ipandasoft.newsfeed.utils.NavigationUtils
import kotlinx.android.synthetic.main.upcoming_games_fragment.*
import org.koin.android.viewmodel.ext.android.viewModel
import timber.log.Timber

class UpcomingGamesFragment: BaseFragment(), SwipeRefreshLayout.OnRefreshListener,
    OnHighlightClickListener, InterstitialAdListener {
    private val gamesViewModel by viewModel<GamesViewModel>()
    private lateinit var upcomingGamesAdapter: UpcomingGamesAdapter
    private lateinit var interstitialAd: InterstitialAd

    companion object {
        fun getInstance(): UpcomingGamesFragment {
            val fragment =
                UpcomingGamesFragment()
            val bundle = Bundle()
            fragment.arguments = bundle
            return fragment
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.upcoming_games_fragment
    }

    override fun initView() {
        mRefreshLayout.setOnRefreshListener(this)
        multipleStatusView.showLoading()
        mRecyclerView.layoutManager= LinearLayoutManager(requireContext())
    }

    override fun lazyLoad() {
        interstitialAd = InterstitialAd(requireContext(), getString(R.string.main_interstitial_ad))
        interstitialAd.setAdListener(this)
        gamesViewModel.loadUpcomingGames()
        gamesViewModel.upcomingGamesData.observe(viewLifecycleOwner, Observer {
            if (it.response != null){
                if (mRefreshLayout.isRefreshing){
                    mRefreshLayout.isRefreshing=false
                }

                upcomingGamesAdapter= UpcomingGamesAdapter(requireContext(),this,it.response.highlights)
                mRecyclerView.adapter=upcomingGamesAdapter
                multipleStatusView.showContent()
            }
        })
    }

    override fun onRefresh() {
        mRefreshLayout.isRefreshing=true
        gamesViewModel.loadTodayGames()
    }

    override fun onGameClicked(highlight: Highlight, position: Int) {
        interstitialAd.loadAd()
        NavigationUtils.navigateWithHighlightBundle(this.activity as AppCompatActivity, highlight,
            0, HighlightsStreamActivity::class.java)
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