package com.zcf.drawgame.socket

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.util.Log
import com.zcf.drawgame.config.Config
import com.zcf.drawgame.model.Quiz
import com.zcf.drawgame.model.Score
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream


class SocketServer {
    val TAG = SocketServer::class.java.simpleName

    val serverAddr = if (Config.serverAddrCustom.isEmpty()) Config.SERVER_ADDR_DEFAULT else Config.serverAddrCustom


    private lateinit var socket: Socket
    private lateinit var client: SocketClient

    internal fun connect(client: SocketClient) {
        try {
            this.client = client

            socket = IO.socket(serverAddr)
            socket
                .on(Socket.EVENT_CONNECTING, Emitter.Listener {
                    Log.i(TAG, "connecting")
                    client.onConnecting()
                })
                .on(Socket.EVENT_CONNECT, Emitter.Listener {
                    Log.i(TAG, "connected")
                    socket.emit("chat message", "hi")
                    client.onConnected()
                })
                .on("quizStart", Emitter.Listener { args ->
                    Log.i(TAG, "quizStart")
                    val obj = args[0] as JSONObject
                    client.onQuiz(parseQuiz(obj))
                })
                .on("drawing", Emitter.Listener { args ->
                    Log.i(TAG, "drawing")

                    val obj = args[0] as JSONObject
                    val bmpString = obj["image"] as String
                    val bmp = decodeImage(bmpString)
                    client.onReceiveDrawing(bmp)
                })
                .on("leaderboard", Emitter.Listener { args ->
                    val obj = args[0] as JSONObject
                    val leaderboard = parseLeaderboard(obj)
                    client.onLeaderboard(leaderboard)

                })
                .on(Socket.EVENT_DISCONNECT, Emitter.Listener {
                    Log.i(TAG, "Disconnected")
                    client.onDisconnected()
                })
            socket.connect()

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        socket.disconnect()
    }

    fun parseQuiz(obj: JSONObject): Quiz {

        val drawer = obj["drawer"] as String
        val question = obj["question"] as String
        val optionsArray = obj["options"] as JSONArray

        val options = ArrayList<String>()
        for (i in 0 until optionsArray.length()) {
            options.add(optionsArray[i] as String)
        }
        return Quiz(drawer, question, options)
    }

    fun parseLeaderboard(obj: JSONObject): List<Score> {
        val leaderboard = ArrayList<Score>()
        for (key in obj.keys()) {
            leaderboard.add(Score(key, obj[key] as Int))
        }
        return leaderboard
    }

    fun sendPlayerName(name: String) {
        socket.emit("register", name)
    }

    fun sendImage(bmp: Bitmap) {
        val sendData = JSONObject()
        try {
            sendData.put("image", encodeImage(bmp))
            socket.emit("syncImage", sendData)
        } catch (e: JSONException) {
        }
    }

    fun submit(result: Boolean) {
        socket.emit("submit", result);
    }

    private fun encodeImage(bmp: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    private fun decodeImage(data: String): Bitmap {
        val b = Base64.decode(data, Base64.DEFAULT)
        return BitmapFactory.decodeByteArray(b, 0, b.size)
    }


}
