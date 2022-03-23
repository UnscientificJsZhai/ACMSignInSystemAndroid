package xyz.orangej.acmsigninsystemandroid.ui.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import dagger.hilt.android.AndroidEntryPoint
import xyz.orangej.acmsigninsystemandroid.R

/**
 * 设置Activity，逻辑见Fragment。
 *
 * @see SettingsFragment
 */
@AndroidEntryPoint
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == android.R.id.home) {
            finish()
            true
        } else super.onOptionsItemSelected(item)
    }
}