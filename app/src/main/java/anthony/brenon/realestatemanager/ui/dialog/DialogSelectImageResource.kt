package anthony.brenon.realestatemanager.ui.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import anthony.brenon.realestatemanager.databinding.DialogSelectImageRessourceBinding


class DialogSelectImageResource : DialogFragment() {

    private lateinit var dialogBinding: DialogSelectImageRessourceBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialogBinding = DialogSelectImageRessourceBinding.inflate(layoutInflater)
        return activity?.let { it ->

            val alertDialog = AlertDialog.Builder(it)
            alertDialog.setView(dialogBinding.root)

            dialogBinding.addEstateBtnPictureFromFolder.setOnClickListener {
                dismiss()
            }
            dialogBinding.addEstateBtnPictureFromCamera.setOnClickListener {
                dismiss()
            }

            alertDialog.create()
        }?: throw IllegalStateException("activity is null")
    }
}