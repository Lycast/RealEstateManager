package anthony.brenon.realestatemanager.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.DialogAgentCreateBinding
import anthony.brenon.realestatemanager.ui.MainViewModel

class DialogAgentCreate : DialogFragment() {

    private lateinit var dialogBinding: DialogAgentCreateBinding
    val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = DialogAgentCreateBinding.inflate(layoutInflater)
        return activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            alertDialog.setPositiveButton(R.string.create) { _, _ ->
                //todo not yet implemented
                dismiss()
            }

            alertDialog.setNegativeButton(R.string.cancel) { _, _ ->
                dismiss()
            }

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }
}