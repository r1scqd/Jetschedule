package ru.rescqd.jetschedule.data.container

import androidx.room.ColumnInfo
import ru.rescqd.jetschedule.data.local.database.entity.AudienceEntity

data class AudienceContainer(
    @ColumnInfo(name = AudienceEntity.AUDIENCE_VALUE)
    val audience: String,
    @ColumnInfo(name = AudienceEntity.CORPUS_VALUE)
    val corpus: Int,
    @ColumnInfo(name = AudienceEntity.FLOOR_VALUE)
    val floor: Int,
)
