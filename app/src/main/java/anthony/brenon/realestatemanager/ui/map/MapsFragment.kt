package anthony.brenon.realestatemanager.ui.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.ui.MainViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var callback: OnMapReadyCallback
    private var latLng = LatLng(0.00, 0.00)
    private var title = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMap()
        viewModel.estateSelected.observe(requireActivity()) {
            latLng = LatLng(it.lat, it.lng)
            setMap()
        }
    }

    private fun setMap() {
        callback = OnMapReadyCallback { googleMap ->
            googleMap.addMarker(MarkerOptions().position(latLng).title(title))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 11.0f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}