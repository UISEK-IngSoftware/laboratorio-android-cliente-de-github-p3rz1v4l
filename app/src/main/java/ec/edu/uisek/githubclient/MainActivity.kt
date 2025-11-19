package ec.edu.uisek.githubclient

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ec.edu.uisek.githubclient.databinding.ActivityMainBinding
import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.services.GithubApiService
import ec.edu.uisek.githubclient.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad principal que muestra la lista de repositorios del usuario.
 */
class MainActivity : AppCompatActivity() {
    /**
     * Objeto de View Binding para acceder a las vistas del layout `activity_main.xml`.
     */
    private lateinit var binding: ActivityMainBinding
    private lateinit var reposAdapter: ReposAdapter

    /**
     * Instancia del servicio de la API de GitHub, obtenida del cliente Retrofit centralizado.
     */
    private val apiService: GithubApiService by lazy {
        RetrofitClient.gitHubApiService
    }

    /**
     * Se llama cuando la actividad es creada. Inicializa la vista y el RecyclerView.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura el RecyclerView y sus listeners.
        setupRecyclerView()

        // Configura el listener del Floating Action Button para abrir el formulario de creación.
        binding.newRepoFab.setOnClickListener {
            displayNewRepoForm()
        }
    }

    /**
     * Se llama cuando la actividad vuelve a estar en primer plano. Se usa para refrescar la lista de repositorios.
     */
    override fun onResume() {
        super.onResume()
        fetchRepositories()
    }

    /**
     * Inicializa el ReposAdapter con las acciones de edición y eliminación y lo asigna al RecyclerView.
     */
    private fun setupRecyclerView() {
        reposAdapter = ReposAdapter(
            // Acción a ejecutar al hacer clic en el botón de editar.
            onEditClick = { repo ->
                val intent = Intent(this, EditRepoActivity::class.java).apply {
                    putExtra("repo_name", repo.name)
                    putExtra("repo_owner", repo.owner.login)
                    putExtra("repo_description", repo.description)
                }
                startActivity(intent)
            },
            // Acción a ejecutar al hacer clic en el botón de eliminar.
            onDeleteClick = { repo ->
                apiService.deleteRepo(repo.owner.login, repo.name).enqueue(object : Callback<Unit> {
                    override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                        if (response.isSuccessful) {
                            showMessage("Repository deleted successfully")
                            fetchRepositories() // Recarga la lista después de eliminar.
                        } else {
                            showMessage("Failed to delete repository")
                        }
                    }

                    override fun onFailure(call: Call<Unit>, t: Throwable) {
                        showMessage("Failed to delete repository: ${t.message}")
                    }
                })
            }
        )
        binding.reposRecyclerView.adapter = reposAdapter
    }

    /**
     * Realiza la llamada a la API para obtener la lista de repositorios y la actualiza en el adaptador.
     */
    private fun fetchRepositories() {
        apiService.getRepos().enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                if (response.isSuccessful) {
                    val repos = response.body()
                    if (repos != null && repos.isNotEmpty()) {
                        reposAdapter.updateRepositories(repos)
                    } else {
                        showMessage("No repositories found")
                    }
                } else {
                    showMessage("Error fetching repositories: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {
                showMessage("Failed to load repositories: ${t.message}")
            }
        })
    }

    /**
     * Muestra un mensaje Toast corto en la pantalla.
     */
    private fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    /**
     * Lanza la actividad para crear un nuevo repositorio.
     */
    private fun displayNewRepoForm() {
        Intent(this, CreateRepoActivity::class.java).apply {
            startActivity(this)
        }
    }
}
