package ru.rescqd.jetschedule.data.container

import androidx.room.ColumnInfo
import ru.rescqd.jetschedule.data.local.database.entity.*

data class OnePairInfoContainer(
    @ColumnInfo(name = ScheduleInfoEntity.SCHEDULE_INFO_UID) val scheduleUid: Long,
    @ColumnInfo(name = ScheduleInfoEntity.PAIR_ORDER_VALUE) val pairOrder: Int,
    @ColumnInfo(name = AudienceEntity.AUDIENCE_VALUE) val audience: String,
    @ColumnInfo(name = SubjectEntity.SUBJECT_VALUE) val subjectDefault: String,
    @ColumnInfo(name = SubjectEntity.SUBJECT_DISPLAY_VALUE) val subjectCustom: String,
    @ColumnInfo(name = TeacherEntity.TEACHER_DEFAULT_VALUE) val teacherDefault: String,
    @ColumnInfo(name = TeacherEntity.TEACHER_DISPLAY_VALUE) val teacherCustom: String,
    @ColumnInfo(name = TeacherEntity.TEACHER_PATRONYMIC_VALUE) val teacherPatronymic: String,
    @ColumnInfo(name = TeacherEntity.TEACHER_NAME_VALUE) val teacherName: String,
    @ColumnInfo(name = TeacherEntity.TEACHER_FAMILY_VALUE) val teacherFamily: String,
    @ColumnInfo(name = GroupEntity.GROUP_VALUE) val group: String,
)