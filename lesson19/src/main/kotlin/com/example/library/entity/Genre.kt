package com.example.library.entity

import jakarta.persistence.*

// ============================================================================
// ЗАДАНИЕ 1: Заполнить сущность Genre
// ============================================================================
// ИНСТРУКЦИЯ:
// 1. Добавь аннотации @Entity и @Table перед data class
// 2. Добавь @Id и @GeneratedValue перед id

data class Genre(
    val id: Long? = null,
    val name: String = ""
)
