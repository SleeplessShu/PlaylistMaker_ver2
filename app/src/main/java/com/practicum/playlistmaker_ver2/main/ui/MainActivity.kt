package com.practicum.playlistmaker_ver2.main.ui


import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
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
        //deleteAllDatabases()
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        navController.addOnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.playlistCreationFragment ||
                    destination.id == R.id.playerFragment) {

                    findViewById<View>(R.id.bottomNavigationView)?.visibility = View.GONE
                    findViewById<View>(R.id.view_line)?.visibility = View.GONE
                }
                else {
                    findViewById<View>(R.id.bottomNavigationView)?.visibility = View.VISIBLE
                    findViewById<View>(R.id.view_line)?.visibility = View.VISIBLE
                }
        }
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.setupWithNavController(navController)

    }
    private fun deleteAllDatabases(){
        this.deleteDatabase("likedTracksDatabase.db")
        this.deleteDatabase("playlistDatabase.db")
        this.deleteDatabase("tracksInPlaylists.db")
    }

}