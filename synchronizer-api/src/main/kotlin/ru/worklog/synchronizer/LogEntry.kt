package ru.worklog.synchronizer

interface LogEntry {

    fun getID() : String

    fun getComment(): String

    fun getDuration(): Long
}