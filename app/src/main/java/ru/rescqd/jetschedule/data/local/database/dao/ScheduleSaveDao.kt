package ru.rescqd.jetschedule.data.local.database.dao

import androidx.room.*
import ru.rescqd.jetschedule.data.local.database.entity.*
import java.time.LocalDate

@Dao
interface ScheduleSaveDao {


    @Query("DELETE FROM ${ScheduleInfoEntity.TABLE_NAME} " +
            "WHERE ${ScheduleInfoEntity.DATE}=:date")
    suspend fun dropScheduleInfo(date: LocalDate)

    @Query("DELETE FROM ${ScheduleInfoEntity.TABLE_NAME}")
    suspend fun dropScheduleInfo()


    @Query("SELECT EXISTS(SELECT * from ${ScheduleInfoEntity.TABLE_NAME} " +
            "WHERE ${ScheduleInfoEntity.DATE}=:date)")
    suspend fun checkExistsSchedule(date: LocalDate): Boolean

    @Insert
    suspend fun addGroup(groupEntity: GroupEntity)

    @Insert
    suspend fun addScheduleInfo(scheduleInfoEntity: ScheduleInfoEntity)

    @Insert
    suspend fun addSubject(subjectEntity: SubjectEntity)

    @Insert
    suspend fun addAudience(audienceEntity: AudienceEntity)

    @Insert
    suspend fun addTeacher(teacherEntity: TeacherEntity)


    //@Query("SELECT COUNT (*) FROM ${ScheduleMetaInfoEntity.TABLE_NAME} " +
    //        "WHERE ${ScheduleInfoEntity.DATE}=:date")
    //suspend fun getExistScheduleMetaCount(date: LocalDate): Int

    @Query("SElECT ${GroupEntity.GROUP_VALUE} FROM ${GroupEntity.TABLE_NAME} " +
            "WHERE ${GroupEntity.GROUP_UID}=:groupUid")
    suspend fun getGroupName(groupUid: Long): String?

    @Query("SElECT ${GroupEntity.GROUP_UID} FROM ${GroupEntity.TABLE_NAME} " +
            "WHERE ${GroupEntity.GROUP_VALUE}=:groupName")
    suspend fun getGroupId(groupName: String): Long?

    @Query("SELECT ${SubjectEntity.SUBJECT_UID} FROM ${SubjectEntity.TABLE_NAME} " +
            "WHERE ${SubjectEntity.SUBJECT_VALUE}=:subjectName")
    suspend fun getSubjectId(subjectName: String): Long?


    @Query("SELECT ${TeacherEntity.TEACHER_UID} FROM ${TeacherEntity.TABLE_NAME} " +
            "WHERE ${TeacherEntity.TEACHER_DEFAULT_VALUE}=:teacherName"
    )
    suspend fun getTeacherId(teacherName: String): Long?

    @Query(
        "SELECT ${AudienceEntity.AUDIENCE_UID} FROM ${AudienceEntity.TABLE_NAME} " +
                "WHERE ${AudienceEntity.AUDIENCE_VALUE}=:audienceName"
    )
    suspend fun getAudienceId(audienceName: String): Long?

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateSubjectInfo(subjectEntity: SubjectEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateTeacherInfo(teacherEntity: TeacherEntity)
}


