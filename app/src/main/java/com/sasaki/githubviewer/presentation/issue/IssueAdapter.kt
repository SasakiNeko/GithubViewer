package com.sasaki.githubviewer.presentation.issue

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.sasaki.githubviewer.databinding.ItemIssueBinding

class IssueAdapter(private val context: Context) : RecyclerView.Adapter<IssueAdapter.ViewHolder>() {

    lateinit var binding: ItemIssueBinding

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        binding = ItemIssueBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return 10
    }

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

    }
}