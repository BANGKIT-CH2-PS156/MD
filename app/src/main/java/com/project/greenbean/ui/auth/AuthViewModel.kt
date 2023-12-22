package com.project.greenbean.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.project.greenbean.data.Repository
import kotlinx.coroutines.launch

class AuthViewModel(private val repository: Repository): ViewModel() {

        fun login(email: String, password: String) = repository.login(email, password)

        suspend fun saveSession(token: String) {
                repository.saveSession(token)
        }

        fun getSession() : LiveData<String> {
                return repository.getSession().asLiveData()
        }

        fun register(email: String, password: String, confirmPassword: String) = repository.register(email, password, confirmPassword)
}