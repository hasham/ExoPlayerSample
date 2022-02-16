package com.hasht.exoplayerexample

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.hasht.exoplayerexample.databinding.ActivityFullscreenBinding


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class FullscreenActivity : AppCompatActivity(), Player.Listener {

    private lateinit var simpleExoplayer: ExoPlayer
    private var playbackPosition: Long = 0
    private val mp4Url = "https://html5demos.com/assets/dizzy.mp4"
    private val dashUrl = "https://storage.googleapis.com/wvmedia/clear/vp9/tears/tears_uhd.mpd"
    private val u1 =
        "https://d2e9b6n0dz3n76.cloudfront.net/28_ded2e512-a20c-499d-ba9e-ddc23101b00c_keykidev_productId_28/master.m3u8"
    private val u2 =
        "https://d2e9b6n0dz3n76.cloudfront.net/75_fff00e22-101f-4511-8513-2230bb485f83_keykilive_productId_75/master.m3u8"
    private val u3 =
        "https://d2e9b6n0dz3n76.cloudfront.net/27_203cc48e-eb43-4d74-bceb-ad41e116c574_keykidev_productId_27/master.m3u8"
    private val u4 =
        "https://d2e9b6n0dz3n76.cloudfront.net/25_52a84c92-3084-4417-8cf7-8f93b97bca06_keykidev_productId_25/master.m3u8"

//        private val urlList = listOf(mp4Url to "default", dashUrl to "dash")
    private val urlList = listOf(u1 to "u1", u2 to "u2", u3 to "u3", u4 to "u4")

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityFullscreenBinding.inflate(layoutInflater)
    }

    private val dataSourceFactory: DataSource.Factory by lazy {
//        DefaultDataSourceFactory(this, "exoplayer-sample")

            DefaultDataSourceFactory(this, "exoplayer-example")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)
    }

    override fun onStart() {
        super.onStart()
        initializePlayer()
    }

    override fun onStop() {
        super.onStop()
        releasePlayer()
    }

    private fun initializePlayer() {
        simpleExoplayer = SimpleExoPlayer.Builder(this).build()
        val randomUrl = urlList.random()
        preparePlayer(randomUrl.first, randomUrl.second)
        viewBinding.exoplayerView.player = simpleExoplayer
        simpleExoplayer.seekTo(playbackPosition)
        simpleExoplayer.playWhenReady = true
        simpleExoplayer.addListener(this)
    }

    private fun buildMediaSource(uri: Uri, type: String): MediaSource {
//        val dataSourceFactory: DataSource.Factory =
//            DefaultDataSourceFactory(this, "exoplayer-example")
        return HlsMediaSource.Factory(dataSourceFactory).createMediaSource(uri)
//        return if (type == "dash") {
//            DashMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(uri)
//        } else {
//            ProgressiveMediaSource.Factory(dataSourceFactory)
//                .createMediaSource(uri)
//        }
    }

    private fun preparePlayer(videoUrl: String, type: String) {
        val uri = Uri.parse(videoUrl)
        val mediaSource = buildMediaSource(uri, type)
        simpleExoplayer.prepare(mediaSource)
    }

    private fun releasePlayer() {
        playbackPosition = simpleExoplayer.currentPosition
        simpleExoplayer.release()
    }

//    override fun onPlayerError(error: ExoPlaybackException) {
//        // handle error
//    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
        if (playbackState == Player.STATE_BUFFERING)
            viewBinding.progressBar.visibility = View.VISIBLE
        else if (playbackState == Player.STATE_READY || playbackState == Player.STATE_ENDED)
            viewBinding.progressBar.visibility = View.INVISIBLE
    }
}