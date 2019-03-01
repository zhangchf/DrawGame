package com.zcf.drawgame

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
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
    }
}
