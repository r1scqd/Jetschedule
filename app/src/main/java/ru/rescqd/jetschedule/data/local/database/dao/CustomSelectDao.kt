package ru.rescqd.jetschedule.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import ru.rescqd.jetschedule.data.container.AudienceContainer
import ru.rescqd.jetschedule.data.container.OnePairInfoContainer
import ru.rescqd.jetschedule.data.local.database.entity.*
import java.time.LocalDate

@Dao
interface CustomSelectDao {


    @Query("SELECT DISTINCT ${ScheduleInfoEntity.DATE} FROM ${ScheduleInfoEntity.TABLE_NAME}")
    fun getSynchronizedScheduleDate(): Flow<List<LocalDate>>

    @Query("SELECT DISTINCT ${ScheduleInfoEntity.DATE} FROM ${ScheduleInfoEntity.TABLE_NAME} " +
            "LEFT JOIN ${GroupEntity.TABLE_NAME} " +
            "ON ${ScheduleInfoEntity.TABLE_NAME}.${GroupEntity.GROUP_UID}=${GroupEntity.TABLE_NAME}.${GroupEntity.GROUP_UID} " +
            "WHERE ${SubjectEntity.SUBJECT_UID}=(SELECT ${SubjectEntity.SUBJECT_UID} FROM ${ScheduleInfoEntity.TABLE_NAME} " +
            "WHERE ${ScheduleInfoEntity.SCHEDULE_INFO_UID}=:pairId) AND ${GroupEntity.GROUP_VALUE}=:groupName " +
            "AND ${ScheduleInfoEntity.DATE}>:date")
    fun getNextPairDatesWithSubjectToGroup(pairId: Long, date: LocalDate, groupName: String): Flow<List<LocalDate>>

    @Query("SELECT DISTINCT ${ScheduleInfoEntity.DATE} FROM ${ScheduleInfoEntity.TABLE_NAME} " +
            "LEFT JOIN ${GroupEntity.TABLE_NAME} " +
            "ON ${ScheduleInfoEntity.TABLE_NAME}.${GroupEntity.GROUP_UID}=${GroupEntity.TABLE_NAME}.${GroupEntity.GROUP_UID} " +
            "WHERE ${SubjectEntity.SUBJECT_UID}=(SELECT ${SubjectEntity.SUBJECT_UID} FROM ${ScheduleInfoEntity.TABLE_NAME} " +
            "WHERE ${ScheduleInfoEntity.SCHEDULE_INFO_UID}=:pairId) AND ${GroupEntity.GROUP_VALUE}=:groupName " +
            "AND ${ScheduleInfoEntity.DATE}<:date")
    fun getPrevPairDatesWithSubjectToGroup(pairId: Long, date: LocalDate, groupName: String): Flow<List<LocalDate>>

    @Query("SELECT ${ScheduleInfoEntity.SCHEDULE_INFO_UID}, ${ScheduleInfoEntity.PAIR_ORDER_VALUE}, " +
            "${AudienceEntity.AUDIENCE_VALUE}, ${SubjectEntity.SUBJECT_VALUE}, ${SubjectEntity.SUBJECT_DISPLAY_VALUE}, " +
            "${TeacherEntity.TEACHER_DEFAULT_VALUE}, ${TeacherEntity.TEACHER_PATRONYMIC_VALUE}, ${TeacherEntity.TEACHER_NAME_VALUE}, " +
            "${TeacherEntity.TEACHER_FAMILY_VALUE}, ${TeacherEntity.TEACHER_DISPLAY_VALUE}, " +
            "${GroupEntity.GROUP_VALUE} " +
            "FROM ${ScheduleInfoEntity.TABLE_NAME} " +
            "LEFT JOIN ${SubjectEntity.TABLE_NAME} " +
            "ON ${SubjectEntity.TABLE_NAME}.${SubjectEntity.SUBJECT_UID}=${ScheduleInfoEntity.TABLE_NAME}.${SubjectEntity.SUBJECT_UID} " +
            "LEFT JOIN ${AudienceEntity.TABLE_NAME} " +
            "ON ${AudienceEntity.TABLE_NAME}.${AudienceEntity.AUDIENCE_UID}=${ScheduleInfoEntity.TABLE_NAME}.${AudienceEntity.AUDIENCE_UID} " +
            "LEFT JOIN ${TeacherEntity.TABLE_NAME} " +
            "ON ${TeacherEntity.TABLE_NAME}.${TeacherEntity.TEACHER_UID}=${ScheduleInfoEntity.TABLE_NAME}.${TeacherEntity.TEACHER_UID} " +
            "LEFT JOIN ${GroupEntity.TABLE_NAME} " +
            "ON ${GroupEntity.TABLE_NAME}.${GroupEntity.GROUP_UID}=${ScheduleInfoEntity.TABLE_NAME}.${GroupEntity.GROUP_UID} " +
            "AND ${GroupEntity.GROUP_VALUE}=:groupName " +
            "WHERE ${ScheduleInfoEntity.DATE}=:date " +
            "AND ${GroupEntity.GROUP_VALUE}=:groupName " +
            "ORDER BY ${ScheduleInfoEntity.PAIR_ORDER_VALUE}")
    fun getScheduleToGroup(date: LocalDate, groupName: String): Flow<List<OnePairInfoContainer>>

