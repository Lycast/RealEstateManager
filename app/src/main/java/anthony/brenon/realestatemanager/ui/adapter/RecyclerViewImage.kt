package anthony.brenon.realestatemanager.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import anthony.brenon.realestatemanager.databinding.ItemImageBinding

/**
 * Created by Lycast on 28/07/2022.
 */
class RecyclerViewImage (
    private val onSelect: (Bitmap?) -> Unit,
    ): RecyclerView.Adapter<RecyclerViewImage.ViewHolder>() {

    private var images: List<Bitmap> = listOf()
    private lateinit var binding: ItemImageBinding

    @SuppressLint("NotifyDataSetChanged")
    fun setData(imagesList: List<Bitmap>) {
        this.images = imagesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = ItemImageBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(images[position], onSelect)
    }

    override fun getItemCount(): Int = images.size

    inner class ViewHolder(
        private val itemBinding: ItemImageBinding,
        ): RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(image: Bitmap, onSelect: (Bitmap?) -> Unit) {
            itemBinding.apply {
                imageItem.setImageBitmap(image)
            }
            // bind your view here
            binding.root.setOnClickListener {
                onSelect(image)
            }
        }
    }
}