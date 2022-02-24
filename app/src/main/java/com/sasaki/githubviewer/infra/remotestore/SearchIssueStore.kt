package com.sasaki.githubviewer.infra.remotestore

import com.apollographql.apollo3.api.Optional
import com.sasaki.githubviewer.SearchIssueQuery
import com.sasaki.githubviewer.domain.entity.IssueSearchResults
import com.sasaki.githubviewer.domain.entity.TimeLineItems
import com.sasaki.githubviewer.infra.Apollo
import com.sasaki.githubviewer.type.IssueState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchIssueStore {
    private val apolloClient = Apollo.getApolloClient()

    suspend fun searchIssue(
        owner: String,
        name: String,
        issueState: List<IssueState>,
        after: String? = null
    ): List<IssueSearchResults> {
        return withContext(Dispatchers.IO) {
            val response = apolloClient.query(
                SearchIssueQuery(
                    owner,
                    name,
                    issueState,
                    Optional.Present(after)
                )
            ).execute()


            val issues: List<IssueSearchResults>? = response.data?.repository?.issues?.nodes?.map {
                IssueSearchResults(
                    it?.title,
                    it?.bodyText,
                    getTimeLineItems(it?.timelineItems)
                )
            }
            return@withContext issues ?: emptyList()
        }
    }

    private fun getTimeLineItems(timelineItems: SearchIssueQuery.TimelineItems?): List<TimeLineItems>? {
        return timelineItems?.edges?.map {
            TimeLineItems(
                it?.cursor,
                it?.node?.onIssueComment?.bodyText
            )
        }
    }
}