package com.arrazyfathan.customnavigationfragment

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {

    val fragmentFlow: LiveData<Pair<MainViewFragmentId, Boolean>> get() = _fragmentFlow
    private val _fragmentFlow = MutableLiveData<Pair<MainViewFragmentId, Boolean>>(Pair(MainViewFragmentId.One, true))

    fun setPage(pageId: MainViewFragmentId, push: Boolean) {
        _fragmentFlow.postValue(Pair(pageId, push))
    }
}

sealed class MainViewFragmentId(key: Int) {
    var KEY = 0

    init {
        KEY = key
    }

    data object One : MainViewFragmentId(0)
    data object Two : MainViewFragmentId(1)
    data object Three : MainViewFragmentId(2)
}
