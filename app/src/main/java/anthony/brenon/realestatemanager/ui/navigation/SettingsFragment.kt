package anthony.brenon.realestatemanager.ui.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.databinding.FragmentSettingsBinding
import anthony.brenon.realestatemanager.ui.MainViewModel

class SettingsFragment : Fragment() {

    private val viewModel by activityViewModels<MainViewModel>()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listenerView()
        set()
    }

    private fun set() {
        binding.toggleBtnMonetary.isChecked = viewModel.monetarySwitch
    }

    private fun listenerView() {
        binding.ivSettingsFragBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.toggleBtnMonetary.setOnCheckedChangeListener { _, isChecked ->
            viewModel.monetarySwitch = isChecked
        }
    }
}