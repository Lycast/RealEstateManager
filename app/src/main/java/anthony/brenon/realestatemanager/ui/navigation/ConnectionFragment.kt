package anthony.brenon.realestatemanager.ui.navigation

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.FragmentConnectionBinding
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.ui.MainViewModel

class ConnectionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private var _binding: FragmentConnectionBinding? = null
    private val binding get() = _binding!!

    private val viewModel by activityViewModels<MainViewModel>()
    private val agentsData: MutableList<Agent> = mutableListOf()

    private var agentSelected = Agent("","")
    private var name = ""
    private var email = ""

    //todo update agent

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDeleteAgent.isEnabled = false

        // access the spinner
        val adapter = ArrayAdapter(
            requireActivity(),
            android.R.layout.simple_spinner_item,
            agentsData
        )

        binding.agentSpinner.adapter = adapter
        binding.agentSpinner.onItemSelectedListener = this


        viewModel.allAgents.observe(requireActivity()) { agents ->
            // access the items of the list
            agentsData.clear()
            agentsData.addAll(agents)
            adapter.notifyDataSetChanged()
        }

        binding.btnDeleteAgent.setOnClickListener { deleteAgent() }
        binding.ivBack.setOnClickListener { Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment) }
        binding.btnConnectAgent.setOnClickListener {
            viewModel.selectThisAgent(agentSelected)
            Navigation.findNavController(binding.root).navigate(R.id.item_list_fragment)
        }
        binding.btnCreateAgent.setOnClickListener {
            name = binding.createDialEtName.text.toString()
            email = binding.createDialEtEmail.text.toString()
            binding.createDialEtName.setText("")
            binding.createDialEtEmail.setText("")
            createAgent(name, email)
        }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        agentSelected = agentsData[position]
        binding.createDialEtName.hint = agentSelected.nameAgent
        binding.createDialEtEmail.hint = agentSelected.emailAgent
        binding.btnDeleteAgent.isEnabled = true
    }
    override fun onNothingSelected(parent: AdapterView<*>?) {}

    private fun createAgent(nameP: String, emailP: String) {
        Log.i("MY_TAG", "name = $nameP - mail = $emailP")
        if (
            nameP.isNotEmpty() && // check not empty data
            emailP.isNotEmpty() &&  // check not empty data
            Patterns.EMAIL_ADDRESS.matcher(emailP).matches() && // check valid mail
            nameP != agentSelected.nameAgent  // check it's not current selected agent
        ) {
            val agentCreated = Agent(nameP, emailP)
            viewModel.insertAgent(agentCreated)
            agentSelected = agentCreated
            binding.createDialEtEmail.text.clear()
        }
    }

    private fun deleteAgent() {
        viewModel.deleteAgent(agentSelected)
        viewModel.agentSelected.value = Agent("", "")
    }
}