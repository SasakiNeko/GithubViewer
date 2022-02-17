package com.sasaki.githubviewer.presentation.repositorysearch

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.sasaki.githubviewer.GithubViewerApplication
import com.sasaki.githubviewer.R
import com.sasaki.githubviewer.databinding.FragmentRepositorySearchBinding
import com.sasaki.githubviewer.domain.entity.RepositoryInfo
import com.sasaki.githubviewer.domain.entity.RepositorySearchResults
import com.sasaki.githubviewer.domain.usecase.RepositorySearchListener
import com.sasaki.githubviewer.presentation.scroll.RecyclerViewScroller

class RepositorySearchFragment : Fragment(), RepositorySearchListener {

    lateinit var binding: FragmentRepositorySearchBinding
    lateinit var sortingSpinner: Spinner
    private var scroller: RecyclerViewScroller? = null
    private val sortingSpinnerSelectedPosition: Int
        get() = binding.sortingSpinner.selectedItemPosition
    private val searchViewQuery: String
        get() = binding.searchView.query.toString()

    private val viewModel: RepositorySearchViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val repositorySearchFragment = this@RepositorySearchFragment
                val context = repositorySearchFragment.context

                val container = if (context == null) null
                else GithubViewerApplication().appContainer

                return RepositorySearchViewModel(repositorySearchFragment, container) as T
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(): RepositorySearchFragment {
            return RepositorySearchFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepositorySearchBinding.inflate(inflater, container, false)
        sortingSpinner = binding.sortingSpinner

        setupRepositoryAdapter()
        setSortingSpinnerValues()
        setupSearchView()
        setSortingSpinnerListener()

        return binding.root
    }

    private fun setSortingSpinnerValues() {
        sortingSpinner.adapter = context?.let {
            ArrayAdapter(
                it,
                R.layout.support_simple_spinner_dropdown_item,
                viewModel.sortingSpinnerTitles
            )
        }
    }

    private fun setSortingSpinnerListener() {
        sortingSpinner.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {

                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (searchViewQuery.isNotBlank()) viewModel.searchRepository(searchViewQuery, sortingSpinnerSelectedPosition)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            }
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!viewModel.isSearchEnabledFlag) return false
                val unwrapQuery = query ?: return false
                viewModel.searchRepository(unwrapQuery, sortingSpinnerSelectedPosition)
                viewModel.isSearchEnabledFlag = false
                return true
            }
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.isSearchEnabledFlag = true
                return false
            }
        })
    }

    private fun setupRepositoryAdapter(repositoryInfo: List<RepositoryInfo> = emptyList()) {
        val unwrapContext = context ?: return
        val linearLayoutManager = LinearLayoutManager(unwrapContext)
        val scroller = object : RecyclerViewScroller(linearLayoutManager) {
            override fun reachBottomOfList() {
                viewModel.searchAdditionalRepository(sortingSpinnerSelectedPosition)
            }
        }
        this.scroller = scroller
        binding.recyclerview.apply {
            adapter = RepositoryAdapter(unwrapContext, repositoryInfo.toMutableList())
            layoutManager = linearLayoutManager
            addOnScrollListener(scroller)
            addItemDecoration(DividerItemDecoration(unwrapContext, DividerItemDecoration.VERTICAL))
        }
    }

    override fun notifyNewSearchRepositoryResult(result: RepositorySearchResults) {
        scroller?.previousTotalCount = 0
        (binding.recyclerview.adapter as? RepositoryAdapter)?.setRepositoryInfo(result.result)
    }

    override fun notifyAdditionalSearchRepositoryResult(result: RepositorySearchResults) {
        (binding.recyclerview.adapter as? RepositoryAdapter)?.addRepositoryInfo(result.result)
    }
}