package anthony.brenon.realestatemanager.ui.navigation

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
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewImage

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var adapter : RecyclerViewImage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.estateSelected.observe(requireActivity()) {
            populateView(it)
            initRVImage(it.id)
        }

        listenerClickView()
    }

    private fun populateView(estate: Estate ) {
        binding.apply {
            detailsActivityTvDescription.text = estate.description
            detailsActivityTvSurface.text = estate.surface
            detailsActivityTvRoomNumber.text = estate.roomsNumber
            detailsActivityTvLocation.text = estate.address
        }
    }

    private fun initRVImage(estateId : Long) {

        adapter = RecyclerViewImage {
            binding.layoutDetails?.visibility = View.GONE
            binding.layoutImage?.visibility = View.VISIBLE
            binding.ivDetails?.setImageBitmap(it)
        }

        binding.recyclerViewImage.adapter = adapter
        Log.i("MY_TAG","id estate recycler view details = $estateId")
        val images = mutableListOf<Bitmap>()
        viewModel.getPicturesByEstate(estateId).observe(requireActivity()) {
            for (picture in it) {
                images.add(picture.picture)
            }
            adapter.setData(images)
        }
    }

    private fun listenerClickView() {
        binding.imgAddEstateBack?.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }
        binding.imgImageClose?.setOnClickListener {
            binding.layoutDetails?.visibility = View.VISIBLE
            binding.layoutImage?.visibility = View.GONE
        }
    }
}