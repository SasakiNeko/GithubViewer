package com.sasaki.githubviewer.domain.usecase

import com.sasaki.githubviewer.domain.dataaccessor.SearchIssueDataAccessor
import com.sasaki.githubviewer.domain.entity.IssueSearchResults

interface IssueSearchListener {
    fun notifySearchIssueResult(result: List<IssueSearchResults>)
    fun notifyAdditionalSearchIssueResult(result: List<IssueSearchResults>)
}


class SearchIssue(
    private val dataAccessor: SearchIssueDataAccessor,
    private val issueSearchListener: IssueSearchListener
) {

    private var cursor: String? = null
    private var searchOwnerName: String? = null
    private var searchRepositoryName: String? = null
    private var searchIssueState : IssueState? = null

    suspend fun searchIssue(owner: String, name: String, issueState: IssueState) {
        val result = dataAccessor.searchIssue(owner, name, getState(issueState))
        issueSearchListener.notifySearchIssueResult(result)
        searchOwnerName = owner
        searchRepositoryName = name
        searchIssueState = issueState
        setCursor(result)
    }

    suspend fun searchAdditionalRepository() {
        val unwrapSearchOwnerName = searchOwnerName ?: return
        val unwrapSearchRepositoryName = searchRepositoryName ?: return
        val unwrapIssueState = searchIssueState ?: return
        val result = dataAccessor.searchIssue(unwrapSearchOwnerName, unwrapSearchRepositoryName, getState(unwrapIssueState), cursor)
        issueSearchListener.notifyAdditionalSearchIssueResult(result)
        setCursor(result)
    }

    private fun getState(state: IssueState): List<com.sasaki.githubviewer.type.IssueState> {
        return when(state) {
            IssueState.OPEN -> listOf(com.sasaki.githubviewer.type.IssueState.OPEN)
            IssueState.CLOSED -> listOf(com.sasaki.githubviewer.type.IssueState.CLOSED)
        }
    }

    private fun setCursor(issueSearchResults: List<IssueSearchResults>) {
        if (issueSearchResults.isEmpty()) return
        val lastIndex = issueSearchResults.lastIndex
        if (lastIndex == -1) return

        val lastResult = issueSearchResults.getOrNull(lastIndex) ?: return
        cursor = lastResult.cursor
    }
}