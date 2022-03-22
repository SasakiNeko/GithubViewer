package com.sasaki.githubviewer.infra.remotestore

import com.apollographql.apollo3.api.Optional
import com.sasaki.githubviewer.SearchIssueQuery
import com.sasaki.githubviewer.SearchIssueTimelineQuery
import com.sasaki.githubviewer.domain.entity.IssueSearchResults
import com.sasaki.githubviewer.domain.entity.IssueTimelineSearchResults
import com.sasaki.githubviewer.domain.entity.TimeLineItems
import com.sasaki.githubviewer.infra.Apollo
import com.sasaki.githubviewer.type.IssueState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.lang.Exception

class SearchIssueStore {
    private val apolloClient = Apollo.getApolloClient()

    suspend fun searchIssue(
        owner: String,
        name: String,
        issueState: List<IssueState>,
        after: String? = null
    ): List<IssueSearchResults> {
        return withContext(Dispatchers.IO) {
            try {
                val response = apolloClient.query(
                    SearchIssueQuery(
                        owner,
                        name,
                        issueState,
                        Optional.Present(after)
                    )
                ).execute()

                val errors = response.errors
                if (errors != null && errors.isNotEmpty()) {
                    return@withContext emptyList()
                }

                val issues: List<IssueSearchResults>? =
                    response.data?.repository?.issues?.edges?.map {
                        IssueSearchResults(
                            it?.cursor,
                            it?.node?.title,
                            it?.node?.number
                        )
                    }
                return@withContext issues ?: emptyList()
            } catch (e: Exception) {
                return@withContext emptyList()
            }
        }
    }

    suspend fun searchIssueTimeLine(
        owner: String,
        name: String,
        issueNumber: Int,
        timelineCursor: String? = null
    ): IssueTimelineSearchResults {
        return withContext(Dispatchers.IO) {
            try {
                val response = apolloClient.query(
                    SearchIssueTimelineQuery(
                        owner,
                        name,
                        issueNumber,
                        Optional.Present(timelineCursor)
                    )
                ).execute()

                val errors = response.errors
                if (errors != null && errors.isNotEmpty()) {
                    return@withContext IssueTimelineSearchResults("", "", emptyList())
                }
                val issue = response.data?.repository?.issue
                val issueTimeline: List<TimeLineItems> =
                    issue?.timelineItems?.edges?.map {
                        TimeLineItems(
                            it?.cursor,
                            it?.node?.onIssueComment?.author?.login,
                            it?.node?.onIssueComment?.bodyHTML.toString()
                        )
                    } ?: return@withContext IssueTimelineSearchResults("", "", emptyList())

                return@withContext IssueTimelineSearchResults(
                    issue.title,
                    issue.bodyHTML.toString(),
                    issueTimeline
                )
            } catch (e: Exception) {
                return@withContext IssueTimelineSearchResults("", "", emptyList())
            }
        }
    }
}