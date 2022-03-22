package com.sasaki.githubviewer.presentation.issuesearch

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sasaki.githubviewer.GithubViewerApplication
import com.sasaki.githubviewer.databinding.FragmentIssueDetailBinding
import com.sasaki.githubviewer.domain.entity.TimeLineItems
import com.sasaki.githubviewer.presentation.scroll.RecyclerViewScroller

class IssueDetailFragment : Fragment(), IssueDataListener {

    lateinit var binding: FragmentIssueDetailBinding
    private var scroller: RecyclerViewScroller? = null
    private var ownerName: String = ""
    private var repositoryName: String = ""
    private var issueNumber: Int? = null

    companion object {
        const val OWNER_NAME = "OWNER_NAME"
        const val REPOSITORY_NAME = "REPOSITORY_NAME"
        const val ISSUE_NUMBER = "ISSUE_NUMBER"

        @JvmStatic
        fun newInstance() = IssueDetailFragment()
    }

    private val viewModel: IssueDetailViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val issueSearchFragment = this@IssueDetailFragment
                val context = issueSearchFragment.context

                val container = if (context == null) null
                else GithubViewerApplication().appContainer

                return IssueDetailViewModel(container, this@IssueDetailFragment) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            ownerName = it.getString(OWNER_NAME) ?: ""
            repositoryName = it.getString(REPOSITORY_NAME) ?: ""
            issueNumber = it.getInt(ISSUE_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIssueDetailBinding.inflate(inflater, container, false)
        setupIssueTimelineAdapter()
        issueNumber?.let { viewModel.searchIssueTimeline(ownerName, repositoryName, it) }
        return binding.root
    }

    private fun setupIssueTimelineAdapter() {
        val unwrapContext = context ?: return
        val linearLayoutManager = LinearLayoutManager(unwrapContext)
        val scroller = object : RecyclerViewScroller(linearLayoutManager) {
            override fun reachBottomOfList() {
                viewModel.searchAdditionalIssueTimeline()
            }
        }
        this.scroller = scroller
        binding.recyclerview.apply {
            adapter = IssueTimelineAdapter(
                unwrapContext,
                viewModel.timelineItems
            )
            layoutManager = linearLayoutManager
            addOnScrollListener(scroller)
            addItemDecoration(DividerItemDecoration(unwrapContext, DividerItemDecoration.VERTICAL))
        }
    }

    override fun notifyIssueTitle(title: String) {
        binding.issueTitle.text = title
    }

    override fun notifyIssueDescription(description: String) {
        binding.issueDescription.text = fromHtml(description, FROM_HTML_MODE_COMPACT)
    }

    override fun notifyIssueTimeline(timelineItems: List<TimeLineItems>) {
        (binding.recyclerview.adapter as? IssueTimelineAdapter)?.addIssueTimeline(timelineItems)
    }

    override fun notifyAdditionalIssueTimeline(timelineItems: List<TimeLineItems>) {
        (binding.recyclerview.adapter as? IssueTimelineAdapter)?.addIssueTimeline(timelineItems)
    }
}