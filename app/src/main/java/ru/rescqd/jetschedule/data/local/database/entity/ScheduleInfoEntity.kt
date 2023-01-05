package ru.rescqd.jetschedule.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = ScheduleInfoEntity.TABLE_NAME)
data class ScheduleInfoEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SCHEDULE_INFO_UID)
    val scheduleInfoUid: Long? = null,
    @ColumnInfo(name = DATE)
    val date: LocalDate,
    @ColumnInfo(name = SubjectEntity.SUBJECT_UID) val subjectUid: Long,
    @ColumnInfo(name = TeacherEntity.TEACHER_UID) val teacherUid: Long,
    @ColumnInfo(name = AudienceEntity.AUDIENCE_UID) val audienceUid: Long,
    @ColumnInfo(name = GroupEntity.GROUP_UID) val groupUid: Long,
    @ColumnInfo(name = PAIR_ORDER_VALUE) val pairOrderValue: Int,

    ){
    companion object{
        const val SCHEDULE_INFO_UID = "schedule_info_uid"
        const val TABLE_NAME = "schedule_info_table"
        const val DATE = "date"
        const val PAIR_ORDER_VALUE = "PAIR_ORDER_VALUE"
    }
}
