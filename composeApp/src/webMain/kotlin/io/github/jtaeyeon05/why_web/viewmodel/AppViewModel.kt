package io.github.jtaeyeon05.why_web.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AppViewModel(initialModel: AppModel = AppModel()) {
    private val _model = MutableStateFlow(initialModel)
    val model: StateFlow<AppModel> = _model

    fun updateAvatar(avatar: Int?) {
        _model.value = _model.value.copy(avatar = avatar)
    }

    fun updateName(name: Int?) {
        _model.value = _model.value.copy(name = name)
    }
}