    @Query("SELECT ${ScheduleInfoEntity.SCHEDULE_INFO_UID}, ${ScheduleInfoEntity.PAIR_ORDER_VALUE}, " +
            "${AudienceEntity.AUDIENCE_VALUE}, ${SubjectEntity.SUBJECT_VALUE}, ${SubjectEntity.SUBJECT_DISPLAY_VALUE}, " +
            "${TeacherEntity.TEACHER_DEFAULT_VALUE}, ${TeacherEntity.TEACHER_PATRONYMIC_VALUE}, ${TeacherEntity.TEACHER_NAME_VALUE}, " +
            "${TeacherEntity.TEACHER_FAMILY_VALUE}, ${TeacherEntity.TEACHER_DISPLAY_VALUE}, " +
            "${GroupEntity.GROUP_VALUE} " +
            "FROM ${ScheduleInfoEntity.TABLE_NAME} " +
            "LEFT JOIN ${SubjectEntity.TABLE_NAME} " +
            "ON ${SubjectEntity.TABLE_NAME}.${SubjectEntity.SUBJECT_UID}=${ScheduleInfoEntity.TABLE_NAME}.${SubjectEntity.SUBJECT_UID} " +
            "LEFT JOIN ${AudienceEntity.TABLE_NAME} " +
            "ON ${AudienceEntity.TABLE_NAME}.${AudienceEntity.AUDIENCE_UID}=${ScheduleInfoEntity.TABLE_NAME}.${AudienceEntity.AUDIENCE_UID} " +
            "LEFT JOIN ${TeacherEntity.TABLE_NAME} " +
            "ON ${TeacherEntity.TABLE_NAME}.${TeacherEntity.TEACHER_UID}=${ScheduleInfoEntity.TABLE_NAME}.${TeacherEntity.TEACHER_UID} " +
            "LEFT JOIN ${GroupEntity.TABLE_NAME} " +
            "ON ${GroupEntity.TABLE_NAME}.${GroupEntity.GROUP_UID}=${ScheduleInfoEntity.TABLE_NAME}.${GroupEntity.GROUP_UID} " +
            "WHERE ${ScheduleInfoEntity.DATE}=:date " +
            "AND ${TeacherEntity.TABLE_NAME}.${TeacherEntity.TEACHER_DEFAULT_VALUE}=:teacher " +
            "ORDER BY ${ScheduleInfoEntity.PAIR_ORDER_VALUE}")
    fun getScheduleToTeacher(date: LocalDate, teacher: String): Flow<List<OnePairInfoContainer>>


    @Query("SELECT DISTINCT  ${AudienceEntity.AUDIENCE_VALUE}, ${AudienceEntity.CORPUS_VALUE}, ${AudienceEntity.FLOOR_VALUE} FROM ${AudienceEntity.TABLE_NAME} " +
            "ORDER BY ${AudienceEntity.FLOOR_VALUE}")
    fun getAllAudience(): Flow<List<AudienceContainer>>


