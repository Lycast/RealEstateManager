package anthony.brenon.realestatemanager.ui.adapter

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import anthony.brenon.realestatemanager.databinding.ItemEstateBinding
import anthony.brenon.realestatemanager.models.Estate
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.logging.SimpleFormatter

/**
 * Created by Lycast on 28/07/2022.
 */
class RecyclerViewEstate (
    private val onSelect: (Estate?) -> Unit,
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

            var price = ""

            val dec = DecimalFormat("#,###")
            if (estate.price?.isNotEmpty() == true) {
                    Log.i("MY_LOG", estate.price.toString())
                price = dec.format(estate.price!!.toInt())
            }



            //todo implement image
            itemBinding.apply {
                cardLayoutImageView.setImageBitmap(estate.picture)
                cardLayoutTextView1.text = estate.type
                cardLayoutTextView2.text = estate.address
                cardLayoutTextView3.text = price
            }
            // bind your view here
            binding.root.setOnClickListener {
                onSelect(estate)
            }
        }
    }
}