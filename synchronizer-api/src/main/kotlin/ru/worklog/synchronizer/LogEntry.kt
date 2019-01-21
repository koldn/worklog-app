package ru.worklog.synchronizer

import java.time.LocalDate

/**
 * Синхронизируемая запись времени
 */
interface LogEntry {

    /**
     * Идентификатор задачи во внешней системе
     */
    fun getExternalTaskID() : String

    /**
     * Комментарий при создании записи во внешней системе
     */
    fun getComment(): String

    /**
     * UTC - Дата создания записи
     */
    fun getEntryDate() : LocalDate

    /**
     * Продолжительность
     */
    fun getDuration(): Long

    /**
     * Имя пользователя для списания
     */
    fun getUserName() : String
}