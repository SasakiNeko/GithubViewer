package com.sasaki.githubviewer.infra.repository

import android.content.Context
import com.sasaki.githubviewer.domain.dataaccessor.SearchIssueDataAccessor
import com.sasaki.githubviewer.domain.dataaccessor.SearchRepositoryDataAccessor
import com.sasaki.githubviewer.domain.entity.IssueSearchResults
import com.sasaki.githubviewer.domain.entity.IssueTimelineSearchResults
import com.sasaki.githubviewer.domain.entity.RepositorySearchResults
import com.sasaki.githubviewer.infra.remotestore.SearchIssueStore
import com.sasaki.githubviewer.infra.remotestore.SearchRepositoryStore
import com.sasaki.githubviewer.type.IssueState

class SearchRepository(val context: Context?) : SearchRepositoryDataAccessor,
    SearchIssueDataAccessor {
    private val searchRepositoryRemoteStore: SearchRepositoryStore = SearchRepositoryStore()
    private val searchIssueRemoteStore: SearchIssueStore = SearchIssueStore()

    override suspend fun searchRepository(name: String, after: String?): RepositorySearchResults {
        return searchRepositoryRemoteStore.searchRepository(name, after)
    }

    override suspend fun searchIssue(
        owner: String,
        name: String,
        issueState: List<IssueState>,
        after: String?
    ): List<IssueSearchResults> {
        return searchIssueRemoteStore.searchIssue(owner, name, issueState, after)
    }

    override suspend fun searchIssueTimeLine(
        owner: String,
        name: String,
        number: Int,
        after: String?
    ): IssueTimelineSearchResults {
        return searchIssueRemoteStore.searchIssueTimeLine(owner, name, number, after)
    }
}