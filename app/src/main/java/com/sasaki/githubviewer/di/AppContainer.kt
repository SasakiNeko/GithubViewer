package com.sasaki.githubviewer.di

import android.content.Context
import com.sasaki.githubviewer.domain.dataaccessor.SearchRepositoryDataAccessor
import com.sasaki.githubviewer.infra.repository.SearchRepository

class AppContainer(val context: Context?) {
    val searchRepositoryDataAccessor: SearchRepositoryDataAccessor = SearchRepository(context)
}