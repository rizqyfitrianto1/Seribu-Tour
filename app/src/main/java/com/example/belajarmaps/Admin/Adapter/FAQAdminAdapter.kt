package com.example.belajarmaps.Admin.Adapter

import android.content.Context
import android.content.Intent
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
import com.example.belajarmaps.Admin.Activity.FAQAdminEditActivity
import com.example.belajarmaps.Admin.Model.FAQ
import com.example.belajarmaps.R
import com.google.firebase.database.FirebaseDatabase
import es.dmoral.toasty.Toasty

class FAQAdminAdapter(val mFAQ :List<FAQ>, val context: Context) : RecyclerView.Adapter<FAQAdminAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_faq_admin, parent, false))
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

        holder.ll_edit.setOnClickListener {
            val intent = Intent(context, FAQAdminEditActivity::class.java)
            intent.putExtra("id", faq.id)
            intent.putExtra("answer", faq.answer)
            intent.putExtra("question", faq.question)
            context.startActivity(intent)
        }

        holder.ll_delete.setOnClickListener {
            val reference = FirebaseDatabase.getInstance().getReference("FAQ").child(faq.id)
            reference.removeValue().addOnCompleteListener {
                Toasty.success(context, "Data has been deleted", Toasty.LENGTH_SHORT, true).show()
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
        var ll_edit = itemView.findViewById<LinearLayout>(R.id.ll_edit)
        var ll_delete = itemView.findViewById<LinearLayout>(R.id.ll_delete)

    }

}