package com.practicum.playlistmaker_ver2.main.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.practicum.playlistmaker_ver2.R
import com.practicum.playlistmaker_ver2.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //this.deleteDatabase("liked_tracks.db")
        //this.deleteDatabase("database.db")
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener {_, destination, _ ->
            findViewById<View>(R.id.bottomNavigationView)?.visibility =
                if (destination.id == R.id.playlistCreationFragment) View.GONE else View.VISIBLE
        }
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

    }

}