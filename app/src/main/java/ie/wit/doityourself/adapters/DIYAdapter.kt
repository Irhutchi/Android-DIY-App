package ie.wit.doityourself.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ie.wit.doityourself.databinding.CardDiytaskBinding
import ie.wit.doityourself.models.DIYModel

class DIYAdapter constructor(private var tasks: List<DIYModel>) :
    RecyclerView.Adapter<DIYAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardDiytaskBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return MainHolder(binding)
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val task = tasks[holder.adapterPosition]
        holder.bind(task)
    }

    override fun getItemCount(): Int = tasks.size

    class MainHolder(private val binding : CardDiytaskBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(task: DIYModel) {
            binding.taskTitle.text = task.title
            binding.description.text = task.description
        }
    }
}