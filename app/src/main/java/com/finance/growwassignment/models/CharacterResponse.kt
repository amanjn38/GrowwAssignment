package com.finance.growwassignment.models

data class CharacterResponse(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<Result>
)