package com.frnd.frndcalendar.di

import com.frnd.frndcalendar.calendar.data.CalendarRepository
import com.frnd.frndcalendar.calendar.data.CalendarRepositoryImpl
import com.frnd.frndcalendar.remote.repository.TaskRemoteRepository
import com.frnd.frndcalendar.remote.repository.TaskRemoteRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
interface RepositoryModule {
    @Binds
    fun bindCalendarRepository(repository: CalendarRepositoryImpl): CalendarRepository

    @Binds
    fun bindTaskRemoteRepository(repository: TaskRemoteRepositoryImpl): TaskRemoteRepository
}