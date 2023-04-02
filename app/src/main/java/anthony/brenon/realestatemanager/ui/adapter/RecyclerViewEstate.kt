package anthony.brenon.realestatemanager.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import anthony.brenon.realestatemanager.databinding.ItemEstateBinding
import anthony.brenon.realestatemanager.models.Estate

/**
 * Created by Lycast on 28/07/2022.
 */
class RecyclerViewEstate (
    private val onSelect: (Estate?) -> Unit,
    //todo private val viewModel: MainViewModel
    ): RecyclerView.Adapter<RecyclerViewEstate.ViewHolder>() {

    private lateinit var binding: ItemEstateBinding
    private var estates: List<Estate> = arrayListOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(estatesList: List<Estate>) {
        this.estates = estatesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemEstateBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(estates[position], onSelect)
    }

    override fun getItemCount(): Int = estates.size

    inner class ViewHolder(
        private val itemBinding: ItemEstateBinding,
        ): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(estate: Estate, onSelect: (Estate?) -> Unit) {
            //todo implement image
            itemBinding.apply {
                //todo cardLayoutImageView.setImageBitmap(estate.)
                cardLayoutTextView1.text = estate.type
                cardLayoutTextView2.text = estate.address
                cardLayoutTextView3.text = estate.price.toString()
            }
            // bind your view here
            binding.root.setOnClickListener {
                onSelect(estate)
            }
        }
    }
}