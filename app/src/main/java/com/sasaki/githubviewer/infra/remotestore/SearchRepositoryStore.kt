package com.sasaki.githubviewer.infra.remotestore

import android.content.Context
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
            val response = apolloClient.query(SearchRepositoryQuery(name, Optional.Present(after))).execute()
            val data = response.data?.search
            val repositoryInfoList = data?.edges?.map {
                RepositoryInfo(
                    it?.cursor,
                    it?.node?.onRepository?.nameWithOwner,
                    it?.node?.onRepository?.url as? String,
                    it?.node?.onRepository?.stargazerCount,
                    it?.node?.onRepository?.forkCount,
                    it?.node?.onRepository?.watchers?.totalCount,
                )
            }

            RepositorySearchResults(
                data?.repositoryCount ?: 0,
                repositoryInfoList
            )
        }
    }
}