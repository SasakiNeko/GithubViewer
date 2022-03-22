package com.sasaki.githubviewer.domain.usecase

import com.sasaki.githubviewer.domain.dataaccessor.SearchIssueDataAccessor
import com.sasaki.githubviewer.domain.entity.IssueTimelineSearchResults

interface IssueTimelineSearchListener {
    fun notifySearchIssueTimeline(result: IssueTimelineSearchResults)
    fun notifyAdditionalSearchIssueTimeline(result: IssueTimelineSearchResults)
}

class SearchIssueTimeline(
    private val dataAccessor: SearchIssueDataAccessor,
    private val issueTimelineSearchListener: IssueTimelineSearchListener
) {
    private var cursor: String? = null
    private var searchOwnerName: String? = null
    private var searchRepositoryName: String? = null
    private var searchNumber: Int? = null

    suspend fun searchIssueTimeline(ownerName: String, repositoryName: String, number: Int) {
        if (ownerName.isBlank() || repositoryName.isBlank() || number < 0) return
        val result = dataAccessor.searchIssueTimeLine(
            ownerName,
            repositoryName,
            number
        )
        searchOwnerName = ownerName
        searchRepositoryName = repositoryName
        searchNumber = number
        issueTimelineSearchListener.notifySearchIssueTimeline(result)
        setCursor(result)
    }

    suspend fun searchAdditionalIssueTimeline() {
        val unwrapSearchOwnerName = searchOwnerName ?: return
        val unwrapSearchRepositoryName = searchRepositoryName ?: return
        val unwrapSearchNumber = searchNumber ?: return
        val result = dataAccessor.searchIssueTimeLine(
            unwrapSearchOwnerName,
            unwrapSearchRepositoryName,
            unwrapSearchNumber,
            cursor
        )
        issueTimelineSearchListener.notifyAdditionalSearchIssueTimeline(result)
        setCursor(result)
    }

    private fun setCursor(issueTimelineSearchResult: IssueTimelineSearchResults) {
        val timelineBodyText = issueTimelineSearchResult.timelineBodyText
        if (timelineBodyText.isEmpty()) return
        val lastIndex = timelineBodyText.lastIndex
        if (lastIndex == -1) return

        val lastResult = timelineBodyText.getOrNull(lastIndex) ?: return
        cursor = lastResult.cursor
    }
}