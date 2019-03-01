package com.zcf.drawgame.socket

import android.graphics.Bitmap
import com.zcf.drawgame.model.Quiz
import com.zcf.drawgame.model.Score


interface SocketClient {
    fun onConnecting()
    fun onConnected()
    fun onMatchedPlayer(partner: String)
    fun onQuiz(quiz: Quiz)
    fun onReceiveDrawing(bmp: Bitmap)
    fun onLeaderboard(leadboard: List<Score>)
    fun onDisconnected()
}