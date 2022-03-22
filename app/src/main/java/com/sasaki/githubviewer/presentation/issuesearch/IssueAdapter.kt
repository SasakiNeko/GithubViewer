package com.sasaki.githubviewer.presentation.issuesearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sasaki.githubviewer.databinding.ItemIssueBinding
import com.sasaki.githubviewer.domain.entity.IssueSearchResults

class IssueAdapter(
    private val context: Context,
    private val issues: MutableList<IssueSearchResults>,
    private val listener: OnClickItemListener
) : RecyclerView.Adapter<IssueAdapter.ViewHolder>() {

    interface OnClickItemListener {
        fun notifyClickAdapterItem(issueNumber: Int)
    }

    lateinit var binding: ItemIssueBinding

    fun addIssues(issues: List<IssueSearchResults>) {
        this.issues.addAll(issues)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = ItemIssueBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = issues.getOrNull(position) ?: return
        val unwrapTitle = data.title ?: return
        holder.setItemName(unwrapTitle)
        val number = data.number ?: return

        holder.binding.issueItem.setOnClickListener {
            listener.notifyClickAdapterItem(number)
        }
    }

    override fun getItemCount(): Int {
        return issues.size
    }

    class ViewHolder(val view: View, val binding: ItemIssueBinding) :
        RecyclerView.ViewHolder(view) {
        fun setItemName(name: String) {
            binding.itemName.text = name
        }
    }
}