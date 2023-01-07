package ru.rescqd.jetschedule.data.local.source

import androidx.work.*
import kotlinx.coroutines.flow.first
import kotlinx.serialization.ExperimentalSerializationApi
import ru.rescqd.jetschedule.data.Contains
import ru.rescqd.jetschedule.data.local.repository.UserPreferencesRepository
import ru.rescqd.jetschedule.data.remote.work.SyncScheduleWorker
import ru.rescqd.jetschedule.data.utils.toApiDate
import java.time.Duration
import java.time.LocalDate

@OptIn(ExperimentalSerializationApi::class)
class ScheduleRemoteDataSource(
    private val workManager: WorkManager,
    userPreferencesRepository: UserPreferencesRepository
) {

    fun syncOnDate(date: LocalDate): WorkContinuation {
       val query = workManager.beginUniqueWork(
            Contains.WORK_SYNC_UNIQUE_NAME + date.toApiDate,
            ExistingWorkPolicy.KEEP,
            workRequest<SyncScheduleWorker>(workDataOf(Contains.WORK_DATE_KEY to date.toEpochDay()))
        )
        query.enqueue()
        return query
    }

    private val repeatIntervalFlow = userPreferencesRepository.getPeriodicWorkRepeatInterval(Duration.ZERO)
    private val flexIntervalFlow = userPreferencesRepository.getPeriodicWorkFlexInterval(Duration.ZERO)
    private val isEnabled = userPreferencesRepository.getIsPeriodicWorkEnabled()

    suspend fun reloadPeriodicWmConfiguration() {
        if (isEnabled.first()) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val work = PeriodicWorkRequestBuilder<SyncScheduleWorker>(
                repeatIntervalFlow.first(),
                flexIntervalFlow.first()
            )
                .setConstraints(constraints)
                .build()
            workManager.cancelUniqueWork(Contains.WORK_PERIODIC_SYNC_UUID)
            workManager.enqueueUniquePeriodicWork(
                Contains.WORK_PERIODIC_SYNC_UUID,
                ExistingPeriodicWorkPolicy.KEEP,
                work
            )
        } else {
            workManager.cancelUniqueWork(Contains.WORK_PERIODIC_SYNC_UUID)
        }
    }
    /**
     * Creates a [OneTimeWorkRequest] with the given inputData and a [tag] if set.
     */
    private inline fun <reified T : ListenableWorker> workRequest(
        inputData: Data,
        tag: String? = null,
    ) =
        OneTimeWorkRequestBuilder<T>().apply {
            setInputData(inputData)
            if (!tag.isNullOrEmpty()) {
                addTag(tag)
            }
        }.build()
}