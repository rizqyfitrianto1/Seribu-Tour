package com.example.belajarmaps.Customer.Adapter

import android.content.Context
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Admin.Model.FAQ
import com.example.belajarmaps.R

class FAQAdapter(val mFAQ :List<FAQ>, val context: Context) : RecyclerView.Adapter<FAQAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_faq, parent, false))
    }

    override fun getItemCount(): Int {
        return mFAQ.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val faq = mFAQ[position]
        holder.question.text = faq.question
        holder.answer.text = faq.answer

        holder.ll_first.setOnClickListener{
            if (holder.expandableView.visibility == View.GONE) {
                TransitionManager.beginDelayedTransition(holder.cardView, AutoTransition())
                holder.expandableView.visibility = View.VISIBLE
                holder.arrowBtn.setBackgroundResource(R.drawable.ic_arrow_down_blue)
            } else {
                TransitionManager.beginDelayedTransition(holder.cardView, AutoTransition())
                holder.expandableView.visibility = View.GONE
                holder.arrowBtn.setBackgroundResource(R.drawable.ic_arrow_right_blue)
            }
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var question : TextView = itemView.findViewById(R.id.tv_question)
        var answer : TextView = itemView.findViewById(R.id.tv_answer)
        var expandableView = itemView.findViewById<LinearLayout>(R.id.expandableView)
        var ll_first = itemView.findViewById<LinearLayout>(R.id.ll_first)
        var arrowBtn = itemView.findViewById<ImageView>(R.id.arrowBtn)
        var cardView = itemView.findViewById<CardView>(R.id.cardView)

    }

}