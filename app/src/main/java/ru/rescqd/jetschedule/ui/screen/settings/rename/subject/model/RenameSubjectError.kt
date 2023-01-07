package ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model

sealed class RenameSubjectError {
    object SubjectNotSelected: RenameSubjectError()
    object Success: RenameSubjectError()
}