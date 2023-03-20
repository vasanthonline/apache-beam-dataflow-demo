package com.example.dataflow.models

data class DataFile(
    val id: String,
    val contents: ByteArray? = null,
    val name: String
)