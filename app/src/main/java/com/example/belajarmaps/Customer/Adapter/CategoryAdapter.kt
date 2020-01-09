package com.example.belajarmaps.Customer.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.belajarmaps.Admin.Model.Category
import com.example.belajarmaps.Customer.Activity.ListByCategoryAct
import com.example.belajarmaps.R
import com.squareup.picasso.Picasso

class CategoryAdapter(val mCategory :ArrayList<Category>, val context: Context) : RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_category, parent, false))
    }

    override fun getItemCount(): Int {
        return mCategory.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val category = mCategory[position]
        holder.mName.text = category.name

        val img = category.image

        Picasso.get()
            .load(img)
            .centerCrop()
            .fit()
            .into(holder.mImage)

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ListByCategoryAct::class.java)
            intent.putExtra("nameofcategory", category.name)
            context.startActivity(intent)
        }
    }

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var mImage : ImageView = itemView.findViewById(R.id.image)
        var mName : TextView = itemView.findViewById(R.id.name)

    }

}