package com.example.howareyou.views.home

import androidx.databinding.ObservableBoolean
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import com.example.howareyou.repository.HomeRepository

class HomeViewModel @ViewModelInject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    val initLoading = ObservableBoolean()

    init {
        initLoading.set(true)
    }

}