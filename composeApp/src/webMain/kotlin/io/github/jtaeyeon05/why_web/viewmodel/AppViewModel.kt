package io.github.jtaeyeon05.why_web.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


class AppViewModel(initialModel: AppModel = AppModel()) {
    private val _model = MutableStateFlow(initialModel)
    val model: StateFlow<AppModel> = _model

    fun updateTest1(test1: Int) {
        _model.value = _model.value.copy(test1 = test1)
    }

    fun updateTest2(test2: String) {
        _model.value = _model.value.copy(test2 = test2)
    }
}
