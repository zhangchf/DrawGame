package com.zcf.drawgame

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.TextView
import com.google.gson.Gson
import com.zcf.drawgame.model.Quiz
import com.zcf.drawgame.model.Score
import com.zcf.drawgame.socket.SocketClient
import com.zcf.drawgame.socket.SocketServer
import kotlinx.android.synthetic.main.activity_entry.*
import kotlinx.android.synthetic.main.activity_game.*

class GameActivity : AppCompatActivity(), SocketClient {

    companion object {
        val EXTRA_NAME = "name";
    }

    private val socketServer = SocketServer()
    private lateinit var playerName: String

    private var currentQuiz: Quiz? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        playerName = intent.getStringExtra(EXTRA_NAME)

        setContentView(R.layout.activity_game)

        sendButton.setOnClickListener {
            var drawing = inkView.getBitmap(resources.getColor(android.R.color.white))
            socketServer.sendImage(drawing);
        }

        clearButton.setOnClickListener {
            inkView.clear()
        }

        option1.setOnClickListener(this::onOptionClicked)
        option2.setOnClickListener(this::onOptionClicked)
        option3.setOnClickListener(this::onOptionClicked)

        socketServer.connect(this)
    }


    override fun onDestroy() {
        super.onDestroy()
        socketServer.disconnect();
    }

    fun onOptionClicked(view: View) {


        val tv = view as TextView
        var result: Boolean
        if (tv.text.equals(currentQuiz?.question)) {
            tv.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
            MediaController.play(this, "Vic-Yes.mp3")
            result = true
        } else {
            tv.setBackgroundColor(resources.getColor(android.R.color.holo_red_light))
            MediaController.play(this, "no-no.mp3")
            result = false
        }
        view.postDelayed(
            Runnable {
                socketServer.submit(result)
            }, 2000)
    }

    override fun onReceiveDrawing(bmp: Bitmap) {
        runOnUiThread {
            drawingImage.setImageBitmap(bmp)
        }
    }

    override fun onConnecting() {
        runOnUiThread {
            msgTextView.text = "Connecting to server ..."
        }
    }

    override fun onConnected() {
        runOnUiThread {
            msgTextView.text = "Connected. Matching ..."
        }
        socketServer.sendPlayerName(playerName)
    }

    override fun onMatchedPlayer(partner: String) {
        runOnUiThread {
            msgTextView.text = "Matched a player: ${partner}"
        }
    }

    override fun onQuiz(quiz: Quiz) {
        this.currentQuiz = quiz
        runOnUiThread {
            resetView()
            if (quiz.drawer.equals(playerName)) {
                drawerLayout.visibility = View.VISIBLE
                drawingImage.visibility = View.INVISIBLE
                optionsLayout.visibility = View.GONE

                msgTextView.text = "Please draw a ${quiz.question}"
            } else {
                drawerLayout.visibility = View.INVISIBLE
                drawingImage.visibility = View.VISIBLE
                optionsLayout.visibility = View.VISIBLE

                msgTextView.text = "Please guess what it is"
                option1.text = quiz.options[0]
                option2.text = quiz.options[1]
                option3.text = quiz.options[2]
            }
        }
    }


    override fun onLeaderboard(leadboard: List<Score>) {
        val intent = Intent(this, LeaderboardActivity::class.java)
        intent.putExtra(LeaderboardActivity.EXTRA_DataList, Gson().toJson(leadboard))
        startActivity(intent)
    }

    override fun onDisconnected() {
        runOnUiThread {
            msgTextView.text = "Disconnected"
        }
    }

    fun resetView() {
        inkView.clear()
        drawingImage.setImageResource(android.R.color.transparent)
        option1.setBackgroundColor(resources.getColor(R.color.orange))
        option2.setBackgroundColor(resources.getColor(R.color.orange))
        option3.setBackgroundColor(resources.getColor(R.color.orange))

    }
}
