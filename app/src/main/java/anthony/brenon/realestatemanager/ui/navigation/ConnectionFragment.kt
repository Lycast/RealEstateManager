package anthony.brenon.realestatemanager.ui.navigation

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import anthony.brenon.realestatemanager.databinding.FragmentConnectionBinding
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.ui.MainActivity
import anthony.brenon.realestatemanager.ui.MainViewModel

class ConnectionFragment : Fragment(), AdapterView.OnItemSelectedListener {

    private lateinit var activity: MainActivity
    private var _binding: FragmentConnectionBinding? = null
    private val binding get() = _binding!!
    private val viewModel by activityViewModels<MainViewModel>()

    private val agentsData: MutableList<Agent> = mutableListOf()
    private var agentSelected = Agent("","")
    private var name = ""
    private var email = ""

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentConnectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListenerView()
        setSpinnerAgents()
        binding.btnDeleteAgent.isEnabled = false
    }
    private fun setSpinnerAgents() {
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, agentsData )
        viewModel.allAgents.observe(activity) { agents ->
            // access list of agents
            agentsData.clear()
            agentsData.addAll(agents)
            adapter.notifyDataSetChanged()
        }
        binding.agentSpinner.adapter = adapter
        binding.agentSpinner.onItemSelectedListener = this
    }

    private fun setListenerView() {
        binding.apply {
            btnDeleteAgent.setOnClickListener { deleteAgent() }
            ivBack.setOnClickListener { Navigation.findNavController(root).popBackStack() }
            btnConnectAgent.setOnClickListener {
                viewModel.agentSelected = agentSelected
                Navigation.findNavController(root).popBackStack()
            }
            btnCreateAgent.setOnClickListener {
                name = createDialEtName.text.toString()
                email = createDialEtEmail.text.toString()
                createDialEtName.setText("")
                createDialEtEmail.setText("")
                createAgent(name, email)
            }
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
        if ( nameP.isNotEmpty() && // check not empty data
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
        viewModel.agentSelected = Agent("", "")
    }
}