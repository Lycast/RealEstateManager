package anthony.brenon.realestatemanager.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.DialogAgentLoginBinding
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.ui.MainViewModel

class DialogAgentConnect : DialogFragment(), AdapterView.OnItemSelectedListener {

    private lateinit var dialogBinding: DialogAgentLoginBinding
    private val viewModel by activityViewModels<MainViewModel>()
    private val agentsData: MutableList<Agent> = mutableListOf()
    private lateinit var agentSelected : Agent


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = DialogAgentLoginBinding.inflate(layoutInflater)
        return activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            // access the spinner
            val adapter = ArrayAdapter(
                requireActivity(),
                android.R.layout.simple_spinner_item,
                agentsData
            )

            dialogBinding.agentSpinner.adapter = adapter

            dialogBinding.agentSpinner.onItemSelectedListener = this

            viewModel.allAgents.observe(requireActivity()) { agents ->
                // access the items of the list
                agentsData.addAll(agents)
                adapter.notifyDataSetChanged()
            }

            alertDialog.setPositiveButton(R.string.connect) { _,_ ->
                viewModel.selectThisAgent(agentSelected)
            }

            alertDialog.setNegativeButton(R.string.create) { _,_ ->
                DialogAgentCreate().show(parentFragmentManager, "dialog_agent_create")
            }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        Log.i("MY_TAG", "agent selected = ${agentsData[position]}")
        agentSelected = agentsData[position]
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {}
}