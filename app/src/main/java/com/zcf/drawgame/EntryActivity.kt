package com.zcf.drawgame

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import com.zcf.drawgame.config.Config
import kotlinx.android.synthetic.main.activity_entry.*

class EntryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entry)

        startButton.setOnClickListener {
            val name = nameInputEditText.text.toString()

            val intent = Intent(this, GameActivity::class.java)
            intent.putExtra(GameActivity.EXTRA_NAME, name)
            startActivity(intent)
        }

        layoutRoot.setOnClickListener {
            val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(layoutRoot.getWindowToken(), 0);
        }

        changeServerBtn.setOnClickListener {
            showChangeServerDialog()
        }
    }

    fun showChangeServerDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.dialog_change_server)

//        val dialogView = layoutInflater.inflate(R.layout.dialog_change_server, null)
        val serverAddrInput = dialog.findViewById<EditText>(R.id.serverAddrInput)
        val cancelTv = dialog.findViewById<TextView>(R.id.cancelTv)
        val okTv = dialog.findViewById<TextView>(R.id.okTv)

        cancelTv.setOnClickListener {
            val serverAddr = serverAddrInput.text.toString()
            if (serverAddr.isNotEmpty()) {
                Config.serverAddrCustom = serverAddr
            }
            dialog.dismiss()
        }
        okTv.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
