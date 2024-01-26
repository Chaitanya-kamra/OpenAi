package com.chaitanya.openaiassignment.ui.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.chaitanya.openaiassignment.R
import com.chaitanya.openaiassignment.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
        binding.bottomNavigation.setOnItemSelectedListener { menuItem ->
            when(menuItem.itemId){
                R.id.linksMenu -> navController.navigate(R.id.dashboardFragment)
                R.id.coursesMenu -> navController.navigate(R.id.coursesFragment2)
                R.id.campaignsMenu -> navController.navigate(R.id.campaignsFragment2)
                R.id.profileMenu -> navController.navigate(R.id.profileFragment2)
            }
            true
        }
        navController.addOnDestinationChangedListener { _, destination, _ ->

            when(destination.id){
                R.id.dashboardFragment -> binding.bottomNavigation.menu.findItem(R.id.linksMenu)?.isChecked = true
                R.id.coursesFragment2 -> binding.bottomNavigation.menu.findItem(R.id.campaignsMenu)?.isChecked = true
                R.id.campaignsFragment2 -> binding.bottomNavigation.menu.findItem(R.id.campaignsMenu)?.isChecked = true
                R.id.profileFragment2 -> binding.bottomNavigation.menu.findItem(R.id.profileMenu)?.isChecked = true
            }
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}