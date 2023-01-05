package ru.rescqd.jetschedule.data.wrapper

import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import ru.rescqd.jetschedule.data.container.GenerateDataContainer
import ru.rescqd.jetschedule.data.local.database.dao.ScheduleDropDao
import ru.rescqd.jetschedule.data.local.database.dao.ScheduleSaveDao
import ru.rescqd.jetschedule.data.local.database.entity.*
import ru.rescqd.jetschedule.data.local.source.TeacherFioDataSource
import java.time.LocalDate

class ScheduleManageWrapper(
    private val saveDao: ScheduleSaveDao,
    private val teacherFioDataSource: TeacherFioDataSource,
    private val dropDao: ScheduleDropDao
) {
    companion object {
        private val saveMutex = Mutex()
    }

    suspend fun changeBackendSideEffect() {
        dropDao.dropSchedule()
    }

    suspend fun dropScheduleOnDate(date: LocalDate) = dropDao.dropScheduleInfo(date)

    suspend fun checkExistSchedule(date: LocalDate): Boolean =
        saveDao.checkExistsSchedule(date)

    suspend fun saveScheduleInfo(date: LocalDate, data: List<GenerateDataContainer>) {
        saveMutex.withLock {
            with(saveDao) {
                data.forEach {
                    var groupId = getGroupId(it.group)
                    if (groupId == null) {
                        addGroup(GroupEntity(null, it.group))
                        groupId = getGroupId(it.group)!!
                    }
                    var subjectId = getSubjectId(it.subject)
                    if (subjectId == null) {
                        addSubject(SubjectEntity(null, it.subject))
                        subjectId = getSubjectId(it.subject)!!
                    }
                    var teacherId = getTeacherId(it.teacher)
                    if (teacherId == null) {
                        val teacherFio =
                            teacherFioDataSource.getTeacherFIOByDefaultValue(it.teacher)
                        addTeacher(
                            TeacherEntity(
                                teacherUid = null,
                                teacherDefaultValue = it.teacher,
                                teacherNameValue = teacherFio.name,
                                teacherFamilyValue = teacherFio.family,
                                teacherPatronymicValue = teacherFio.path
                            )
                        )
                        teacherId = getTeacherId(it.teacher)!!
                    }

                    var audienceId = getAudienceId(it.audience.name)
                    if (audienceId == null) {
                        //add floor and corpus
                        addAudience(
                            AudienceEntity(
                                corpusValue = it.audience.corpus,
                                floorValue = it.audience.floor,
                                audienceValue = it.audience.name
                            )
                        )
                        audienceId = getAudienceId(it.audience.name)!!
                    }

                    addScheduleInfo(
                        ScheduleInfoEntity(
                            scheduleInfoUid = null,
                            date = date,
                            subjectUid = subjectId,
                            teacherUid = teacherId,
                            audienceUid = audienceId,
                            groupUid = groupId,
                            pairOrderValue = it.pairOrder
                        )
                    )
                }
            }
        }
    }
}