    @Query(
        "SELECT DISTINCT ${AudienceEntity.AUDIENCE_VALUE}, ${AudienceEntity.CORPUS_VALUE}, ${AudienceEntity.FLOOR_VALUE} FROM ${AudienceEntity.TABLE_NAME} " +
                "WHERE ${AudienceEntity.CORPUS_VALUE}=:corpus " +
                "ORDER BY ${AudienceEntity.FLOOR_VALUE}"
    )
    fun getAllAudience(corpus: Int): Flow<List<AudienceContainer>>

    @Query(
        "SELECT DISTINCT ${AudienceEntity.AUDIENCE_VALUE}, ${AudienceEntity.CORPUS_VALUE}, ${AudienceEntity.FLOOR_VALUE} " +
                "FROM ${ScheduleInfoEntity.TABLE_NAME} " +
                "LEFT JOIN ${AudienceEntity.TABLE_NAME} " +
                "ON ${ScheduleInfoEntity.TABLE_NAME}.audience_uid=${AudienceEntity.TABLE_NAME}.audience_uid " +
                "WHERE ${AudienceEntity.CORPUS_VALUE}=:corpus " +
                "AND ${ScheduleInfoEntity.PAIR_ORDER_VALUE}=:pairOrder " +
                "AND ${ScheduleInfoEntity.DATE}=:date " +
                "ORDER BY ${AudienceEntity.FLOOR_VALUE}"
    )
    fun getBusyAudience(corpus: Int, date: LocalDate, pairOrder: Int): Flow<List<AudienceContainer>>

    @Query(
        "SELECT DISTINCT ${AudienceEntity.AUDIENCE_VALUE}, ${AudienceEntity.CORPUS_VALUE}, ${AudienceEntity.FLOOR_VALUE} " +
                "FROM ${ScheduleInfoEntity.TABLE_NAME} " +
                "LEFT JOIN ${AudienceEntity.TABLE_NAME} " +
                "ON ${ScheduleInfoEntity.TABLE_NAME}.audience_uid=${AudienceEntity.TABLE_NAME}.audience_uid " +
                "WHERE ${AudienceEntity.CORPUS_VALUE}=:corpus " +
                "AND ${ScheduleInfoEntity.DATE}=:date " +
                "ORDER BY ${AudienceEntity.FLOOR_VALUE}"
    )
    fun getBusyAudience(corpus: Int, date: LocalDate): Flow<List<AudienceContainer>>

    @Query(
        "SELECT DISTINCT ${AudienceEntity.AUDIENCE_VALUE}, ${AudienceEntity.CORPUS_VALUE}, ${AudienceEntity.FLOOR_VALUE} " +
                "FROM ${AudienceEntity.TABLE_NAME} " +
                "WHERE ${AudienceEntity.CORPUS_VALUE}=:corpus " +
                "EXCEPT " +
                "SELECT DISTINCT ${AudienceEntity.AUDIENCE_VALUE}, ${AudienceEntity.CORPUS_VALUE}, ${AudienceEntity.FLOOR_VALUE} " +
                "FROM ${ScheduleInfoEntity.TABLE_NAME} " +
                "LEFT JOIN ${AudienceEntity.TABLE_NAME} " +
                "ON ${ScheduleInfoEntity.TABLE_NAME}.audience_uid=${AudienceEntity.TABLE_NAME}.audience_uid " +
                "WHERE ${ScheduleInfoEntity.DATE}=:date " +
                "ORDER BY ${AudienceEntity.FLOOR_VALUE}"
    )
    fun getAvailableAudience(corpus: Int, date: LocalDate): Flow<List<AudienceContainer>>

