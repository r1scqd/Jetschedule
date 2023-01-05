package ru.rescqd.jetschedule.data.local.database.dao

import androidx.room.Dao
import androidx.room.Query
import ru.rescqd.jetschedule.data.local.database.entity.ScheduleInfoEntity
import java.time.LocalDate

@Dao
interface ScheduleDropDao {

    @Query(
        "DELETE FROM ${ScheduleInfoEntity.TABLE_NAME} " +
                "WHERE ${ScheduleInfoEntity.DATE}=:date"
    )
    suspend fun dropScheduleInfo(date: LocalDate)

    @Query(
        "DELETE FROM ${ScheduleInfoEntity.TABLE_NAME} "
    )
    suspend fun dropSchedule()
}