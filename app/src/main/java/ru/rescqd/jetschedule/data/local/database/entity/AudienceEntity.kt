package ru.rescqd.jetschedule.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = AudienceEntity.TABLE_NAME,
    indices = [
        Index(value = [AudienceEntity.AUDIENCE_VALUE], unique = true)
    ]
)
data class AudienceEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = AUDIENCE_UID)
    val audienceUid: Long? = null,
    @ColumnInfo(name = AUDIENCE_VALUE) val audienceValue: String,
    @ColumnInfo(name = CORPUS_VALUE) val corpusValue: Int = -1,
    @ColumnInfo(name = FLOOR_VALUE) val floorValue: Int = -1,
){
    companion object{
        const val AUDIENCE_UID = "audience_uid"
        const val AUDIENCE_VALUE = "AUDIENCE_VALUE"
        const val CORPUS_VALUE = "CORPUS_VALUE"
        const val TABLE_NAME = "AUDIENCE_TABLE"
        const val FLOOR_VALUE = "FLOOR_VALUE"
    }
}
