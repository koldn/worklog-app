package ru.worklog.synchronizer

/**
 * Результат синхронизации
 */
sealed class SyncResult

/**
 * Синхронизация не удалась
 * @param errorMessage - сообщение об ошибке
 */
data class ErrorSync(val errorMessage: String) : SyncResult()

/**
 * Успешная синхронизация
 * @param externalLogID идентификатор созданной записи во внешней системе
 */
data class SuccessSync(val externalLogID : String) : SyncResult()