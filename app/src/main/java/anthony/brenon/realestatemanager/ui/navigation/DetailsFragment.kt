package anthony.brenon.realestatemanager.ui.navigation

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentDetailsBinding
import anthony.brenon.realestatemanager.models.Estate
import anthony.brenon.realestatemanager.ui.MainActivity
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewImage
import anthony.brenon.realestatemanager.utils.EstateStatus
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.snackbar.Snackbar
import java.text.DecimalFormat

class DetailsFragment : Fragment() {

    private lateinit var activity: MainActivity
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    private lateinit var adapter : RecyclerViewImage

    // MAP
    private lateinit var callback: OnMapReadyCallback
    private lateinit var googleMap: GoogleMap

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setMap()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map_details) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)

        binding.layoutDetails.visibility = View.GONE
        viewModel.estateSelected.observe(activity) {
            binding.layoutDetails.visibility = View.VISIBLE
            populateView(it)
            initRVImage(it.id)
            googleMap.addMarker(MarkerOptions().position(LatLng(it.lat, it.lng)).title(it.addressStreet))
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.lat, it.lng), 11.0f))
        }

        listenerClickView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setMap() {
        callback = OnMapReadyCallback {
            googleMap = it
        }
    }

    private fun populateView(estate: Estate ) {

        var price = ""
        val dec = DecimalFormat("#,###")
        if (estate.price.isNotEmpty()) {
            price = "${dec.format(estate.price.toInt())}$"
        }

        binding.apply {
            detailsActivityTvDescription.text = estate.description
            detailsActivityTvType.text = estate.type
            detailsActivityTvPrice.text = price
            detailsActivityTvSurface.text = estate.surface
            detailsActivityTvRoom.text = estate.roomsNumber
            detailsActivityTvInteresting.text = estate.interestingPoint
            detailsActivityTvOnSaleDate.text = estate.onSaleDate
            detailsActivityTvSold.text = estate.dateOfSale
            detailsActivityTvAgent.text = estate.agentInChargeName

            val loc2 = "${estate.addressCode} ${estate.addressCity}"
            detailsActivityTvLoc1.text = estate.addressStreet
            detailsActivityTvLoc2.text = loc2
            detailsActivityTvLoc3.text = estate.addressCountry
        }
    }

    private fun initRVImage(estateId : Long) {

        adapter = RecyclerViewImage {
            binding.layoutDetails.visibility = View.GONE
            binding.layoutImage.visibility = View.VISIBLE
            binding.ivDetails.setImageBitmap(it)
        }

        binding.recyclerViewImage.adapter = adapter
        Log.i("MY_TAG","id estate recycler view details = $estateId")
        val images = mutableListOf<Bitmap>()
        viewModel.getPicturesByEstate(estateId).observe(activity) {
            for (picture in it) {
                images.add(picture.picture)
            }
            adapter.setData(images)
        }
    }

    private fun listenerClickView() {
        binding.apply {
            imgAddEstateBack?.setOnClickListener {
                Navigation.findNavController(binding.root).popBackStack()
            }

            imgImageClose.setOnClickListener {
                binding.layoutDetails.visibility = View.VISIBLE
                binding.layoutImage.visibility = View.GONE
            }

            imgEstateEdit.setOnClickListener {
                if(viewModel.agentIsConnected()) {
                    viewModel.selectThisEstateStatus(EstateStatus.UPDATE_EXISTING_ESTATE)
                    Navigation.findNavController(binding.root).navigate(R.id.item_add_fragment)
                } else Snackbar.make(binding.root, "You need to be logged in for edit an estate", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}