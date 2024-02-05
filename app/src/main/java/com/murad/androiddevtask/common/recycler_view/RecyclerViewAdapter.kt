package com.murad.androiddevtask.common.recycler_view

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.viewbinding.ViewBinding
import com.murad.androiddevtask.common.Inflater


/**
 * Created on 03 February 2024, 6:09 PM
 * @author Murad Eliyev
 */


class RecyclerViewAdapter<Data : Any, ItemBinding : ViewBinding>(
    private val inflater: Inflater<ItemBinding>,
    private val onBind: ItemBinding.(Data, Int) -> Unit
) : ListAdapter<Data, GeneralViewHolder<ItemBinding>>(GenericCallBack<Data>()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GeneralViewHolder<ItemBinding> {
        return GeneralViewHolder(
            inflater(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: GeneralViewHolder<ItemBinding>, position: Int) {
        holder.bind {
            onBind(getItem(position), position)
        }
    }
}


class GeneralViewHolder<Binding : ViewBinding>(
    private val binding: Binding
) : ViewHolder(binding.root) {

    fun bind(action: Binding.() -> Unit) {
        binding.action()
    }

}


class GenericCallBack<Item : Any> : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }
}
