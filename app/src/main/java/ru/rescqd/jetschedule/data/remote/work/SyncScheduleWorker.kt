package ru.rescqd.jetschedule.data.remote.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.data.Contains
import ru.rescqd.jetschedule.data.local.repository.UserPreferencesRepository
import ru.rescqd.jetschedule.data.parser.GenerateDataParser
import ru.rescqd.jetschedule.data.remote.network.base.CollegeApi
import ru.rescqd.jetschedule.data.remote.network.entity.BackParamRequestEntity
import ru.rescqd.jetschedule.data.remote.network.entity.CheckFileRequestEntity
import ru.rescqd.jetschedule.data.remote.network.entity.GenerateDataRequestEntity
import ru.rescqd.jetschedule.data.wrapper.ScheduleManageWrapper
import ru.rescqd.jetschedule.data.utils.toApiDate
import ru.rescqd.jetschedule.ui.theme.ScheduleBackendProvider
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset


@OptIn(ExperimentalSerializationApi::class)
@HiltWorker
class SyncScheduleWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParameters: WorkerParameters,
    userPreferencesRepository: UserPreferencesRepository,
    private val saveUseCase: ScheduleManageWrapper,
    private val api: CollegeApi,
) : CoroutineWorker(context, workerParameters) {
    private val thresholdFlow =
        userPreferencesRepository.getPeriodicWorkThreshold(Contains.INVALID_LONG)
    private val dateOffset =
        userPreferencesRepository.getPeriodicWorkDateOffset(Contains.INVALID_LONG)

    private suspend fun syncOnDateByTeachers(date: LocalDate): Result {
        if (saveUseCase.checkExistSchedule(date)) return Result.failure()
        val dateApi = date.toApiDate
        if (api.checkFile(CheckFileRequestEntity(dateApi)) == CheckFileRequestEntity.ERR) return Result.failure()
        val bp = api.backParam(BackParamRequestEntity(dateApi, BackParamRequestEntity.TEACHER))
        // bp  (key == value) what?
        //TODO(implement optimization to len bp)
        val job = CoroutineScope(currentCoroutineContext()).launch {
            for (key in bp.keys()) {
                //TODO(implement optimization if schedule sync for key)
                launch {
                    val parsedData = GenerateDataParser.parse(
                        api.generateData(
                            GenerateDataRequestEntity(
                                dateApi, GenerateDataRequestEntity.TEACHER, key
                            )
                        ), GenerateDataParser.RawDataType.TEACHER, bp.getString(key)
                    )
                    saveUseCase.saveScheduleInfo(date, parsedData)
                }
            }
        }
        job.join()
        return Result.success()
    }

    private suspend fun syncOnDateByGroups(date: LocalDate): Result {
        if (saveUseCase.checkExistSchedule(date)) return Result.failure()
        val dateApi = date.toApiDate
        if (api.checkFile(CheckFileRequestEntity(dateApi)) == CheckFileRequestEntity.ERR) return Result.failure()
        val bp = api.backParam(BackParamRequestEntity(dateApi, BackParamRequestEntity.GROUP))
        //TODO(implement optimization to len bp)
        val coroutineScope = CoroutineScope(currentCoroutineContext())
        val job = coroutineScope.launch(Dispatchers.IO) {
            for (key in bp.keys()) {
                //TODO(implement optimization if schedule sync for key)
                launch(Dispatchers.IO) {
                    val parsedData = GenerateDataParser.parse(
                        html = api.generateData(
                            GenerateDataRequestEntity(
                                dateApi, GenerateDataRequestEntity.GROUP, key
                            )
                        ), GenerateDataParser.RawDataType.GROUP, bp.getString(key)
                    )
                    saveUseCase.saveScheduleInfo(date, parsedData)
                }
            }
        }
        job.join()
        return Result.success()
    }

    private val backend = userPreferencesRepository.getScheduleBackendProvider()

    private suspend fun oneTimeWork(date: LocalDate): Result {
        mutex.withLock {
            Log.e(Contains.WORK_DEBUG, "sync start on ${date.toApiDate}")
            val time = LocalDateTime.now()
            try {
                when (backend.first()) {
                    ScheduleBackendProvider.TEACHER -> syncOnDateByTeachers(
                        date
                    )
                    ScheduleBackendProvider.GROUP -> syncOnDateByGroups(
                        date
                    )
                }
            } catch (e: Throwable){
                val outputData = workDataOf(Contains.WORK_FAILED_ERROR_KEY to e.message)
                saveUseCase.dropScheduleOnDate(date)
                return Result.failure(outputData)
            }
            Log.e(
                Contains.WORK_DEBUG,
                "sync stop on ${date.toApiDate} " + (LocalDateTime.now()
                    .toEpochSecond(ZoneOffset.UTC) - time.toEpochSecond(
                    ZoneOffset.UTC
                )).toString()
            )
            return Result.success()
        }
    }

    override suspend fun doWork(): Result {
        val date = LocalDate.ofEpochDay(inputData.getLong(Contains.WORK_DATE_KEY, 0L))
        Log.e(Contains.WORK_DEBUG, "work on ${date.toApiDate}")
        return if (date != LocalDate.ofEpochDay(0L)) {
            oneTimeWork(date)
        } else {
            periodicTimeWork()
        }

    }

    private suspend fun periodicTimeWork(): Result {
        val todayOffset = LocalDate.now().toEpochDay() + dateOffset.first()
        (todayOffset..todayOffset + thresholdFlow.first()).forEach {
            oneTimeWork(LocalDate.ofEpochDay(it))
        }
        return Result.success()
    }

    companion object {
        private val mutex = Mutex()
    }
}