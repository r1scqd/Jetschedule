package ru.rescqd.jetschedule.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.rescqd.jetschedule.data.local.database.conventer.DateConverter
import ru.rescqd.jetschedule.data.local.database.dao.CustomSelectDao
import ru.rescqd.jetschedule.data.local.database.dao.ScheduleDropDao
import ru.rescqd.jetschedule.data.local.database.dao.ScheduleSaveDao
import ru.rescqd.jetschedule.data.local.database.entity.*

@Database(
    entities = [GroupEntity::class, SubjectEntity::class, ScheduleInfoEntity::class, AudienceEntity::class, TeacherEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(DateConverter::class)
abstract class JetscheduleDatabase : RoomDatabase(){
    abstract fun getScheduleSaveDao(): ScheduleSaveDao
    abstract fun getCustomSelectDao(): CustomSelectDao
    abstract fun getScheduleDropDao(): ScheduleDropDao
}