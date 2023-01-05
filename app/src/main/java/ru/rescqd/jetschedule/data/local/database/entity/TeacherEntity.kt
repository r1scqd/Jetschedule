package ru.rescqd.jetschedule.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = TeacherEntity.TABLE_NAME
)
data class TeacherEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = TEACHER_UID)
    val teacherUid: Long? = null,
    @ColumnInfo(name = TEACHER_DEFAULT_VALUE) val teacherDefaultValue: String,
    @ColumnInfo(name = TEACHER_PATRONYMIC_VALUE) val teacherPatronymicValue: String,
    @ColumnInfo(name = TEACHER_NAME_VALUE) val teacherNameValue: String,
    @ColumnInfo(name = TEACHER_FAMILY_VALUE) val teacherFamilyValue: String,
    @ColumnInfo(name = TEACHER_DISPLAY_VALUE) val teacherDisplayValue: String = teacherDefaultValue
){
    companion object{
        const val TEACHER_UID = "teacher_uid"
        const val TEACHER_DISPLAY_VALUE= "TEACHER_DISPLAY_VALUE"
        const val TEACHER_DEFAULT_VALUE = "TEACHER_DEFAULT_VALUE"
        const val TEACHER_NAME_VALUE = "TEACHER_NAME_VALUE"
        const val TEACHER_FAMILY_VALUE = "TEACHER_FAMILY_VALUE"
        const val TEACHER_PATRONYMIC_VALUE = "TEACHER_PATRONYMIC_VALUE"
        const val TABLE_NAME = "TEACHER_TABLE"
    }
}