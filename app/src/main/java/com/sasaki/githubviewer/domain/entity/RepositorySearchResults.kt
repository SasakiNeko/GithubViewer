package com.sasaki.githubviewer.domain.entity

data class RepositorySearchResults (
    val resultCount: Int,
    val result: List<RepositoryInfo>?
)

data class RepositoryInfo(
    val cursor: String?,
    val nameWithOwner: String?,
    val url: String?,
    val stargazerCount: Int?,
    val forkCount: Int?,
    val watchingCount: Int?
)