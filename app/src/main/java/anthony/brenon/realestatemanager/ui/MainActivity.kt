package anthony.brenon.realestatemanager.ui


import android.Manifest
import android.content.pm.ActivityInfo
import android.os.Build
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
import anthony.brenon.realestatemanager.utils.RequestCodes.PERMISSIONS_REQUEST_CODE
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class MainActivity : AppCompatActivity(),
    EasyPermissions.PermissionCallbacks {

    private lateinit var binding: ActivityMainBinding
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            (application as RealEstateManagerApp).agentRepository,
            (application as RealEstateManagerApp).estateRepository
        )
    }
    private val requiredPermissions = mutableListOf(Manifest.permission.CAMERA)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            requiredPermissions.add(Manifest.permission.READ_EXTERNAL_STORAGE)
        } else {
            requiredPermissions.add(Manifest.permission.READ_MEDIA_IMAGES)
        }

        if (!hasAllPermissions()) requestPermissions()
        else viewModel.hasAllPermissions = true

        viewModel.allEstates.observe(this) { viewModel.updateSortListEstate(it) }

        initializeDrawer()
    }

    private fun initializeDrawer() {
        drawerLayout = binding.myDrawerLayout
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close)

        val header: View = binding.headerNavigationView.getHeaderView(0)
        val name: TextView = header.findViewById(R.id.name_header)
        val email: TextView = header.findViewById(R.id.email_header)

        viewModel.agentSelected.observe(this) {
            name.text = it?.nameAgent
            email.text = it?.emailAgent
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
                Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment))
                    .navigate(R.id.item_filter_fragment)
                return true
            }

            R.id.item_map -> {
                Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment))
                    .navigate(R.id.item_map_fragment)
                return true
            }

            R.id.item_add -> {
                if (viewModel.agentIsConnected()) {
                    viewModel.isNewEstate = true
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment))
                        .navigate(R.id.item_add_fragment)
                } else Snackbar.make(
                    binding.root,
                    getString(R.string.you_need_to_be_logged_in_for_create_an_estate),
                    Snackbar.LENGTH_SHORT
                ).show()
                return true
            }

            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

    // drawer listener
    private fun drawerMenuListener() {
        binding.headerNavigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_simulator -> {
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment))
                        .navigate(R.id.item_loan_simulator_fragment)
                    drawerLayout.close()
                }

                R.id.item_settings -> {
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment))
                        .navigate(R.id.item_settings_fragment)
                    drawerLayout.close()
                }

                R.id.item_login -> {
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment))
                        .navigate(R.id.item_connect_fragment)
                    drawerLayout.close()
                }

                R.id.item_exit -> finishAndRemoveTask()
            }
            true
        }
    }

    // PERMISSIONS
    private fun hasAllPermissions(): Boolean {
        return requiredPermissions.all {
            EasyPermissions.hasPermissions(this, it)
        }
    }

    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            EasyPermissions.requestPermissions(
                this,
                "These permissions are required for this application",
                PERMISSIONS_REQUEST_CODE,
                *requiredPermissions.toTypedArray()
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "These permissions are required for this application",
                PERMISSIONS_REQUEST_CODE,
                Manifest.permission.CAMERA,
                Manifest.permission.READ_MEDIA_IMAGES
            )
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        if (requestCode == PERMISSIONS_REQUEST_CODE && perms.containsAll(requiredPermissions.toList())) {
            // Handle the case when all required permissions are granted
            viewModel.hasAllPermissions = true
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }
}