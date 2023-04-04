package anthony.brenon.realestatemanager.ui.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentListBinding
import anthony.brenon.realestatemanager.ui.MainViewModel
import anthony.brenon.realestatemanager.ui.adapter.RecyclerViewEstate


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    private lateinit var adapter: RecyclerViewEstate


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
        ): View {
            _binding = FragmentListBinding.inflate(inflater, container, false)
            return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setRecyclerViewEstates()
    }

    private fun setRecyclerViewEstates() {

        adapter = RecyclerViewEstate {
            viewModel.selectThisEstate(it!!)
            Navigation.findNavController(binding.root).navigate(R.id.item_detail_fragment)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        binding.recyclerView.adapter = adapter

        viewModel.allEstates.observe(requireActivity()) {
            adapter.setData(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}