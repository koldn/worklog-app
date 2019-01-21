package ru.worklog.synchronizer

/**
 * Описание задачи во внешней системе
 * @param externalID - идентификатор задачи во внешней системе
 * @param title - название задачи
 */
data class TaskDescriptor(val externalID: String, val title: String)