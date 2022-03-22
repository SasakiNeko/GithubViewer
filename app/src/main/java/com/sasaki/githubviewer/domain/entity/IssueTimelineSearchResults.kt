package com.sasaki.githubviewer.domain.entity

data class IssueTimelineSearchResults(
    val title: String?,
    val bodyText: String?,
    val timelineBodyText: List<TimeLineItems>
)

data class TimeLineItems(
    val cursor: String?,
    val name: String?,
    val issueComment: String?
)
