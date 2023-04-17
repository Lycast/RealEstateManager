package anthony.brenon.realestatemanager.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.DialogAgentCreateBinding
import anthony.brenon.realestatemanager.models.Agent
import anthony.brenon.realestatemanager.ui.MainViewModel
import com.google.android.material.snackbar.Snackbar

class DialogAgentCreate : DialogFragment() {

    private lateinit var dialogBinding: DialogAgentCreateBinding
    private val viewModel by activityViewModels<MainViewModel>()
    private lateinit var alertDialog: AlertDialog.Builder
    private var name = ""
    private var email = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = DialogAgentCreateBinding.inflate(layoutInflater)
        return activity?.let {
            alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            alertDialog.setPositiveButton(R.string.create) { _, _ ->
                if (name.isNotEmpty() && email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    val agentCreated = Agent(name, email)
                    viewModel.insertAgent(agentCreated)
                }
                DialogAgentConnect().show(childFragmentManager,"dialog_connect")
            }

            dialogBinding.createDialEtName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    Log.i("MY_TAG", "name : " + s.toString())
                    name = dialogBinding.createDialEtName.text.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            dialogBinding.createDialEtEmail.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    Log.i("MY_TAG", "mail : " + s.toString())
                    email = dialogBinding.createDialEtEmail.text.toString()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })

            alertDialog.setNegativeButton(R.string.cancel) { _, _ ->
                dismiss()
            }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }

    private fun initPositiveButton() {
        alertDialog.setPositiveButton(R.string.create) { _, _ ->
            val agentCreated = Agent(name, email)
            viewModel.insertAgent(agentCreated)
            DialogAgentConnect().show(childFragmentManager,"dialog_connect")
        }
    }

//    if (name.isNotEmpty() && email.isNotEmpty() && Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
//        val agentCreated = Agent(name, email)
//        viewModel.insertAgent(agentCreated)
//        DialogAgentConnect().show(childFragmentManager,"dialog_connect")
//    } else {
//        Snackbar.make(
//            dialogBinding.root,
//            "please enter valid information",
//            Snackbar.LENGTH_SHORT
//        ).show()
//    }
}