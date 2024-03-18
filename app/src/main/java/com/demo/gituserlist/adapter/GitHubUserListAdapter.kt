package com.demo.gituserlist.adapter

import com.demo.gituserlist.R
import com.demo.gituserlist.databinding.ItemUserListBinding
import com.demo.gituserlist.model.GetUserListItem

class GitHubUserListAdapter : BaseRecyclerViewAdapter<GetUserListItem, ItemUserListBinding>() {

    override fun getLayout() = R.layout.item_user_list

    override fun onBindViewHolder(
        holder: Companion.BaseViewHolder<ItemUserListBinding>,
        position: Int
    ) {
        holder.binding.getUserListItem = items[position]
        holder.binding.favImgId.setOnClickListener {
            listener?.invoke(items[position])
        }
    }

    fun filterList(filterlist: ArrayList<GetUserListItem>) {
        addItems(filterlist)
    }
}