    @Query(
        "SELECT DISTINCT ${AudienceEntity.AUDIENCE_VALUE}, ${AudienceEntity.CORPUS_VALUE}, ${AudienceEntity.FLOOR_VALUE} " +
                "FROM ${AudienceEntity.TABLE_NAME} " +
                "WHERE ${AudienceEntity.CORPUS_VALUE}=:corpus " +
                "EXCEPT " +
                "SELECT DISTINCT ${AudienceEntity.AUDIENCE_VALUE}, ${AudienceEntity.CORPUS_VALUE}, ${AudienceEntity.FLOOR_VALUE} " +
                "FROM ${ScheduleInfoEntity.TABLE_NAME} " +
                "LEFT JOIN ${AudienceEntity.TABLE_NAME} " +
                "ON ${ScheduleInfoEntity.TABLE_NAME}.audience_uid=${AudienceEntity.TABLE_NAME}.audience_uid " +
                "WHERE ${AudienceEntity.CORPUS_VALUE}=:corpus " +
                "AND ${ScheduleInfoEntity.PAIR_ORDER_VALUE}=:pairOrder " +
                "AND ${ScheduleInfoEntity.DATE}=:date " +
                "ORDER BY ${AudienceEntity.FLOOR_VALUE}"
    )
    fun getAvailableAudience(
        corpus: Int,
        date: LocalDate,
        pairOrder: Int,
    ): Flow<List<AudienceContainer>>

    @Query("SELECT ${GroupEntity.GROUP_VALUE} FROM ${GroupEntity.TABLE_NAME} " +
            "ORDER BY ${GroupEntity.GROUP_VALUE}")
    fun getAllGroup(): Flow<List<String>>

    @Query("SELECT ${TeacherEntity.TEACHER_DEFAULT_VALUE} FROM ${TeacherEntity.TABLE_NAME} " +
            "ORDER BY ${TeacherEntity.TEACHER_DEFAULT_VALUE}")
    fun getAllTeacher(): Flow<List<String>>

    @Query("SELECT ${ScheduleInfoEntity.SCHEDULE_INFO_UID}, ${ScheduleInfoEntity.PAIR_ORDER_VALUE}, " +
            "${AudienceEntity.AUDIENCE_VALUE}, ${SubjectEntity.SUBJECT_VALUE}, ${SubjectEntity.SUBJECT_DISPLAY_VALUE}, " +
            "${TeacherEntity.TEACHER_DEFAULT_VALUE}, ${TeacherEntity.TEACHER_PATRONYMIC_VALUE}, ${TeacherEntity.TEACHER_NAME_VALUE}, " +
            "${TeacherEntity.TEACHER_FAMILY_VALUE}, ${TeacherEntity.TEACHER_DISPLAY_VALUE}, " +
            "${GroupEntity.GROUP_VALUE} " +
            "FROM ${ScheduleInfoEntity.TABLE_NAME} " +
            "LEFT JOIN ${SubjectEntity.TABLE_NAME} " +
            "ON ${SubjectEntity.TABLE_NAME}.${SubjectEntity.SUBJECT_UID}=${ScheduleInfoEntity.TABLE_NAME}.${SubjectEntity.SUBJECT_UID} " +
            "LEFT JOIN ${AudienceEntity.TABLE_NAME} " +
            "ON ${AudienceEntity.TABLE_NAME}.${AudienceEntity.AUDIENCE_UID}=${ScheduleInfoEntity.TABLE_NAME}.${AudienceEntity.AUDIENCE_UID} " +
            "LEFT JOIN ${TeacherEntity.TABLE_NAME} " +
            "ON ${TeacherEntity.TABLE_NAME}.${TeacherEntity.TEACHER_UID}=${ScheduleInfoEntity.TABLE_NAME}.${TeacherEntity.TEACHER_UID} " +
            "LEFT JOIN ${GroupEntity.TABLE_NAME} " +
            "ON ${GroupEntity.TABLE_NAME}.${GroupEntity.GROUP_UID}=${ScheduleInfoEntity.TABLE_NAME}.${GroupEntity.GROUP_UID} " +
            "WHERE ${ScheduleInfoEntity.SCHEDULE_INFO_UID}=:pairUid ")
    suspend fun getPairInfo(pairUid: Long): OnePairInfoContainer

}