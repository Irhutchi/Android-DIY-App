package ie.wit.doityourself.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.doityourself.databinding.CardDiytaskBinding
import ie.wit.doityourself.fragments.DiyListFragment
import ie.wit.doityourself.models.DIYModel

// interface will represent click events on the task Card.
interface DIYListener {
    fun onDIYClick(task: DIYModel)
}

// Adapter - accepts and installs an event handler based on the interface
class DIYAdapter(private var tasks: List<DIYModel>,
                 private val listener: DiyListFragment
) :
    RecyclerView.Adapter<DIYAdapter.MainHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardDiytaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val task = tasks[holder.adapterPosition]
        holder.bind(task, listener)
    }

    override fun getItemCount(): Int = tasks.size

    class MainHolder(private val binding : CardDiytaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: DIYModel, listener: DIYListener) {
            binding.taskTitle.text = task.title
            binding.description.text = task.description
            binding.rgRating.text = task.rating
            Picasso.get().load(task.image).resize(200,200).into(binding.imageIcon)
            binding.root.setOnClickListener { listener.onDIYClick(task) }

        }
    }
}