package ru.worklog.synchronizer

interface ExternalTask {

    fun getID() : String

    fun getTitle() : String
}