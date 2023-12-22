package com.project.greenbean.di

import android.content.Context
import com.project.greenbean.data.Repository
import com.project.greenbean.data.pref.UserPreference
import com.project.greenbean.data.pref.dataStore
import com.project.greenbean.data.retrofit.ApiConfig
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): Repository {
        val pref = UserPreference.getInstance(context.dataStore)
        val userToken = runBlocking { pref.getSession().first() }
        val apiService = ApiConfig.retrofitInstance(userToken)
        val apiServiceWithoutHeader = ApiConfig.retrofitInstanceNoHeader()
        return Repository.getInstance(apiService, pref, apiServiceWithoutHeader)
    }
}