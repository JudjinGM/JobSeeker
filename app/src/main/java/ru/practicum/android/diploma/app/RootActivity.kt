package ru.practicum.android.diploma.app

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import ru.practicum.android.diploma.R
import ru.practicum.android.diploma.databinding.ActivityRootBinding

class RootActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRootBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRootBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navigationView: BottomNavigationView = binding.navigationView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.rootFragmentContainerView) as NavHostFragment
        val navController = navHostFragment.navController

        navigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment,
                R.id.favoritesFragment,
                R.id.teamFragment -> {
                    if (binding.navigationView.visibility == View.GONE) {
                        binding.navigationView.visibility = View.VISIBLE
                        binding.divider.visibility = View.VISIBLE
                        binding.navigationView.animation = AnimationUtils.loadAnimation(this, R.anim.slide_in_up)
                        binding.navigationView.animate()
                    }
                }

                else -> {
                    if (binding.navigationView.visibility == View.VISIBLE) {
                        binding.navigationView.animation = AnimationUtils.loadAnimation(this, R.anim.slide_out_down)
                        binding.navigationView.animate()
                        binding.navigationView.visibility = View.GONE
                        binding.divider.visibility = View.GONE
                    }
                }
            }
        }
    }
}