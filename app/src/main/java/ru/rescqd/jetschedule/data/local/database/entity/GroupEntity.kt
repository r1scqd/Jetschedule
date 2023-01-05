package ru.rescqd.jetschedule.data.local.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = GroupEntity.TABLE_NAME,
    indices = [
        Index(value = arrayOf(GroupEntity.GROUP_VALUE), unique = true)
    ]
)
data class GroupEntity(
    @ColumnInfo(name = GROUP_UID)
    @PrimaryKey(autoGenerate = true)
    val groupUid: Long? = null,
    @ColumnInfo(name = GROUP_VALUE)
    val groupName: String,
) {
    companion object {
        const val TABLE_NAME = "group_table"
        const val GROUP_UID = "group_id"
        const val GROUP_VALUE = "group_value"
    }
}