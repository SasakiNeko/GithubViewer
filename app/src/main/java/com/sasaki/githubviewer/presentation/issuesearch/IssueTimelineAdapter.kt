package com.sasaki.githubviewer.presentation.issuesearch

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.sasaki.githubviewer.databinding.ItemTimelineBinding
import com.sasaki.githubviewer.domain.entity.TimeLineItems

class IssueTimelineAdapter(
    private val context: Context,
    private val issueTimeline: MutableList<TimeLineItems>
) : RecyclerView.Adapter<IssueTimelineAdapter.ViewHolder>() {

    lateinit var binding: ItemTimelineBinding

    fun addIssueTimeline(issueTimeline: List<TimeLineItems>) {
        this.issueTimeline.addAll(issueTimeline)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemTimelineBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding.root, binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = issueTimeline.getOrNull(position) ?: return
        val name = data.name ?: return
        val htmlComment = data.issueComment ?: return
        holder.setUserName(name)
        holder.setTimelineComment(htmlComment)
    }

    override fun getItemCount(): Int = issueTimeline.size

    class ViewHolder(val view: View, val binding: ItemTimelineBinding) :
        RecyclerView.ViewHolder(view) {

        fun setUserName(name: String) {
            binding.name.text = name
        }

        fun setTimelineComment(comment: String) {
            binding.timelineComment.text = HtmlCompat.fromHtml(comment, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    }
}