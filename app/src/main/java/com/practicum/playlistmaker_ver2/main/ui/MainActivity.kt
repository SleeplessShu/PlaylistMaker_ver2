package com.practicum.playlistmaker_ver2.main.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.navigation.fragment.NavHostFragment
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //deleteAllDatabases()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController

        binding.bottomNavigationView.selectedItemId = R.id.mediatekaFragment

        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setOnItemSelectedListener { item ->
            val currentDestination = navController.currentDestination?.id

            when (item.itemId) {
                R.id.mediatekaFragment -> {
                    if (currentDestination != R.id.mediatekaFragment) {
                        if (currentDestination == R.id.settingsFragment) {
                            navController.navigate(
                                R.id.mediatekaFragment, null, getNavOptions(false)
                            )
                        } else {
                            navController.navigate(
                                R.id.mediatekaFragment, null, getNavOptions(true)
                            )
                        }
                    }
                    true
                }

                R.id.searchFragment -> {
                    if (currentDestination != R.id.searchFragment) {
                        navController.navigate(R.id.searchFragment, null, getNavOptions(false))
                    }
                    true
                }

                R.id.settingsFragment -> {
                    if (currentDestination != R.id.settingsFragment) {
                        navController.navigate(R.id.settingsFragment, null, getNavOptions(true))
                    }
                    true
                }

                else -> false
            }
        }

        navController.addOnDestinationChangedListener { _, destination, _ ->
            bottomNavigationVisible(
                !(destination.id == R.id.playlistCreationFragment || destination.id == R.id.playerFragment)
            )
        }

    }

    private fun bottomNavigationVisible(isVisible: Boolean) {
            findViewById<View>(R.id.bottomNavigationView)?.isVisible = isVisible
            findViewById<View>(R.id.view_line)?.isVisible = isVisible
    }


    private fun getNavOptions(slideRight: Boolean): androidx.navigation.NavOptions {
        when (slideRight) {
            true -> {
                return androidx.navigation.NavOptions.Builder().setEnterAnim(R.anim.slide_in_right)
                    .setExitAnim(R.anim.slide_out_left).setPopEnterAnim(R.anim.slide_in_left)
                    .setPopExitAnim(R.anim.slide_out_right).build()
            }

            false -> {
                return androidx.navigation.NavOptions.Builder().setEnterAnim(R.anim.slide_in_left)
                    .setExitAnim(R.anim.slide_out_right).setPopEnterAnim(R.anim.slide_in_right)
                    .setPopExitAnim(R.anim.slide_out_left).build()
            }
        }
    }

    private fun deleteAllDatabases() {
        this.deleteDatabase("likedTracksDatabase.db")
        this.deleteDatabase("playlistDatabase.db")
        this.deleteDatabase("tracksInPlaylists.db")
    }

}