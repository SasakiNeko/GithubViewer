package com.sasaki.githubviewer.infra.remotestore

import com.apollographql.apollo3.api.Optional
import com.sasaki.githubviewer.SearchRepositoryQuery
import com.sasaki.githubviewer.infra.Apollo
import com.sasaki.githubviewer.domain.entity.RepositoryInfo
import com.sasaki.githubviewer.domain.entity.RepositorySearchResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepositoryStore {
    private val apolloClient = Apollo.getApolloClient()

    /**
     * リポジトリを名前から検索する
     *
     * @param name リポジトリ名
     * @param after カーソル
     * @return 検索結果 RepositorySearchResults
     */
    suspend fun searchRepository(name: String, after: String? = null): RepositorySearchResults {
        return withContext(Dispatchers.IO) {
            try {
                val response = apolloClient.query(SearchRepositoryQuery(name, Optional.Present(after))).execute()
                val errors = response.errors

                if (errors != null && errors.isNotEmpty()) {
                    return@withContext RepositorySearchResults(0, null)
                }

                val searchResults = response.data?.search
                val repositoryInfoList = searchResults?.edges?.map {
                    RepositoryInfo(
                        it?.cursor,
                        it?.node?.onRepository?.nameWithOwner,
                        it?.node?.onRepository?.url as? String,
                        it?.node?.onRepository?.stargazerCount,
                        it?.node?.onRepository?.forkCount,
                        it?.node?.onRepository?.watchers?.totalCount,
                    )
                }

                return@withContext RepositorySearchResults(
                    searchResults?.repositoryCount ?: 0,
                    repositoryInfoList
                )
            } catch (e: Exception) {
                return@withContext RepositorySearchResults(0, null)
            }
        }
    }
}