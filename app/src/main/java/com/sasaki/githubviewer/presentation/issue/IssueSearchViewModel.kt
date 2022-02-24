package com.sasaki.githubviewer.presentation.issue

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sasaki.githubviewer.di.AppContainer
import com.sasaki.githubviewer.domain.entity.IssueSearchResults
import com.sasaki.githubviewer.domain.usecase.IssueSearchListener
import com.sasaki.githubviewer.domain.usecase.SearchIssue
import kotlinx.coroutines.launch

class IssueSearchViewModel(
    private val appContainer: AppContainer?
) : ViewModel(), IssueSearchListener {

    private val searchIssueUseCase: SearchIssue? by lazy {
        val unwrapAppContainer = appContainer ?: return@lazy null
        SearchIssue(unwrapAppContainer.searchIssueDataAccessor, this)
    }

    fun searchIssue(
        owner: String,
        name: String,
        issueState: SearchIssue.IssueState,
        after: String? = null
    ) {
        viewModelScope.launch {
            searchIssueUseCase?.searchIssue(owner, name, issueState, after)
        }
    }

    override fun notifySearchIssueResult(result: List<IssueSearchResults>) {

    }
}