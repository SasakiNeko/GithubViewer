package com.sasaki.githubviewer.presentation.repositorysearch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sasaki.githubviewer.di.AppContainer
import com.sasaki.githubviewer.domain.entity.RepositorySearchResults
import com.sasaki.githubviewer.domain.usecase.RepositorySearchListener
import com.sasaki.githubviewer.domain.usecase.SearchRepository
import kotlinx.coroutines.launch

class RepositorySearchViewModel(
    private val listener: RepositorySearchListener,
    private val appContainer: AppContainer?
) : ViewModel() {
    val sortingSpinnerValues = mutableListOf("関連度順", "スター順", "新しい順", "古い順")

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
     * @param after カーソル
     */
    fun searchRepository(name: String) {
        viewModelScope.launch {
            searchRepositoryUseCase?.searchRepository(name)
        }
    }

    fun searchAdditionalRepository() {
        viewModelScope.launch {
            searchRepositoryUseCase?.searchAdditionalRepository()
        }
    }

    fun searchRepositoryInStarsOrder(name: String, after: String?) {
        viewModelScope.launch {
            searchRepositoryUseCase?.searchRepositoryInStarsOrder(name, after)
        }
    }
}