package ie.wit.doityourself.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import ie.wit.doityourself.R
import ie.wit.doityourself.databinding.CardDiytaskBinding
import ie.wit.doityourself.models.DIYModel
import ie.wit.doityourself.utils.customTransformation

// interface will represent click events on the task Card.
interface DIYClickListener {
    fun onDIYClick(task: DIYModel)
}

// Adapter - accepts and installs an event handler based on the interface
class DIYAdapter(private var tasks: ArrayList<DIYModel>,
                 private val listener: DIYClickListener,
                 private val readOnly: Boolean) :
    RecyclerView.Adapter<DIYAdapter.MainHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainHolder {
        val binding = CardDiytaskBinding    // initialise view
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return MainHolder(binding, readOnly)    // return holder view
    }

    override fun onBindViewHolder(holder: MainHolder, position: Int) {
        val task = tasks[holder.adapterPosition]
        holder.bind(task, listener)
    }

    fun removeAt(position: Int) {
        tasks.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = tasks.size

    inner class MainHolder(val binding: CardDiytaskBinding, private val readOnly: Boolean) :
        RecyclerView.ViewHolder(binding.root) {

        val readOnlyRow = readOnly

        fun bind(task: DIYModel, listener: DIYClickListener) {
            // update task data element with the individual task that is passed to main holder class
            binding.root.tag = task.uid
            binding.task = task
            Picasso.get().load(task.profilepic.toUri())
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(binding.imageIcon)
            binding.imageIcon.setImageResource(R.mipmap.ic_launcher_round)
            binding.root.setOnClickListener { listener.onDIYClick(task) }
            binding.executePendingBindings() // force bindings to happen immediately
        }
    }


}