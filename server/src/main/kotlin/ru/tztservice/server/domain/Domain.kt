package ru.tztservice.server.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

@Document
data class DomainUser(@Id @Indexed val userName: String, val password: String)

@Document
data class WorkLogEntry(
    @Indexed
    private val startTimestamp: Long,
    @Indexed
    private val endTimestamp: Long,
    private val description: String,
    @Indexed
    private val userLogin: String,
    @Indexed
    private val task : TaskDescriptor? = null,
    @Id
    private val id: String = UUID.randomUUID().toString()
)

@Document
data class TaskDescriptor(
    @Id
    @Indexed
    private val UUID : String,
    private val title : String,
    private val colorCode : String
)