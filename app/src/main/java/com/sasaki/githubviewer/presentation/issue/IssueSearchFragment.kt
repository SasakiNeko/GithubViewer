package com.sasaki.githubviewer.presentation.issue

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sasaki.githubviewer.GithubViewerApplication
import com.sasaki.githubviewer.R
import com.sasaki.githubviewer.databinding.FragmentIssueSearchBinding
import com.sasaki.githubviewer.presentation.repositorysearch.RepositorySearchFragment
import com.sasaki.githubviewer.presentation.repositorysearch.RepositorySearchViewModel


class IssueSearchFragment : Fragment() {

    lateinit var binding: FragmentIssueSearchBinding

    private val viewModel: IssueSearchViewModel by viewModels {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                val issueSearchFragment = this@IssueSearchFragment
                val context = issueSearchFragment.context

                val container = if (context == null) null
                else GithubViewerApplication().appContainer

                return IssueSearchViewModel(container) as T
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = IssueSearchFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentIssueSearchBinding.inflate(inflater, container, false)

        return binding.root
    }
}