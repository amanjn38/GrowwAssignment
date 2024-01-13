package com.finance.growwassignment.models

data class Movies(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<MovieResult>
)