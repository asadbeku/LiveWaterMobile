package uz.prestige.livewater

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navGraphViewModels
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import uz.prestige.livewater.auth.TokenManager

class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNav.menu.clear()
        val role = TokenManager.getRole(context = applicationContext)
        val profileType = TokenManager.getProfileType(context = applicationContext)

        val navGraph: NavGraph = when (profileType) {
            "level" -> navController.navInflater.inflate(R.navigation.nav_graph_level)
            "dayver" -> navController.navInflater.inflate(R.navigation.nav_graph_dayver)
            else -> throw IllegalArgumentException("Unsupported profile type: $profileType")
        }


        // Set the nav graph on the NavController
        navController.graph = navGraph


        if (role == "admin") {
            bottomNav.inflateMenu(R.menu.bottom_navigation_menu_admin)
        } else {
            bottomNav.inflateMenu(R.menu.bottom_navigation_menu_operator)
        }

        setupWithNavController(bottomNav, navController)
    }
}