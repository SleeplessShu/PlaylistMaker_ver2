package com.practicum.playlistmaker_ver2.creator



import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.practicum.playlistmaker_ver2.App
import com.practicum.playlistmaker_ver2.search.data.dto.TracksRepositoryImpl
import com.practicum.playlistmaker_ver2.search.data.network.RetrofitNetworkClient
import com.practicum.playlistmaker_ver2.search.data.repository.ClickedTracksRepositoryImpl
import com.practicum.playlistmaker_ver2.search.domain.api.TrackInteractor
import com.practicum.playlistmaker_ver2.search.domain.api.TracksRepository
import com.practicum.playlistmaker_ver2.search.domain.impl.ClickedTracksInteractorImpl
import com.practicum.playlistmaker_ver2.search.domain.impl.TrackInteractorImpl
import com.practicum.playlistmaker_ver2.search.domain.interactor.ClickedTracksInteractor
import com.practicum.playlistmaker_ver2.search.domain.repository.ClickedTracksRepository
import com.practicum.playlistmaker_ver2.search.domain.api.SearchInteractor
import com.practicum.playlistmaker_ver2.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker_ver2.search.ui.SearchViewModel
import com.practicum.playlistmaker_ver2.settings.data.repositories.ExternalNavigatorRepositoryImpl
import com.practicum.playlistmaker_ver2.settings.data.repositories.SettingsRepositoryImpl
import com.practicum.playlistmaker_ver2.settings.data.repositories.SharingRepositoryImpl
import com.practicum.playlistmaker_ver2.settings.domain.api.SettingsInteractor
import com.practicum.playlistmaker_ver2.settings.domain.api.SharingInteractor
import com.practicum.playlistmaker_ver2.settings.domain.interactors.SettingsInteractorImpl
import com.practicum.playlistmaker_ver2.settings.domain.interactors.SharingInteractorImpl
import com.practicum.playlistmaker_ver2.settings.domain.repositories.ExternalNavigatorRepository
import com.practicum.playlistmaker_ver2.settings.domain.repositories.SettingsRepository
import com.practicum.playlistmaker_ver2.settings.domain.repositories.SharingRepository



object Creator {

    /*
        private const val THEME_STATUS_SHARED_PREFERENCES_KEY: String = "NightMode"
        private const val PREVIOUS_SEARCH_RESULT_KEY: String = "previous_search_result"
    */

    /*private lateinit var application: Application
    fun initApplication(application: Application) {
        Creator.application = application
    }
*//*
    // SEARCH ACTIVITY
    fun provideSearchViewModelFactory(
    ): ViewModelProvider.Factory {
        return SearchViewModel.provideFactory(provideSearchInteractor())
    }*/
    /*
    private fun provideSearchInteractor(): SearchInteractor {

        val sharedPreferences =
            App.appContext.getSharedPreferences(PREVIOUS_SEARCH_RESULT_KEY, Context.MODE_PRIVATE)
        val gson = Gson()

        val tracksInteractor = provideTracksInteractor()
        val clickedTracksInteractor = provideClickedTracksInteractor(sharedPreferences, gson)
        return SearchInteractorImpl(tracksInteractor, clickedTracksInteractor)
    }

*/

    /* private fun provideTracksRepository(): TracksRepository {
         return TracksRepositoryImpl(RetrofitNetworkClient())
     }*/

    /*    private fun provideTracksInteractor(
        ): TrackInteractor {
            val tracksRepository = provideTracksRepository()
            return TrackInteractorImpl(tracksRepository)
        }*/


    // PLAYER ACTIVITY


    //SHARED PREFERENCES
    /*

        fun provideSharedPreferencesManager(appContext: Context): SharedPreferencesManager {
            return SharedPreferencesManager(appContext)
        }
    */

    /*
        private fun provideSharedPreferences(): SharedPreferences {
            return App.appContext.getSharedPreferences(
                THEME_STATUS_SHARED_PREFERENCES_KEY,
                Context.MODE_PRIVATE
            )
        }
    */

    /*
        private fun provideClickedTracksInteractor(
            sharedPreferences: SharedPreferences, gson: Gson
        ): ClickedTracksInteractor {
            return ClickedTracksInteractorImpl(provideClickedTracksRepository(sharedPreferences, gson))
        }
    */

    /*    private fun provideClickedTracksRepository(
            sharedPreferences: SharedPreferences, gson: Gson
        ): ClickedTracksRepository {
            return ClickedTracksRepositoryImpl(sharedPreferences, gson)
        }*/


    //THEME STATUS
    /*fun provideSettingsInteractor(): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository())
    }
*/
    /*private fun provideSettingsRepository(): SettingsRepository {
        return SettingsRepositoryImpl(provideSharedPreferences())
    }*/

    //SHARING
    /*fun provideSharingInteractor(): SharingInteractor {
        return SharingInteractorImpl(provideSharingRepository(), provideExternalNavigator())
    }

    private fun provideSharingRepository(): SharingRepository {
        return SharingRepositoryImpl()
    }

    private fun provideExternalNavigator(): ExternalNavigatorRepository {
        return ExternalNavigatorRepositoryImpl()
    }*/

}