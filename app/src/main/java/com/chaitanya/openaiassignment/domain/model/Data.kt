package com.chaitanya.openaiassignment.domain.model

data class Data(
    val favourite_links: List<Any>,
    val overall_url_chart: Map<String, Int>,
    val recent_links: List<Link>,
    val top_links: List<Link>
)