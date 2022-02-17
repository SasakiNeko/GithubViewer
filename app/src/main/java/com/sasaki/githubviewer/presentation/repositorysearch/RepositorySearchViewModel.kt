package com.sasaki.githubviewer.presentation.repositorysearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sasaki.githubviewer.di.AppContainer
import com.sasaki.githubviewer.domain.entity.RepositorySearchResults
import com.sasaki.githubviewer.domain.usecase.RepositorySearchListener
import com.sasaki.githubviewer.domain.usecase.SearchQueryGetter
import com.sasaki.githubviewer.domain.usecase.SearchRepository
import kotlinx.coroutines.launch

class RepositorySearchViewModel(
    private val listener: RepositorySearchListener,
    private val appContainer: AppContainer?
) : ViewModel() {
    val sortingSpinnerTitles = mutableListOf("関連度順", "スター順", "新しい順", "古い順")
    private val searchQueryTypes = mutableListOf(
        SearchQueryGetter.QueryType.MATCH,
        SearchQueryGetter.QueryType.STARS,
        SearchQueryGetter.QueryType.NEWEST,
        SearchQueryGetter.QueryType.OLDEST
    )

    /** 検索可能かを表すフラグ */
    var isSearchEnabledFlag = true

    private val searchRepositoryUseCase: SearchRepository? by lazy {
        val unwrapAppContainer = appContainer ?: return@lazy null
        SearchRepository(unwrapAppContainer.searchRepositoryDataAccessor, listener)
    }

    /**
     * リポジトリを名前から検索する
     *
     * @param name リポジトリ名
     * @param selectedSpinnerPosition スピナーの選択されているポジション
     */
    fun searchRepository(name: String, selectedSpinnerPosition: Int) {
        val queryType = searchQueryTypes.getOrElse(selectedSpinnerPosition) { SearchQueryGetter.QueryType.MATCH }
        viewModelScope.launch {
            searchRepositoryUseCase?.searchRepository(name, queryType)
        }
    }

    /**
     * カーソルを使い追加でデータの読み込みをおこなう
     *
     * @param selectedSpinnerPosition スピナーの選択されているポジション
     */
    fun searchAdditionalRepository(selectedSpinnerPosition: Int) {
        val queryType = searchQueryTypes.getOrElse(selectedSpinnerPosition) { SearchQueryGetter.QueryType.MATCH }
        viewModelScope.launch {
            searchRepositoryUseCase?.searchAdditionalRepository(queryType)
        }
    }
}