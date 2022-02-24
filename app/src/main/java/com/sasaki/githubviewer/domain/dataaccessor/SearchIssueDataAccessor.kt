package com.sasaki.githubviewer.domain.dataaccessor

import com.sasaki.githubviewer.domain.entity.IssueSearchResults
import com.sasaki.githubviewer.type.IssueState

interface SearchIssueDataAccessor {
    suspend fun searchIssue(owner: String, name: String, issueState: List<IssueState>, after: String?): List<IssueSearchResults>
}