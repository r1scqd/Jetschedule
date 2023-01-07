package ru.rescqd.jetschedule.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.rescqd.jetschedule.data.container.SubjectContainer
import ru.rescqd.jetschedule.data.local.database.entity.SubjectEntity

@Dao
interface RenameDao {

    @Query("SELECT * FROM ${SubjectEntity.TABLE_NAME}")
    fun getAllSubjects(): Flow<List<SubjectContainer>>

    @Query(
        "UPDATE ${SubjectEntity.TABLE_NAME} " +
                "SET ${SubjectEntity.SUBJECT_DISPLAY_VALUE}=:displayName " +
                "WHERE ${SubjectEntity.SUBJECT_UID}=:uid"
    )
    suspend fun updateSubject(uid: Long, displayName: String)

    @Query(
        "UPDATE ${SubjectEntity.TABLE_NAME} " +
                "SET ${SubjectEntity.SUBJECT_DISPLAY_VALUE}=:displayName " +
                "WHERE ${SubjectEntity.SUBJECT_VALUE}=:defaultName"
    )
    suspend fun updateSubject(defaultName: String, displayName: String)
}