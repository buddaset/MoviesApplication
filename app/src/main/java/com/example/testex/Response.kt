package com.example.testex

data class Response(
    val page: Int,
    val results: List<Result>,
    val total_pages: Int,
    val total_results: Int
)