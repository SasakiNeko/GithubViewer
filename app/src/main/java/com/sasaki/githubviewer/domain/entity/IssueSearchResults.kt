package com.sasaki.githubviewer.domain.entity

data class IssueSearchResults(
    val title: String?,
    val bodyText: String?,
    val titleLineItems: List<TimeLineItems>?
)

data class TimeLineItems(
    val cursor: String?,
    val issueComment: String?
)