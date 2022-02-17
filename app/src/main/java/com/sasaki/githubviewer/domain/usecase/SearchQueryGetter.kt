package com.sasaki.githubviewer.domain.usecase

class SearchQueryGetter {
    /**
     * クエリのタイプを表すenum
     * MATCH : 関連度順
     * STARS : スター順
     * NEWEST : 新しい順
     * OLDEST : 古い順
     */
    enum class QueryType { MATCH, STARS, NEWEST, OLDEST }

    /**
     * 与えられたqueryTypeに対応したクエリを返す
     *
     * @param query 検索ワード
     * @param queryType クエリのタイプ
     * @return queryTypeに則したクエリ
     */
    fun getQuery(query: String, queryType: QueryType): String {
        return when (queryType) {
            QueryType.MATCH -> "$query in:name"
            QueryType.STARS -> "$query in:name sort:stars"
            QueryType.NEWEST -> "$query in:name sort:updated"
            QueryType.OLDEST -> "$query in:name sort:updated-asc"
        }
    }
}