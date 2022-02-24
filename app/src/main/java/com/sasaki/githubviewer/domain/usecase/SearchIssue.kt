package com.sasaki.githubviewer.domain.usecase

import com.sasaki.githubviewer.domain.dataaccessor.SearchIssueDataAccessor
import com.sasaki.githubviewer.domain.entity.IssueSearchResults
import com.sasaki.githubviewer.type.IssueState

interface IssueSearchListener {
    fun notifySearchIssueResult(result: List<IssueSearchResults>)
}

class SearchIssue(private val dataAccessor: SearchIssueDataAccessor, private val listener: IssueSearchListener) {

    enum class IssueState { OPEN, CLOSED }

    suspend fun searchIssue(owner: String, name: String, issueState: IssueState, after: String? = null) {
        val result = dataAccessor.searchIssue(owner, name, getState(issueState), after)
        listener.notifySearchIssueResult(result)
    }

    private fun getState(state: IssueState): List<com.sasaki.githubviewer.type.IssueState> {
        return when(state) {
            IssueState.OPEN -> listOf(com.sasaki.githubviewer.type.IssueState.OPEN)
            IssueState.CLOSED -> listOf(com.sasaki.githubviewer.type.IssueState.CLOSED)
        }
    }
}