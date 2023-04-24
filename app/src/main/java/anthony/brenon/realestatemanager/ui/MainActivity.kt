package anthony.brenon.realestatemanager.ui


import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.RealEstateManagerApp
import anthony.brenon.realestatemanager.databinding.ActivityMainBinding
import anthony.brenon.realestatemanager.utils.EstateStatus
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as RealEstateManagerApp).agentRepository, (application as RealEstateManagerApp).estateRepository)
    }
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle

    //TODO implement content provider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        initDrawer()
    }

    private fun initDrawer() {
        drawerLayout = binding.myDrawerLayout
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        val header: View = binding.headerNavigationView.getHeaderView(0)
        viewModel.agentSelected.observe(this) {
            val name: TextView = header.findViewById(R.id.name_header)
            val email: TextView = header.findViewById(R.id.email_header)
            name.text = it.nameAgent
            email.text = it.emailAgent
        }

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        drawerMenuListener()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    // toolbar listener
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true
        }
        return when (item.itemId) {
            R.id.item_search -> {
                Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_filter_fragment)
                return true
            }
            R.id.item_add -> {
                if (viewModel.agentIsConnected()) {
                    viewModel.selectThisEstateStatus(EstateStatus.ADD_NEWS_ESTATE)
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment))
                        .navigate(R.id.item_add_fragment)
                } else Snackbar.make(binding.root, "You need to be logged in for create an estate", Snackbar.LENGTH_SHORT).show()
                return true
            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    private fun drawerMenuListener() {
        binding.headerNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_settings -> {
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_settings_fragment)
                    drawerLayout.close()
                }
                R.id.item_login -> {
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_connect_fragment)
                    drawerLayout.close()
                }
                R.id.item_exit -> finishAndRemoveTask()
            }
            true
        }
    }
}