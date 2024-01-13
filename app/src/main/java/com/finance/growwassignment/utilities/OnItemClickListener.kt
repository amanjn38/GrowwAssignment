package com.finance.growwassignment.utilities

import com.finance.growwassignment.models.Result

interface OnItemClickListener<Result> {
    fun onItemClick(data: Result)
}
