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
import com.google.android.exoplayer2.upstream.HttpDataSource
import com.google.android.exoplayer2.util.Util
import com.google.gson.Gson
import ke.co.ipandasoft.futaasoccerlivestreams.R
import ke.co.ipandasoft.futaasoccerlivestreams.constants.Constants
import ke.co.ipandasoft.futaasoccerlivestreams.data.response.Response
import ke.co.ipandasoft.futaasoccerlivestreams.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_streaming.*
import kotlinx.android.synthetic.main.custom_exo_layout.*
import timber.log.Timber

class LiveStreamActivity :BaseActivity(), Player.EventListener {


    private var isFullscreen = false
    var exoPlayer: SimpleExoPlayer? = null
    private lateinit var  toolbar: Toolbar
    private lateinit var trackSelector: DefaultTrackSelector
    private var isActivityIsVisible:Boolean=true
    private var isVideoPlaying:Boolean=false
    private lateinit var response: Response
    private lateinit var localStreamLink:String

    override fun layoutId(): Int {
        return R.layout.activity_streaming
    }

    override fun initData() {
      val dataFromIntent=intent.getStringExtra(Constants.RESPONSE_DATA_PAYLOAD)
      response=Gson().fromJson(dataFromIntent,Response::class.java)
      Timber.e("DATA FROM HOME"+Gson().toJson(dataFromIntent))
      initStreamLink()
    }

    override fun initView() {
        trackSelector = DefaultTrackSelector(this)
        exoPlayer = SimpleExoPlayer.Builder(this).
        setTrackSelector(trackSelector).build()

        exoPlayer?.addListener(this)
        initToolbar()
        initPlayer()
        playStream(localStreamLink)
    }

    private fun initStreamLink() {
        localStreamLink = if (response.channel.containsExtraLinks){
            if (response.channel.clickPosition==1){
                response.channel.linkOneExtra
            }else if(response.channel.clickPosition==2){
                response.channel.linkTwoExtra
            }else{
                response.channel.stream
            }
        }else{
            response.channel.stream
        }
    }


    private fun playStream(videoStreamUrl:String){

        val dataSourceFactory = DefaultDataSourceFactory(applicationContext, Util.getUserAgent(applicationContext, "livestreams"))

        val bandwidthMeter= DefaultBandwidthMeter()

        val videoUri= Uri.parse(videoStreamUrl)
        val videoSource: HlsMediaSource = HlsMediaSource.Factory(dataSourceFactory)
            .setAllowChunklessPreparation(true)
            .createMediaSource(videoUri)

        exoPlayerView.player=exoPlayer

        Timber.e("SOCCER STREAM URI $videoStreamUrl")

        exoPlayerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIXED_HEIGHT

        exoPlayer?.prepare(videoSource)

        exoPlayer?.playWhenReady = true
    }

    override fun onPlayerError(error: ExoPlaybackException) {
        super.onPlayerError(error)
        if (isBehindLiveWindow(error)) {
            playStream(localStreamLink)
        }else if(isNetworkError(error)){
            Toast.makeText(this,"Error in link! Please Navigate back & try next Link.Thanks",Toast.LENGTH_SHORT).show()
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

    private fun isNetworkError(e:ExoPlaybackException):Boolean{
        var cause:Throwable?
        try {
            cause=e.sourceException
            while (cause!=null){
                if (cause is HttpDataSource.InvalidResponseCodeException){
                    return true
                }
                cause=cause.cause
            }
        }catch (ex:Exception){
            Timber.e("EXCEPTION"+ex.localizedMessage)
        }

        return true
    }


    private fun initToolbar() {
        toolbar=findViewById(R.id.player_toolbar)
        if (localStreamLink.endsWith(".m3u8") && localStreamLink.length>10){
            val filename=localStreamLink.substring( localStreamLink.length-7 ,localStreamLink.length )
            toolbar.title= filename
        }else{
          toolbar.title="${response.team_one.name} vs ${response.team_two.name}"
        }

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

    public override fun onDestroy() {
        exoPlayer?.release()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        if (exoPlayer != null) {
            exoPlayer?.playWhenReady = true
            playStream(localStreamLink)
        }
        else {
            exoPlayer?.playWhenReady = true
            playStream(localStreamLink)
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