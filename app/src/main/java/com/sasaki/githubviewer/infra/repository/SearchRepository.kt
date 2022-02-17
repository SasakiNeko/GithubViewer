package com.sasaki.githubviewer.infra.repository

import android.content.Context
import com.sasaki.githubviewer.domain.dataaccessor.SearchRepositoryDataAccessor
import com.sasaki.githubviewer.domain.entity.RepositorySearchResults
import com.sasaki.githubviewer.infra.remotestore.SearchRepositoryStore

class SearchRepository(val context: Context?) : SearchRepositoryDataAccessor {
    private val remoteStore: SearchRepositoryStore = SearchRepositoryStore()

    override suspend fun searchRepository(name: String, after: String?): RepositorySearchResults {
        return remoteStore.searchRepository(name, after)
    }
}