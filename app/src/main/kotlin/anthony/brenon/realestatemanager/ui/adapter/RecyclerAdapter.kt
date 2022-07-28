package anthony.brenon.realestatemanager.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import anthony.brenon.realestatemanager.R
import anthony.brenon.realestatemanager.databinding.CardItemBinding
import anthony.brenon.realestatemanager.models.Estate

/**
 * Created by Lycast on 28/07/2022.
 */
class RecyclerAdapter (private var estates: List<Estate>): RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    private lateinit var binding: CardItemBinding

    fun updateDataEstates(estateList: List<Estate>) {
        this.estates = estateList
        this.notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val estate: Estate = estates[position]
        holder.bind(estate)
    }

    override fun getItemCount(): Int = estates.size


    inner class ViewHolder(private val itemBinding: CardItemBinding): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(estate: Estate) {
            itemBinding.apply {
                cardLayoutTextView1.text = estate.type
                cardLayoutTextView2.text = estate.price.toString()
            }
        }

        init {
            itemView.setOnClickListener {
                val position: Int = absoluteAdapterPosition
                Toast.makeText(itemView.context, "position is : $position", Toast.LENGTH_SHORT).show()
            }
        }

    }
}