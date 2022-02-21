package com.sasaki.githubviewer.presentation.repositorysearch

import java.io.Serializable

data class SerializableRepositoryInfo(
    val cursor: String?,
    val nameWithOwner: String?,
    val url: String?,
    val stargazerCount: Int?,
    val forkCount: Int?,
    val watchingCount: Int?
) : Serializable