package com.yunns.calculator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.OnBackPressedDispatcher
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.yunns.calculator.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var tasarim : ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tasarim = ActivityMainBinding.inflate(layoutInflater)
        setContentView(tasarim.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.navHostFragment) as NavHostFragment

        NavigationUI.setupWithNavController(tasarim.navigationView,navHostFragment.navController)

        val toggle = ActionBarDrawerToggle(this,tasarim.drawer,tasarim.toolbar,0,0)

        tasarim.drawer.addDrawerListener(toggle)
        toggle.syncState()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if(tasarim.drawer.isDrawerOpen(GravityCompat.START)){
            tasarim.drawer.closeDrawer(GravityCompat.START)
        }
        else onBackPressedDispatcher.onBackPressed()
    }
}