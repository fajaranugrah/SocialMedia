package com.android.socialmedia.ListAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.ImageView
import android.widget.TextView
import com.android.socialmedia.Model.search.Search
import com.android.socialmedia.PicassoKotlin.CircleTransform
import com.android.socialmedia.R
import com.squareup.picasso.Picasso
import java.util.ArrayList

class ListSearchAdapter(internal var context: Context, textViewResourceId: Int, internal var items: List<Search.Post>) : ArrayAdapter<Search.Post>(context, textViewResourceId, items) {
    internal var OriginalBreed: MutableList<Search.Post>
    internal var sections: Array<String>? = null
    internal var filter: BreedFilter? = null

    init {
        OriginalBreed = ArrayList<Search.Post>()
        OriginalBreed.addAll(items)
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView

        try {
            val holder: ViewHolder
            if (convertView == null) {
                holder = ViewHolder()
                convertView = LayoutInflater.from(context).inflate(R.layout.item_search_selection, parent, false)
                holder.nameUser = convertView!!.findViewById(R.id.text_search_user_name) as TextView
                holder.icon_search_user = convertView!!.findViewById(R.id.icon_search_user) as ImageView

                convertView.tag = holder
            } else {
                holder = convertView.tag as ViewHolder
            }

            holder.nameUser!!.setText(items[position].userName)

            val circle : CircleTransform = CircleTransform()
            Picasso.get()
                .load(items[position].userAvatar)
                .centerCrop()
                .fit()
                .transform(circle)
                .into(holder.icon_search_user!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return convertView!!
    }

    override fun getFilter(): Filter {
        if (filter == null) {
            filter = BreedFilter()
        }
        return filter as BreedFilter
    }

    private class ViewHolder {
        internal var nameUser: TextView? = null
        internal var icon_search_user: ImageView? = null
    }

    fun getSections(): Array<String>? {
        return sections // to string will be called to display the letter
    }

    inner class BreedFilter : Filter() {
        override fun performFiltering(constraint: CharSequence): Filter.FilterResults {

            val result = Filter.FilterResults()
            val filterText = constraint.toString().toLowerCase()

            if (filterText == null || filterText.length == 0) {
                val list = ArrayList<Search.Post>(OriginalBreed)
                result.values = list
                result.count = list.size
            } else {
                val filteredItems = ArrayList<Search.Post>()
                val unfilteredItems = ArrayList<Search.Post>()
                unfilteredItems.addAll(OriginalBreed)

                for (i in unfilteredItems.indices) {
                    val breed = unfilteredItems[i]
                    if (breed.userName.toLowerCase().contains(filterText)) {
                        filteredItems.add(breed)
                    }
                }
                result.count = filteredItems.size
                result.values = filteredItems
            }
            return result
        }

        override fun publishResults(constraint: CharSequence, results: Filter.FilterResults) {

            items = results.values as List<Search.Post>
            notifyDataSetChanged()
            clear()
            val count = items.size
            for (i in 0 until count) {
                add(items[i])
                notifyDataSetInvalidated()
            }
        }
    }

}