package xyz.orangej.acmsigninsystemandroid.ui.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import xyz.orangej.acmsigninsystemandroid.R

/**
 * 设置Activity，逻辑见Fragment。
 *
 * @see SettingsFragment
 */
class SettingsActivity : AppCompatActivity() {

    private lateinit var viewModel: SettingsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        this.viewModel = ViewModelProvider(this)[SettingsActivityViewModel::class.java]

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}