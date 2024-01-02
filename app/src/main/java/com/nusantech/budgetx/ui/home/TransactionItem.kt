package com.nusantech.budgetx.ui.home

data class CategoryRecord (
    val id: Int,
    val categoryName: String,
    val type: String,
    val limit: Int
)

data class TransactionItem (
    val id: Int,
    val category: CategoryRecord,
    val total: Int,
    val tanggal: String
)