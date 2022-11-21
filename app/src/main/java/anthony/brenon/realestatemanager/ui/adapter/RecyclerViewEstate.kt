package anthony.brenon.realestatemanager.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import anthony.brenon.realestatemanager.databinding.CardItemBinding
import anthony.brenon.realestatemanager.models.Estate

/**
 * Created by Lycast on 28/07/2022.
 */
class RecyclerViewEstate (
    private var estates: List<Estate>,
    private val onSelect: (Estate?) -> Unit,
    ): RecyclerView.Adapter<RecyclerViewEstate.ViewHolder>() {

    private lateinit var binding: CardItemBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(estates[position], onSelect)
    }

    override fun getItemCount(): Int = estates.size

    inner class ViewHolder(
        private val itemBinding: CardItemBinding,
        ): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(estate: Estate, onSelect: (Estate?) -> Unit) {
            itemBinding.apply {
                cardLayoutImageView.setImageResource(estate.photos.get(0))
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