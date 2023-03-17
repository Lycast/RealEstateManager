package anthony.brenon.realestatemanager.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import anthony.brenon.realestatemanager.databinding.DialogAgentLoginBinding
import anthony.brenon.realestatemanager.ui.MainViewModel

class DialogAgentConnect : DialogFragment() {

    private lateinit var dialogBinding: DialogAgentLoginBinding
    val viewModel by activityViewModels<MainViewModel>()



    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = DialogAgentLoginBinding.inflate(layoutInflater)
        return activity?.let {
            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            alertDialog.create()

        }?: throw IllegalStateException("activity is null")
    }
}