package com.mackenzie.waifuviewer.domain.video


data class CategoriesItem(
    val categories: List<CategoryItem>,
    val count: Int
)

data class CategoryItem(
    val category: String
)
