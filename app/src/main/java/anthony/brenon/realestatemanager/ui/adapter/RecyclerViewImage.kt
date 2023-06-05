package anthony.brenon.realestatemanager.ui.adapter

import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import anthony.brenon.realestatemanager.databinding.ItemImageBinding

class RecyclerViewImage (
    private val onSelect: (Bitmap?) -> Unit,
    ): RecyclerView.Adapter<RecyclerViewImage.ViewHolder>() {

    private lateinit var binding: ItemImageBinding
    private var images: List<Bitmap> = listOf()

    fun setData(imagesList: List<Bitmap>) {
        val diffResult = DiffUtil.calculateDiff(BitmapDiffCallback(images, imagesList))
        images = imagesList
        diffResult.dispatchUpdatesTo(this)
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

    class BitmapDiffCallback(private val oldList: List<Bitmap>, private val newList: List<Bitmap>) : DiffUtil.Callback() {

        override fun getOldListSize(): Int { return oldList.size }

        override fun getNewListSize(): Int { return newList.size }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }
    }
}