package ec.edu.uisek.githubclient

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ec.edu.uisek.githubclient.databinding.FragmentRepoItemBinding
import ec.edu.uisek.githubclient.models.Repo

/**
 * ViewHolder para un solo elemento de la lista de repositorios.
 * Se encarga de vincular los datos de un repositorio a las vistas del layout.
 */
class ReposViewHolder(
    private val binding: FragmentRepoItemBinding,
    private val onEditClick: (Repo) -> Unit,
    private val onDeleteClick: (Repo) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    /**
     * Vincula los datos de un objeto [Repo] a las vistas del item.
     */
    fun bind(repo: Repo) {
        // Asigna los datos del repositorio a los TextViews correspondientes.
        binding.repoName.text = repo.name
        binding.repoDescription.text = repo.description
        binding.repoLang.text = repo.language

        // Carga la imagen del propietario del repositorio usando Glide.
        Glide.with(binding.root.context)
            .load(repo.owner.avatar_url)
            .placeholder(R.mipmap.ic_launcher) // Imagen mientras carga
            .error(R.mipmap.ic_launcher)       // Imagen si hay error
            .circleCrop()                      // Recorta la imagen en círculo
            .into(binding.repoOwnerImage)

        // Asigna las funciones lambda a los listeners de los botones de editar y eliminar.
        binding.editRepoButton.setOnClickListener { onEditClick(repo) }
        binding.deleteRepoButton.setOnClickListener { onDeleteClick(repo) }
    }
}

/**
 * Adaptador para el RecyclerView que muestra la lista de repositorios.
 * Gestiona la creación y actualización de los elementos de la lista.
 */
class ReposAdapter(
    private val onEditClick: (Repo) -> Unit,    // Lambda para manejar el clic de edición
    private val onDeleteClick: (Repo) -> Unit // Lambda para manejar el clic de eliminación
) : RecyclerView.Adapter<ReposViewHolder>() {

    private var repositories: List<Repo> = emptyList()

    /**
     * Devuelve el número total de elementos en la lista.
     */
    override fun getItemCount(): Int = repositories.size

    /**
     * Se llama cuando el RecyclerView necesita un nuevo ViewHolder. Crea e infla la vista del item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReposViewHolder {
        val binding = FragmentRepoItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ReposViewHolder(binding, onEditClick, onDeleteClick)
    }

    /**
     * Se llama para mostrar los datos en una posición específica. Vincula los datos del repositorio
     * con el ViewHolder correspondiente.
     */
    override fun onBindViewHolder(holder: ReposViewHolder, position: Int) {
        holder.bind(repositories[position])
    }

    /**
     * Actualiza la lista de repositorios en el adaptador y notifica al RecyclerView para que se redibuje.
     */
    fun updateRepositories(newRepositories: List<Repo>) {
        repositories = newRepositories
        notifyDataSetChanged() // Notifica que los datos han cambiado
    }
}
