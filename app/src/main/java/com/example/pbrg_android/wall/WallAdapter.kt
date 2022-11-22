package com.example.pbrg_android.wall

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.pbrg_android.R

class WallAdapter(private val data: List<String>?) : BaseAdapter(),
    View.OnClickListener {

    private var context: Context? = null

    override fun getCount(): Int {
        return data?.size ?: 0
    }

    override fun getItem(i: Int): Any {
        return data!![i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View, viewGroup: ViewGroup): View {
        var view: View = view
        var viewHolder: ViewHolder? = null
        if (context == null) context = viewGroup.context
        if (view == null) {
            view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item, null)
            viewHolder = ViewHolder()
            viewHolder.mTv = view.findViewById(R.id.mTv) as TextView
            viewHolder.mBtn = view.findViewById(R.id.mBtn) as Button
            view.tag = viewHolder
        }
        // get viewHolder instance
        viewHolder = view.tag as ViewHolder
        // set data
        viewHolder!!.mTv!!.text = data!![i]
        // set tag
        viewHolder.mBtn!!.setTag(R.id.btn,i)
        // set data
        viewHolder.mBtn!!.text = "route#$i"
        // set listener
        viewHolder.mBtn!!.setOnClickListener(this)

        return view
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.mBtn -> {
                Log.d("tag", "Btn_onClick: view = $view")
                Toast.makeText(context, "button clicked", Toast.LENGTH_SHORT).show()
            }
            R.id.mTv -> {
                Log.d("tag", "Tv_onClick: view = $view")
                Toast.makeText(context, "text clicked", Toast.LENGTH_SHORT).show()
            }
        }
    }

    internal class ViewHolder {
        var mTv: TextView? = null
        var mBtn: Button? = null
    }
}