package ru.worklog.synchronizer

/**
 * Интерфейс `синхронизатора` с внешней системой учета
 * @author koldn
 */
interface Synchronizer {

    /**
     * Найти задачи во внешней системе по подстрокее
     * @param searchString - поисковая строка
     * @return Коллекцию описаний задач
     */
    fun searchTasks(searchString: String): Collection<TaskDescriptor>

    /**
     * @param entry запись затраченного времени
     * @return результат синхронизации
     */
    fun synchronizeEntry(entry: LogEntry): SyncResult
}