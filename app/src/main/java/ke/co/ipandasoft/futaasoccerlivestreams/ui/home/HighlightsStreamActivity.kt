package ke.co.ipandasoft.futaasoccerlivestreams.ui.home

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.View
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.BehindLiveWindowException
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import ke.co.ipandasoft.futaasoccerlivestreams.R
import ke.co.ipandasoft.futaasoccerlivestreams.constants.Constants
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Highlight
import ke.co.ipandasoft.futaasoccerlivestreams.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_streaming.*
import kotlinx.android.synthetic.main.custom_exo_layout.*
import timber.log.Timber

class HighlightsStreamActivity :BaseActivity(), Player.EventListener {


    private var isFullscreen = false
    var exoPlayer: SimpleExoPlayer? = null
    private lateinit var  toolbar: Toolbar
    private lateinit var trackSelector: DefaultTrackSelector
    private var isActivityIsVisible:Boolean=true
    private var isVideoPlaying:Boolean=false
    private lateinit var highlight: Highlight

    override fun layoutId(): Int {
        return R.layout.activity_streaming
    }

    override fun initData() {
      val dataFromIntent=intent.getStringExtra(Constants.HIGHLIGHT_DATA_PAYLOAD)
      highlight=Gson().fromJson(dataFromIntent,Highlight::class.java)
      Timber.e("DATA FROM HOME"+Gson().toJson(dataFromIntent))
    }

    override fun initView() {
        trackSelector = DefaultTrackSelector(this)
        exoPlayer = SimpleExoPlayer.Builder(this).
        setTrackSelector(trackSelector).build()

        exoPlayer?.addListener(this)
        initToolbar()

    }

    private fun playStream(){

        val dataSourceFactory = DefaultDataSourceFactory(applicationContext, Util.getUserAgent(applicationContext, "livestreams"))

        val bandwidthMeter= DefaultBandwidthMeter()

        val videoUri= Uri.parse(highlight.highlights_url.toString())
        val videoSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)
            .createMediaSource(videoUri)

        exoPlayerView.player=exoPlayer


        Timber.e("MOVIE URI $videoUri")

        exoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT

        exoPlayer?.prepare(videoSource)
        exoPlayer?.playWhenReady = true
    }


    private fun initToolbar() {
        toolbar=findViewById(R.id.player_toolbar)
        toolbar.title= "${highlight.team_one.name} vs ${highlight.team_two.name}"
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }


    private fun initPlayer() {
        img_full_scr.setOnClickListener {
            if (isFullscreen) {
                renderNormalView()
            } else {
                renderFullScreen()
            }
        }
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun renderFullScreen() {
        player_toolbar.visibility=View.INVISIBLE
        img_full_scr.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_close))
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_USER_LANDSCAPE

        val params = playerFrameLayout.layoutParams
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT
        params.height = RelativeLayout.LayoutParams.MATCH_PARENT
        playerFrameLayout.layoutParams = params
        exoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        isFullscreen = true
    }

    @SuppressLint("SourceLockedOrientationActivity")
    private fun renderNormalView() {
        player_toolbar.visibility=View.VISIBLE
        img_full_scr.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_fullscreen_open))
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE


        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val params = playerFrameLayout.layoutParams
        params.width = RelativeLayout.LayoutParams.MATCH_PARENT
        params.height = (300 * applicationContext.resources.displayMetrics.density).toInt()
        playerFrameLayout.layoutParams = params
        exoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT
        isFullscreen = false
    }


    override fun start() {
        initPlayer()
        playStream()
    }

    public override fun onPause() {
        super.onPause()
        if ( exoPlayer != null) {
            exoPlayer?.playWhenReady = false
        }
        exoPlayer?.playWhenReady = false
        exoPlayer?.playbackState
    }

    override fun onIsPlayingChanged(isPlaying: Boolean) {
        super.onIsPlayingChanged(isPlaying)
        if (isPlaying){
            isVideoPlaying=true
            actionBar?.hide()
        }else{
            actionBar?.show()
        }

        Timber.e("VIDEO PLAYING $isVideoPlaying")
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        if (isBehindLiveWindow(error)) {
            exoPlayer?.release()
            playStream()
        }else if(error.equals(error.sourceException)){
            Toast.makeText(this,"Error in link!Please wait for links to be updated.Thanks", Toast.LENGTH_SHORT).show()
        }
    }

    private fun isBehindLiveWindow(e: ExoPlaybackException): Boolean {
        if (e.type != ExoPlaybackException.TYPE_SOURCE) {
            return false
        }
        var cause: Throwable? = e.sourceException
        while (cause != null) {
            if (cause is BehindLiveWindowException) {
                return true
            }
            cause = cause.cause
        }
        return false
    }

    public override fun onDestroy() {
        exoPlayer?.release()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        if (exoPlayer != null) {
            exoPlayer?.playWhenReady = true
        }
        else {
            exoPlayer?.playWhenReady = true
        }
    }


    override fun onBackPressed() {
        if (isFullscreen){
            renderNormalView()
        }else{
            super.onBackPressed()
        }

    }

}