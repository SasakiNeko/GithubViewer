package com.sasaki.githubviewer.domain.dataaccessor

import com.sasaki.githubviewer.domain.entity.RepositorySearchResults

interface SearchRepositoryDataAccessor {
    suspend fun searchRepository(name: String, after: String? = null): RepositorySearchResults
}