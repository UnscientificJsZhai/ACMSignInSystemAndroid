package xyz.orangej.acmsigninsystemandroid.ui.settings

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import xyz.orangej.acmsigninsystemandroid.R
import javax.inject.Inject

/**
 * 设置Activity，逻辑见Fragment。
 *
 * @see SettingsFragment
 */
class SettingsActivity : AppCompatActivity() {

    @Inject
    lateinit var viewModel: SettingsActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

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