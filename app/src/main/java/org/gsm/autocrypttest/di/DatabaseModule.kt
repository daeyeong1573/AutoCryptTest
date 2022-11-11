package org.gsm.autocrypttest.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.gsm.autocrypttest.data.local.CentersDatabase
import org.gsm.autocrypttest.util.Constants
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context) =
        Room.databaseBuilder(
            context,
            CentersDatabase::class.java,
            Constants.DATABASE_NAME
        ).fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideCenterDao(appDatabase: CentersDatabase) = appDatabase.centerDao()
}

