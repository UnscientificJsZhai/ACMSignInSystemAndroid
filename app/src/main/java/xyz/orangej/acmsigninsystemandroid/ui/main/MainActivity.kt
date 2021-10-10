package xyz.orangej.acmsigninsystemandroid.ui.main

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import xyz.orangej.acmsigninsystemandroid.R
import xyz.orangej.acmsigninsystemandroid.SystemApplication
import xyz.orangej.acmsigninsystemandroid.databinding.ActivityMainBinding
import xyz.orangej.acmsigninsystemandroid.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var systemApplication: SystemApplication

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        this.systemApplication = application as SystemApplication
        val dao = systemApplication.getDatabase().userDao()
        val hash = systemApplication.session.hashCode()
        this.viewModel = ViewModelProvider(
            this,
            MainActivityViewModel.Factory(dao, hash)
        )[MainActivityViewModel::class.java]

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        viewModel.viewModelScope.launch {
            val user = viewModel.getCurrentUser(systemApplication.session)
            if (user == null) {
                Toast.makeText(this@MainActivity, "登录过期", Toast.LENGTH_SHORT).show()
                systemApplication.session = ""
                startActivity(Intent(this@MainActivity, LoginActivity::class.java))
                finish()
            } else {
                withContext(Dispatchers.IO) {
                    dao.addCurrentUser(user)
                }
            }
        }
    }
}