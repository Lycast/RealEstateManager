package anthony.brenon.realestatemanager.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R

/**
 * Created by Lycast on 28/07/2022.
 */
class RecyclerAdapter: RecyclerView.Adapter<RecyclerAdapter.ViewHolder>() {

    //TODO("add list for recycler")

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return 0
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private var itemImage: ImageView

        init {
            itemImage = itemView.findViewById(R.id.card_relative_layout_image_view)

            itemView.setOnClickListener {
                val position: Int = absoluteAdapterPosition

                Toast.makeText(itemView.context, "position is : $position", Toast.LENGTH_SHORT).show()
            }
        }
    }
}