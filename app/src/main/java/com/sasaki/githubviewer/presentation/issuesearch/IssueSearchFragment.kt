package com.sasaki.githubviewer.presentation.issuesearch

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sasaki.githubviewer.GithubViewerApplication
import com.sasaki.githubviewer.databinding.FragmentIssueSearchBinding
import com.sasaki.githubviewer.domain.entity.IssueSearchResults
import com.sasaki.githubviewer.domain.usecase.IssueSearchListener
import com.sasaki.githubviewer.presentation.scroll.RecyclerViewScroller

class IssueSearchFragment : Fragment(), IssueSearchListener, IssueAdapter.OnClickItemListener {

    interface OnIssueItemClickListener {
        fun notifyIssueItemClick(ownerName: String, repositoryName: String, issueNumber: Int)
    }

    lateinit var binding: FragmentIssueSearchBinding
    private var scroller: RecyclerViewScroller? = null
    private val ownerName: String
        get() = binding.ownerName.text?.toString() ?: ""
    private val repositoryName
        get() = binding.repositoryName.text?.toString() ?: ""
    private var listener: OnIssueItemClickListener? = null

    private val viewModel: IssueSearchViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val issueSearchFragment = this@IssueSearchFragment
                val context = issueSearchFragment.context

                val container = if (context == null) null
                else GithubViewerApplication().appContainer

                return IssueSearchViewModel(container, this@IssueSearchFragment) as T
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = IssueSearchFragment()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as? OnIssueItemClickListener
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIssueSearchBinding.inflate(inflater, container, false)

        setupSearchButton()
        setupIssueAdapter()
        return binding.root
    }

    private fun setupIssueAdapter() {
        val unwrapContext = context ?: return
        val linearLayoutManager = LinearLayoutManager(unwrapContext)
        val scroller = object : RecyclerViewScroller(linearLayoutManager) {
            override fun reachBottomOfList() {
                viewModel.searchAdditionalIssue()
            }
        }
        this.scroller = scroller
        binding.recyclerview.apply {
            adapter = IssueAdapter(
                unwrapContext,
                viewModel.issues,
                this@IssueSearchFragment
            )
            layoutManager = linearLayoutManager
            addOnScrollListener(scroller)
            addItemDecoration(DividerItemDecoration(unwrapContext, DividerItemDecoration.VERTICAL))
        }
    }

    private fun setupSearchButton() {
        binding.searchButton.setOnClickListener {
            if (!viewModel.checkSearchWordChange(
                    ownerName,
                    repositoryName
                )
            ) return@setOnClickListener
            searchIssue()

            viewModel.previousSearchOwnerName = ownerName
            viewModel.previousSearchRepositoryName = repositoryName
        }
    }

    private fun searchIssue() {
        val unwrapOwnerName = binding.ownerName.text.toString()
        val unwrapRepositoryName = binding.repositoryName.text.toString()
        if (unwrapOwnerName.isBlank() || unwrapRepositoryName.isBlank()) return
        viewModel.searchIssue(
            unwrapOwnerName,
            unwrapRepositoryName,
            IssueSearchViewModel.IssueState.OPEN
        )
    }

    override fun notifySearchIssueResult(result: List<IssueSearchResults>) {
        scroller?.previousTotalCount = 0
        viewModel.issues.clear()
        (binding.recyclerview.adapter as? IssueAdapter)?.addIssues(result)
    }

    override fun notifyAdditionalSearchIssueResult(result: List<IssueSearchResults>) {
        (binding.recyclerview.adapter as? IssueAdapter)?.addIssues(result)
    }

    override fun notifyClickAdapterItem(issueNumber: Int) {
        viewModel.apply {
            listener?.notifyIssueItemClick(
                previousSearchOwnerName,
                previousSearchRepositoryName,
                issueNumber
            )
        }
    }
}