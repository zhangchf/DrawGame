package com.zcf.drawgame

import android.content.Context
import android.media.MediaPlayer
import java.lang.reflect.Array.getLength
import android.content.res.AssetFileDescriptor



class MediaController {

    companion object {
        val mediaPlayer = MediaPlayer()

        fun play(context: Context, filename: String) {
            mediaPlayer.reset();
            try {
                val afd = context.getAssets().openFd(filename)
                mediaPlayer.setDataSource(afd.getFileDescriptor(), afd.getStartOffset(), afd.getLength())
                afd.close()
                mediaPlayer.prepare()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            mediaPlayer.start()
        }
    }
}