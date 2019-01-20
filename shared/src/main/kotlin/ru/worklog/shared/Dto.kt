package ru.worklog.shared


data class CredentialsDto(val userName: String, val userPassword: String)

data class TimeEntryDto(
    val start: String,
    val end: String? = null,
    val shortDesc: String? = null,
    val burnComment: String? = null,
    val entryID: Long = -1L
)