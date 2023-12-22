package com.project.greenbean.ui.home

import androidx.lifecycle.ViewModel
import com.project.greenbean.data.Repository

class HomeViewModel(private val repository: Repository): ViewModel() {

    fun getUser() = repository.getUser()


}