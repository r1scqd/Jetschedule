package ru.rescqd.jetschedule.data.container

import androidx.room.ColumnInfo
import ru.rescqd.jetschedule.data.local.database.entity.SubjectEntity

data class SubjectContainer(
    @ColumnInfo(name = SubjectEntity.SUBJECT_UID)
    val uid: Long,
    @ColumnInfo(name = SubjectEntity.SUBJECT_VALUE)
    val defaultName: String,
    @ColumnInfo(name = SubjectEntity.SUBJECT_DISPLAY_VALUE)
    val displayName: String
)
