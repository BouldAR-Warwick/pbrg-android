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
import com.example.pbrg_android.data.model.RouteListItem

class WallAdapter(private val data: List<RouteListItem>?) : BaseAdapter(),
    View.OnClickListener {

    private var context: Context? = null

    override fun getCount(): Int {
        return data?.size ?: 0
    }

    override fun getItem(i: Int): RouteListItem {
        return data!![i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    override fun getView(i: Int, view: View?, viewGroup: ViewGroup): View? {
        var view: View? = view
        var viewHolder: ViewHolder? = null
        if (context == null) context = viewGroup.context
        if (view == null) {
            view = LayoutInflater.from(viewGroup.context).inflate(R.layout.list_item, null)
            viewHolder = ViewHolder()
            viewHolder.routeItemName = view.findViewById(R.id.routeItemName) as TextView
            viewHolder.routeItemDifficulty = view.findViewById(R.id.routeItemDifficulty) as TextView
            viewHolder.routeID = view.findViewById(R.id.routeID) as TextView
            view.tag = viewHolder
        }
        // get viewHolder instance
        viewHolder = view!!.tag as ViewHolder
        // set data
        viewHolder!!.routeItemName!!.text = data!![i].routeName
        viewHolder!!.routeItemDifficulty!!.text = "V${data!![i].difficulty.toString()}"
        viewHolder!!.routeID!!.text = data!![i].routeID.toString()

        return view
    }

    override fun onClick(view: View) {

    }

    internal class ViewHolder {
        var routeItemName: TextView? = null
        var routeItemDifficulty: TextView? = null
        var routeID: TextView? = null
    }
}