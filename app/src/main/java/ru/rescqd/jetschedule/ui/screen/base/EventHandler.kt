package ru.rescqd.jetschedule.ui.screen.base

interface  EventHandler<T> {
    fun  obtainEvent(event: T)
}