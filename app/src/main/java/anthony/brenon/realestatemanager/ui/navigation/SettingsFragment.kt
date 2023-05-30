package anthony.brenon.realestatemanager.ui.navigation


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.databinding.FragmentSettingsBinding
import anthony.brenon.realestatemanager.inject.DataGenerator
import anthony.brenon.realestatemanager.ui.MainViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
        if (!viewModel.hasAllPermissions) {
            binding.btnGenerateData.isEnabled = false
        }
        listenerView()
        set()
    }

    private fun set() {
        viewModel.monetarySwitch.observe(viewLifecycleOwner) {
            binding.toggleBtnMonetary.isChecked = it
        }
    }

    private fun listenerView() {
        binding.ivSettingsFragBack.setOnClickListener {
            Navigation.findNavController(binding.root).popBackStack()
        }

        binding.toggleBtnMonetary.setOnCheckedChangeListener { _, isChecked ->
            viewModel.selectMonetary(isChecked)
        }

        binding.btnGenerateData.setOnClickListener {
            viewEnable(false)
            CoroutineScope(Dispatchers.IO).launch {
                for (agent in DataGenerator.generateAgentData()) viewModel.insertAgent(agent)
                for (estate in DataGenerator.generateEstateData(requireContext())) {
                    viewModel.insertEstate(estate)
                }
                withContext(Dispatchers.Main) {
                    viewEnable(true)
                }
            }
        }
    }

    private fun viewEnable(boolean: Boolean) {
        binding.btnGenerateData.isEnabled = boolean
        binding.toggleBtnMonetary.isEnabled = boolean
        binding.loadProgressBar.visibility = if (boolean) View.GONE else View.VISIBLE
        binding.ivSettingsFragBack.visibility = if (boolean) View.VISIBLE else View.GONE
    }
}