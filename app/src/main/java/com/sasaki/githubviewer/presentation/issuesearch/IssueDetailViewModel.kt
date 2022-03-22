package com.sasaki.githubviewer.presentation.issuesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sasaki.githubviewer.di.AppContainer
import com.sasaki.githubviewer.domain.entity.IssueTimelineSearchResults
import com.sasaki.githubviewer.domain.entity.TimeLineItems
import com.sasaki.githubviewer.domain.usecase.IssueTimelineSearchListener
import com.sasaki.githubviewer.domain.usecase.SearchIssueTimeline
import kotlinx.coroutines.launch

interface IssueDataListener {
    fun notifyIssueTitle(title: String)
    fun notifyIssueDescription(description: String)
    fun notifyIssueTimeline(timelineItems: List<TimeLineItems>)
    fun notifyAdditionalIssueTimeline(timelineItems: List<TimeLineItems>)
}

class IssueDetailViewModel(
    private val appContainer: AppContainer?,
    private val listener: IssueDataListener
) : ViewModel(), IssueTimelineSearchListener {

    val timelineItems: MutableList<TimeLineItems> = mutableListOf()

    private val searchIssueTimelineUseCase: SearchIssueTimeline? by lazy {
        val unwrapAppContainer = appContainer ?: return@lazy null
        SearchIssueTimeline(unwrapAppContainer.searchIssueDataAccessor, this)
    }

    fun searchIssueTimeline(owner: String, name: String, number: Int) {
        viewModelScope.launch {
            searchIssueTimelineUseCase?.searchIssueTimeline(owner, name, number)
        }
    }

    fun searchAdditionalIssueTimeline() {
        viewModelScope.launch {
            searchIssueTimelineUseCase?.searchAdditionalIssueTimeline()
        }
    }

    override fun notifySearchIssueTimeline(result: IssueTimelineSearchResults) {
        val unwrapTitle = result.title ?: return
        val unwrapBodyText = result.bodyText ?: return
        listener.notifyIssueTitle(unwrapTitle)
        listener.notifyIssueDescription(unwrapBodyText)
        listener.notifyIssueTimeline(result.timelineBodyText)
    }

    override fun notifyAdditionalSearchIssueTimeline(result: IssueTimelineSearchResults) {
        listener.notifyAdditionalIssueTimeline(result.timelineBodyText)
    }
}