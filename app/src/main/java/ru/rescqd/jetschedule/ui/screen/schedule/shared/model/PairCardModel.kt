package ru.rescqd.jetschedule.ui.screen.schedule.shared.model

data class PairCardModel(
    val id: Long,
    val subject: String,
    val pairOrder: Int,
    val teacher: String,
    val audience: String,
    val pairStartTime: String = "not impl",
    val pairStopTime: String = "not impl"
)
