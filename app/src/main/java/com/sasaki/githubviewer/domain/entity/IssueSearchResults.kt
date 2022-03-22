package com.sasaki.githubviewer.domain.entity

data class IssueSearchResults(
    val cursor: String?,
    val title: String?,
    val number: Int?
)