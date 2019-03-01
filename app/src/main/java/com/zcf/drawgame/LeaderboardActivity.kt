package com.zcf.drawgame

import android.graphics.Typeface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.zcf.drawgame.model.Score
import kotlinx.android.synthetic.main.activity_leaderboard.*

class LeaderboardActivity : AppCompatActivity() {
    companion object {
        val EXTRA_DataList = "DataList"
    }

    private lateinit var dataList: List<Score>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dataListJson = intent.getStringExtra(EXTRA_DataList)
        dataList = Gson().fromJson(dataListJson, object: TypeToken<List<Score>>(){}.type)

        dataList = dataList.sortedByDescending { score -> score.score }

        setContentView(R.layout.activity_leaderboard)

        leaderboardList.layoutManager = LinearLayoutManager(this)
        leaderboardList.adapter = MyAdapter(dataList)

    }

    override fun onResume() {
        super.onResume()

        MediaController.play(this, "Well-done.mp3")
    }

    class MyAdapter(private val dataset: List<Score>): RecyclerView.Adapter<MyAdapter.MyViewHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.leaderboard_item, parent, false)
            return MyViewHolder(itemView)
        }

        override fun getItemCount(): Int {
            return dataset.size
        }

        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val data = dataset[position]
            holder.nameView.text = data.name
            holder.scoreView.text = data.score.toString()

            if (position == 0) {
                holder.nameView.typeface = Typeface.DEFAULT_BOLD
                holder.scoreView.typeface = Typeface.DEFAULT_BOLD
            } else {
                holder.nameView.typeface = Typeface.DEFAULT
                holder.scoreView.typeface = Typeface.DEFAULT
            }
        }

        class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var nameView: TextView
            val scoreView: TextView

            init {
                this.nameView = itemView.findViewById(R.id.nameTv)
                this.scoreView = itemView.findViewById(R.id.scoreTv)
            }
        }
    }


}
