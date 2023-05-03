package anthony.brenon.realestatemanager.ui


import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
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
import anthony.brenon.realestatemanager.callback.CallbackLocation
import anthony.brenon.realestatemanager.databinding.ActivityMainBinding
import anthony.brenon.realestatemanager.utils.Code
import com.google.android.material.snackbar.Snackbar
import com.vmadalin.easypermissions.EasyPermissions
import com.vmadalin.easypermissions.dialogs.SettingsDialog

class MainActivity : AppCompatActivity(),
    CallbackLocation,
    EasyPermissions.PermissionCallbacks
{

    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private lateinit var locationListener: LocationListener
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var actionBarDrawerToggle: ActionBarDrawerToggle
    private val viewModel: MainViewModel by viewModels {
        MainViewModelFactory(
            (application as RealEstateManagerApp).agentRepository,
            (application as RealEstateManagerApp).estateRepository)
    }

    //TODO implement content provider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeLocationManger()
        if (!hasPermission()) requestPermissions()
        else viewModel.hasAllPermissions = true

        viewModel.allEstates.observe(this) { viewModel.updateSortListEstate(it) }

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

            R.id.item_map -> {
                startLocationListener()
                Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_map_fragment)
                return true
            }

            R.id.item_add -> {
                if (viewModel.agentIsConnected()) {
                    viewModel.isNewEstate = true
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_add_fragment)
                } else Snackbar.make(binding.root, getString(R.string.you_need_to_be_logged_in_for_create_an_estate), Snackbar.LENGTH_SHORT).show()
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
                R.id.item_simulator -> {
                    Navigation.findNavController(binding.root.findViewById(R.id.nav_host_fragment)).navigate(R.id.item_loan_simulator_fragment)
                    drawerLayout.close()
                }
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

    // Initialize the LocationManager and LocationListener
    private fun initializeLocationManger() {
        Log.i("TAG", "initializeLocationManger()")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationListener = LocationListener { location ->
            // This method will be called when the GPS location changes
            viewModel.updateLocation(location)
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationListener() {
        Log.i("TAG", "startLocationListener()")
        if (hasPermission()) locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10f, locationListener)
    }

    override fun stopLocationListener() {
        // Callback when map fragment is closed for remove update location
        Log.i("TAG", "stopLocationListener()")
        locationManager.removeUpdates(locationListener)
    }

    // PERMISSIONS

    private fun hasPermission() =
        EasyPermissions.hasPermissions(
            this,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )

    private fun requestPermissions() {
        EasyPermissions.requestPermissions(
            this,
            "These permission are required for this application",
            Code.PERMISSIONS_REQUEST_CODE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    override fun onPermissionsDenied(requestCode: Int, perms: List<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            SettingsDialog.Builder(this).build().show()
        } else {
            requestPermissions()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: List<String>) {
        viewModel.hasAllPermissions = true
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