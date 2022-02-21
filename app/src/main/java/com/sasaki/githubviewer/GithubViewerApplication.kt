package com.sasaki.githubviewer

import android.app.Application
import android.content.Context
import com.sasaki.githubviewer.di.AppContainer

class GithubViewerApplication : Application() {
    val appContainer = AppContainer(baseContext ?: null)
}