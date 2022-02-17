package com.sasaki.githubviewer.domain.usecase

import com.sasaki.githubviewer.domain.dataaccessor.SearchRepositoryDataAccessor
import com.sasaki.githubviewer.domain.entity.RepositorySearchResults

interface RepositorySearchListener {
    fun notifyNewSearchRepositoryResult(result: RepositorySearchResults)
    fun notifyAdditionalSearchRepositoryResult(result: RepositorySearchResults)
}

class SearchRepository(private val dataAccessor: SearchRepositoryDataAccessor, private val listener: RepositorySearchListener) {
    /** 検索文字列 */
    var searchName: String? = null
    var cursor: String? = null

    suspend fun searchRepository(name: String) {
        val query = "$name in:name"
        val result = dataAccessor.searchRepository(query)

        listener.notifyNewSearchRepositoryResult(result)
        searchName = name
        setCursor(result)
    }

    /**
     * カーソルを使い追加でデータの読み込みをおこなう
     *
     */
    suspend fun searchAdditionalRepository() {
        val unwrapSearchName = searchName ?: return
        val query = "$unwrapSearchName in:name"
        val result = dataAccessor.searchRepository(query)

        listener.notifyAdditionalSearchRepositoryResult(result)
        setCursor(result)
    }

    suspend fun searchRepositoryInStarsOrder(name: String, after: String?) {
        val query = "$name in:name sort:stars"
        val result = dataAccessor.searchRepository(query, after)

        listener.notifyNewSearchRepositoryResult(result)
        searchName = name
        setCursor(result)
    }

    private fun setCursor(repositorySearchResults: RepositorySearchResults) {
        val unwrapResult = repositorySearchResults.result ?: return
        val lastIndex = unwrapResult.lastIndex
        if (lastIndex == -1) return

        val lastResult = repositorySearchResults.result.getOrNull(lastIndex) ?: return
        cursor = lastResult.cursor
    }
}