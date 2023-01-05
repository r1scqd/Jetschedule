package ru.rescqd.jetschedule.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = SubjectEntity.TABLE_NAME,
    indices = [
        Index(value = arrayOf(SubjectEntity.SUBJECT_VALUE), unique = true)
    ]
)
data class SubjectEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = SUBJECT_UID)
    val subjectUid: Long? = null,
    @ColumnInfo(name = SUBJECT_VALUE) val subjectValue: String,
    @ColumnInfo(name = SUBJECT_DISPLAY_VALUE) val subjectCustomValue: String = subjectValue
){
    companion object{
        const val SUBJECT_UID = "subject_uid"
        const val SUBJECT_DISPLAY_VALUE = "SUBJECT_DISPLAY_VALUE"
        const val SUBJECT_VALUE = "SUBJECT_VALUE"
        const val TABLE_NAME = "SUBJECT_TABLE"
    }
}
