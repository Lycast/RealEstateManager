package anthony.brenon.realestatemanager.ui.navigation

import android.os.Bundle
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

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

//    private lateinit var adapter : RecyclerViewImage
//    private lateinit var images : List<Int>

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
        }
        onClickFabClose()
    }

    private fun populateView(estate: Estate ) {
        binding.apply {
//            initRVImage(images)
            detailsActivityTvDescription.text = estate.description
            detailsActivityTvSurface.text = "${estate.surface} sq m"
            detailsActivityTvRoomNumber.text = estate.roomsNumber.toString()
            detailsActivityTvLocation.text = estate.address
        }
    }

//    private fun initRVImage(images : List<Int>) {
//        adapter = RecyclerViewImage(images){
//            //todo add init images
//        }
//        binding.recyclerViewImage.adapter = adapter
//    }

    private fun onClickFabClose() {
        binding.imgDetailsClose?.setOnClickListener {
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }
    }
}