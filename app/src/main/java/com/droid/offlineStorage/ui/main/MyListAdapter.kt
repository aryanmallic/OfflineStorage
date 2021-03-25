package com.droid.offlineStorage.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.droid.offlineStorage.R
import com.droid.offlineStorage.databinding.ListItemBinding
import com.droid.offlineStorage.model.ExResponse
import com.droid.offlineStorage.utils.Constants

/**
 * Created by Akhtar
 */
class MyListAdapter(
    private var mList: MutableList<ExResponse>,
    private val clickListener: (ExResponse, String) -> Unit
) :
    RecyclerView.Adapter<MyListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val v = LayoutInflater.from(parent.context)
        val binding: ListItemBinding = DataBindingUtil.inflate(v, R.layout.list_item, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    fun updateList(mList: MutableList<ExResponse>) {
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    inner class MyViewHolder(val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.listItemBtEdit.setOnClickListener {
                clickListener(mList[adapterPosition], Constants.CLICK_EDIT)
            }

            binding.listItemTvMap.setOnClickListener {
                clickListener(mList[adapterPosition], Constants.CLICK_MAP)
            }

            binding.listItemBtDelete.setOnClickListener {
                clickListener(mList[adapterPosition], Constants.CLICK_DELETE)
            }
        }

        fun bind(exResponse: ExResponse) {
            binding.listItemTvId.text = exResponse.id.toString()
            binding.listItemTvUserId.text = exResponse.userId.toString()
            binding.listItemTvTitle.text = exResponse.title
            binding.listItemTvDescription.text = exResponse.body
            binding.listItemTvCreatedOn.text = exResponse.createdOn
            binding.listItemTvUpdatedOn.text = exResponse.updateOn
            binding.listItemTvLat.text = exResponse.lat.toString()
            binding.listItemTvLng.text = exResponse.lng.toString()

            if (exResponse.lat == 0.0 || exResponse.lng == 0.0) {
                binding.listItemTvMap.visibility = View.GONE
            } else {
                binding.listItemTvMap.visibility = View.VISIBLE
            }
        }
    }
}