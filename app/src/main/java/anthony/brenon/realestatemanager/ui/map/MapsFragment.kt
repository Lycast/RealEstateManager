package anthony.brenon.realestatemanager.ui.map


import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentMapsBinding
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.ui.MainViewModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var googleMap: GoogleMap
    private var markers: ArrayList<Marker> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMap()
        listenerClickView()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun onMapReady(p0: GoogleMap) {
        googleMap = p0
        googleMap.setOnInfoWindowClickListener(this::onMarkerClick)
        setObservers()
    }

    private fun setObservers() {
        viewModel.sortListEstate.observe(viewLifecycleOwner) { setView(it) }
    }

    private fun setView(estates :List<Estate>) {

        // Remove existing markers
        for (marker in markers) {
            marker.remove()
        }
        markers.clear()

        val pos = if (estates.isNotEmpty()) LatLng(estates[0].lat, estates[0].lng)
        else LatLng(47.06072250587188, -0.8897286371775496)

        for (i in estates.indices) {
            val latitude: Double = estates[i].lat
            val longitude: Double = estates[i].lng
            val locations = LatLng(latitude, longitude)
            val marker: Marker? = googleMap.addMarker(MarkerOptions().position(locations).title(estates[i].addressStreet))
            marker?.tag = estates[i]
            marker?.let { markers.add(it) }
        }

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pos, 11.0f))
    }

    private fun listenerClickView() {
        binding.imgMapBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }
    }

    private fun onMarkerClick(marker: Marker): Boolean {
        viewModel.selectThisEstate(marker.tag as Estate)
        Navigation.findNavController(binding.root).navigate(R.id.item_detail_fragment)
        return false
    }
}