package ru.rescqd.jetschedule.data.local.repository

import ru.rescqd.jetschedule.data.local.database.dao.RenameDao

@Suppress("unused")
class SchedulePreferencesRepository(
    private val renameDao: RenameDao
) {
    fun getAllSubjects() = renameDao.getAllSubjects()

    suspend fun updateSubject(uid: Long, displayName: String) =
        renameDao.updateSubject(uid, displayName)

    suspend fun updateSubject(defaultName: String, displayName: String) =
        renameDao.updateSubject(defaultName, displayName)
}