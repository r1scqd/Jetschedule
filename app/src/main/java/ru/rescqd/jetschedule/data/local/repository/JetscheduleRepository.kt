package ru.rescqd.jetschedule.data.local.repository

import kotlinx.coroutines.flow.Flow
import ru.rescqd.jetschedule.data.container.AudienceContainer
import ru.rescqd.jetschedule.data.container.OnePairInfoContainer
import ru.rescqd.jetschedule.data.local.database.dao.CustomSelectDao
import ru.rescqd.jetschedule.data.local.database.dao.ScheduleDropDao
import ru.rescqd.jetschedule.data.local.database.entity.*
import ru.rescqd.jetschedule.data.local.source.ScheduleRemoteDataSource
import java.time.LocalDate

@Suppress("unused")
class JetscheduleRepository(
    private val dropDao: ScheduleDropDao,
    private val customSelectDao: CustomSelectDao,
    private val scheduleRemoteDataSource: ScheduleRemoteDataSource
) {


    // ////////////////////////////////////////////////
    // remote
    // ////////////////////////////////////////////////

    fun syncOnDate(date: LocalDate) = scheduleRemoteDataSource.syncOnDate(date)

    suspend fun reloadScheduleAutoDownload() =
        scheduleRemoteDataSource.reloadPeriodicWmConfiguration()

    // ////////////////////////////////////////////////
    // delete
    // ////////////////////////////////////////////////

    suspend fun dropScheduleOnDate(date: LocalDate) {
        dropDao.dropScheduleInfo(date)
    }

    //////////////////////////////////////////////////
    //get
    //////////////////////////////////////////////////


    fun getNextPairDatesWithSubject(
        pairId: Long,
        currentDate: LocalDate,
        groupName: String
    ): Flow<List<LocalDate>> =
        customSelectDao.getNextPairDatesWithSubjectToGroup(pairId, currentDate, groupName)

    fun getPrevPairDatesWithSubject(
        pairId: Long,
        currentDate: LocalDate,
        groupName: String
    ): Flow<List<LocalDate>> =
        customSelectDao.getPrevPairDatesWithSubjectToGroup(pairId, currentDate, groupName)

    suspend fun getPairMoreInfo(pairUid: Long): OnePairInfoContainer =
        customSelectDao.getPairInfo(pairUid)

    fun getAllTeacher(): Flow<List<String>> = customSelectDao.getAllTeacher()

    fun getAllGroups(): Flow<List<String>> = customSelectDao.getAllGroup()


    fun getDefaultScheduleOnDate(
        teacher: String,
        date: LocalDate
    ): Flow<List<OnePairInfoContainer>> {
        return customSelectDao.getScheduleToTeacher(date, teacher)
    }


    fun getDefaultScheduleOnDate(
        date: LocalDate,
        groupName: String
    ): Flow<List<OnePairInfoContainer>> {
        return customSelectDao.getScheduleToGroup(date, groupName)
    }

    fun getSynchronizedSchedule(): Flow<List<LocalDate>> =
        customSelectDao.getSynchronizedScheduleDate()

    fun getBusyAudience(corpus: Int, date: LocalDate): Flow<List<AudienceContainer>> =
        customSelectDao.getBusyAudience(corpus, date)

    fun getAllAudience(): Flow<List<AudienceContainer>> = customSelectDao.getAllAudience()

    fun getAllAudience(corpus: Int): Flow<List<AudienceContainer>> =
        customSelectDao.getAllAudience(corpus)

    fun getBusyAudience(
        corpus: Int,
        date: LocalDate,
        pairOrder: Int
    ): Flow<List<AudienceContainer>> = customSelectDao.getBusyAudience(corpus, date, pairOrder)

    fun getAvailableAudience(corpus: Int, date: LocalDate): Flow<List<AudienceContainer>> =
        customSelectDao.getAvailableAudience(corpus, date)

    fun getAvailableAudience(
        corpus: Int,
        date: LocalDate,
        pairOrder: Int
    ): Flow<List<AudienceContainer>> = customSelectDao.getAvailableAudience(corpus, date, pairOrder)
}