package com.ad4th.seoulandroid.ui.vote

import android.arch.lifecycle.ViewModel;

class VoteViewModel : ViewModel() {
    // TODO: Implement the ViewModel
//    val selected = MutableList<Data>()

    fun select(item: Data) {
 //       selected.value = item
    }

    inner class Data{
        var a: Int = 0
    }
}

