package ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model

sealed class RenameSubjectEvent {
    object EnterScreen : RenameSubjectEvent()
    object ConfirmClicked : RenameSubjectEvent()

    data class SubjectDisplayNameChange(val newDisplayName: String) : RenameSubjectEvent()
    data class SubjectSelected(val subjectModel: SubjectModel): RenameSubjectEvent()
}
