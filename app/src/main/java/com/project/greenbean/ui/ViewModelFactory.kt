package com.project.greenbean.ui

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.project.greenbean.data.Repository
import com.project.greenbean.di.Injection
import com.project.greenbean.ui.auth.AuthViewModel
import com.project.greenbean.ui.community.CommunityViewModel
import com.project.greenbean.ui.home.HomeViewModel
import com.project.greenbean.ui.profile.ProfileViewModel
import com.project.greenbean.ui.verifikasi.VerifikasiViewModel

class ViewModelFactory private constructor(private val repository: Repository): ViewModelProvider.NewInstanceFactory(){


    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(repository) as T
            }
            modelClass.isAssignableFrom(AuthViewModel::class.java) -> {
                AuthViewModel(repository) as T
            }
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(repository) as T

            }modelClass.isAssignableFrom(CommunityViewModel::class.java) -> {
                CommunityViewModel(repository) as T

            }modelClass.isAssignableFrom(VerifikasiViewModel::class.java) -> {
                VerifikasiViewModel(repository) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }


    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (instance == null) {
                synchronized(ViewModelFactory::class.java) {
                    instance = ViewModelFactory(Injection.provideRepository(context))
                }
            }
            return instance as ViewModelFactory
        }
    }

}