package ru.rescqd.jetschedule.ui.screen.settings.rename.subject.model

sealed class RenameSubjectViewState{
    object Loading: RenameSubjectViewState()

    data class DisplaySubjectSelect(
        val subjects: List<SubjectModel>,
        val currentSubject: SubjectModel? = null,
        val sendingError: RenameSubjectError? = null
    ): RenameSubjectViewState()
}
