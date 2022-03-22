package com.sasaki.githubviewer.presentation.issuesearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sasaki.githubviewer.di.AppContainer
import com.sasaki.githubviewer.domain.entity.IssueSearchResults
import com.sasaki.githubviewer.domain.usecase.IssueSearchListener
import com.sasaki.githubviewer.domain.usecase.SearchIssue
import kotlinx.coroutines.launch

class IssueSearchViewModel(
    private val appContainer: AppContainer?,
    private val listener: IssueSearchListener
) : ViewModel() {

    enum class IssueState { OPEN, CLOSED }

    private val searchIssueUseCase: SearchIssue? by lazy {
        val unwrapAppContainer = appContainer ?: return@lazy null
        SearchIssue(unwrapAppContainer.searchIssueDataAccessor, listener)
    }

    var previousSearchOwnerName = ""
    var previousSearchRepositoryName = ""

    val issues: MutableList<IssueSearchResults> = mutableListOf()

    fun searchIssue(
        owner: String,
        name: String,
        issueState: IssueState
    ) {
        val state = convertIssueState(issueState)
        viewModelScope.launch {
            searchIssueUseCase?.searchIssue(owner, name, state)
        }
    }

    fun searchAdditionalIssue() {
        viewModelScope.launch {
            searchIssueUseCase?.searchAdditionalRepository()
        }
    }

    /**
     * 検索ワードが変更されたか確認する
     *
     * @param ownerName
     * @param repositoryName
     * @return true: 変更あり, false: 変更なし
     */
    fun checkSearchWordChange(ownerName: String, repositoryName: String) =
        ownerName != previousSearchOwnerName || repositoryName != previousSearchRepositoryName

    private fun convertIssueState(issueState: IssueState): com.sasaki.githubviewer.domain.usecase.IssueState {
        return if (issueState == IssueState.OPEN) {
            com.sasaki.githubviewer.domain.usecase.IssueState.OPEN
        } else {
            com.sasaki.githubviewer.domain.usecase.IssueState.CLOSED
        }
    }
}