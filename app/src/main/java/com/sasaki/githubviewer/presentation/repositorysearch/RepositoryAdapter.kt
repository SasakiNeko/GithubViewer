package com.sasaki.githubviewer.presentation.repositorysearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sasaki.githubviewer.databinding.ItemRepositoryBinding
import com.sasaki.githubviewer.domain.entity.RepositoryInfo

class RepositoryAdapter(
    private val context: Context,
    var repositoryInfo: MutableList<RepositoryInfo>,
    private val listener: OnClickItemListener,
) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    interface OnClickItemListener {
        fun notifyClickAdapterItem(repositoryInfo: RepositoryInfo)
    }

    lateinit var binding: ItemRepositoryBinding

    /**
     * リポジトリデータを追加する
     *
     * @param repositoryInfo
     */
    fun addRepositoryInfo(repositoryInfo: List<RepositoryInfo>?) {
        val unwrapRepositoryInfo = repositoryInfo ?: return
        this.repositoryInfo.addAll(unwrapRepositoryInfo)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = ItemRepositoryBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = repositoryInfo[position]
        val nameWithOwner = data.nameWithOwner ?: return
        holder.setItemName(nameWithOwner)
        holder.binding.repositoryItem.setOnClickListener {
            listener.notifyClickAdapterItem(repositoryInfo[position])
        }
    }

    override fun getItemCount(): Int = repositoryInfo.size

    class ViewHolder(
        val view: View,
        val binding: ItemRepositoryBinding,
    ) : RecyclerView.ViewHolder(view) {

        fun setItemName(name: String) {
            binding.itemName.text = name
        }
    }
}