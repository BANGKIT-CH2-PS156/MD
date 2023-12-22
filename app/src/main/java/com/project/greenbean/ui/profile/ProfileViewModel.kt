package com.project.greenbean.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.project.greenbean.data.Repository
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository): ViewModel() {
        fun getUser() = repository.getUser()

        fun logout() {
                viewModelScope.launch {
                        repository.logout()
                }
        }

        fun updateProfile(
                name: String,
                email: String,
                address: String,
                phone: String,
        ) = repository.updateProfile(name, email, address, phone)
}