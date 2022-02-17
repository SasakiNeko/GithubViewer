package com.sasaki.githubviewer.domain.usecase

import com.sasaki.githubviewer.domain.dataaccessor.SearchRepositoryDataAccessor
import com.sasaki.githubviewer.domain.entity.RepositorySearchResults

interface RepositorySearchListener {
    fun notifyNewSearchRepositoryResult(result: RepositorySearchResults)
    fun notifyAdditionalSearchRepositoryResult(result: RepositorySearchResults)
}

class SearchRepository(private val dataAccessor: SearchRepositoryDataAccessor, private val listener: RepositorySearchListener) {
    /** 検索文字列 */
    private var searchName: String? = null
    private var cursor: String? = null
    private var searchQueryGetter = SearchQueryGetter()

    /**
     * リポジトリを名前から検索する
     *
     * @param name リポジトリ名
     * @param queryType クエリのタイプ
     */
    suspend fun searchRepository(name: String, queryType: SearchQueryGetter.QueryType) {
        val query = searchQueryGetter.getQuery(name, queryType)
        val result = dataAccessor.searchRepository(query)

        listener.notifyNewSearchRepositoryResult(result)
        searchName = name
        setCursor(result)
    }

    /**
     * カーソルを使い追加でデータの読み込みをおこなう
     *
     * @param queryType クエリのタイプ
     */
    suspend fun searchAdditionalRepository(queryType: SearchQueryGetter.QueryType) {
        val unwrapSearchName = searchName ?: return
        val query = searchQueryGetter.getQuery(unwrapSearchName, queryType)
        val result = dataAccessor.searchRepository(query, cursor)

        listener.notifyAdditionalSearchRepositoryResult(result)
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