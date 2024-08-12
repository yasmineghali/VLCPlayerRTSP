package com.example.myapplication

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.ViewTreeObserver
import android.widget.Button
import android.widget.ImageButton
import android.widget.VideoView
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout

class MainActivity : AppCompatActivity() {
    private lateinit var libVLC: LibVLC
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var  videoView: SurfaceView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val btnMute = findViewById<ImageButton>(R.id.btnMute)
        val btnUnmute = findViewById<ImageButton>(R.id.btnUnmute)
        val btnPlay = findViewById<ImageButton>(R.id.btnPlay)
        val btnPause = findViewById<ImageButton>(R.id.btnPause)

        libVLC = LibVLC(this)
        // Get RTSP URL from strings.xml
        var url ="rtsp://admin:acoba12345@192.168.1.204:554/Streaming/Channels/101";
        supportActionBar?.hide()

        // Set up media options and create Media object
        val media = Media(libVLC, Uri.parse(url))
        media.addOption("--aout=opensles")
        media.addOption("--audio-time-stretch")
        media.addOption("-vvv") // verbosity

        // Create MediaPlayer and set media
        mediaPlayer = MediaPlayer(libVLC)
        mediaPlayer.media = media

        videoView = findViewById(R.id.videoView)

        val viewTreeObserver = videoView.viewTreeObserver

        btnMute.setOnClickListener {
            mediaPlayer.volume = 0
        }
        btnUnmute.setOnClickListener {
            mediaPlayer.volume = 100
        }
   btnPause.setOnClickListener {
       mediaPlayer.pause()
       mediaPlayer.volume = 0
   }
     btnPlay.setOnClickListener {
         mediaPlayer.play()
         mediaPlayer.volume = 100}
    }


    override fun onPause() {
        mediaPlayer.stop()
        super.onPause()
    }

    override fun onDestroy() {
        mediaPlayer.stop()
        mediaPlayer.release()
        libVLC.release()
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        // Reset the SurfaceView and MediaPlayer
        videoView.holder.addCallback(object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder) {
                mediaPlayer.vlcVout.setVideoSurface(holder.surface, holder)
                mediaPlayer.vlcVout.setWindowSize(videoView.width, videoView.height)
                mediaPlayer.vlcVout.attachViews()
                mediaPlayer.play()
            }

            override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                // Handle surface size changes if needed
            }

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                // Handle surface destruction if needed
            }
        })
    }

}