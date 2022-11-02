package com.nikita.demoapplication.app.framework.base

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.nikita.demoapplication.R

/**
 * Created by nikita
 *
 * use layoutDatabinding needed to use this adapter pass in layout in constructor and set generic values
 * and  to set data in adapter just set use setList function
 * */
open class BaseRecyclerViewAdapter<T, U>(private val layoutResource: Int) : RecyclerView.Adapter<BaseViewHolder<T>>() {
    private var list= mutableListOf<U>()
    protected var viewHolderListener: ViewHolderListener<T, U>? = null


    fun setList(list: MutableList<U>, viewHolderListener: ViewHolderListener<T, U>) {
        this.list = list
        this.viewHolderListener = viewHolderListener
        notifyDataSetChanged()
    }

    fun setViewHolder(viewHolderListener: ViewHolderListener<T, U>) {
        this.viewHolderListener = viewHolderListener
    }

    open fun setList(list: MutableList<U>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder<T> {
        val view = DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), layoutResource, parent, false)
        return BaseViewHolder(view)
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the [BaseViewHolder.itemView] to reflect the item at the given
     * position.
     *
     *
     * Note that unlike [ListView], RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the `position` parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use [BaseViewHolder.getAdapterPosition] which will
     * have the updated adapter position.
     *
     *
     * Override [.onBindViewHolder] instead if Adapter can
     * handle efficient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     * item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    override fun onBindViewHolder(holder: BaseViewHolder<T>, position: Int) {
        val item = list[position]
        holder.itemView.setTag(R.string.position, position)
        viewHolderListener?.onBindViewHolder(holder, item)
    }

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    override fun getItemCount(): Int {
        return list.size
    }

    interface ViewHolderListener<T, U> {
        fun onBindViewHolder(holder: BaseViewHolder<T>, item: U)
    }
}