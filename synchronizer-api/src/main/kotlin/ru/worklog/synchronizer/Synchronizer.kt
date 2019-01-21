package ru.worklog.synchronizer

interface Synchronizer {
    fun searchTasks(searchString : String) : Collection<ExternalTask>

    fun synchronizeEntry(entry: LogEntry)
}