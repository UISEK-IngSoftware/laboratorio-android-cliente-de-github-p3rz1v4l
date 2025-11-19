package ec.edu.uisek.githubclient

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ec.edu.uisek.githubclient.databinding.ActivityRepoFormBinding
import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.models.RepoRequest
import ec.edu.uisek.githubclient.services.GithubApiService
import ec.edu.uisek.githubclient.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Actividad que gestiona el formulario para crear un nuevo repositorio en GitHub.
 */
class CreateRepoActivity : AppCompatActivity() {
    /**
     * Instancia del servicio de la API de GitHub, obtenida del cliente Retrofit centralizado.
     * Se inicializa de forma diferida (lazy) la primera vez que se accede a ella.
     */
    private val githubApiService: GithubApiService by lazy {
        RetrofitClient.gitHubApiService
    }

    /**
     * Objeto de View Binding que permite acceder a las vistas del layout `activity_repo_form.xml`
     * de forma segura y eficiente.
     */
    private lateinit var binding: ActivityRepoFormBinding

    /**
     * Se llama cuando la actividad es creada. Inicializa la vista y configura los listeners de los botones.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Infla el layout usando View Binding y lo establece como el contenido de la actividad.
        binding = ActivityRepoFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configura el listener del botón "Guardar" para que llame a la función createRepo.
        binding.saveButton.setOnClickListener {
            createRepo()
        }

        // Configura el listener del botón "Cancelar" para que cierre la actividad.
        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    /**
     * Recoge los datos del formulario, realiza la llamada a la API para crear el repositorio
     * y gestiona la respuesta.
     */
    private fun createRepo() {
        // Obtiene el nombre y la descripción del repositorio desde los campos de texto.
        val repoName = binding.repoNameInput.text.toString()
        val repoDescription = binding.repoDescriptionInput.text.toString()

        // Valida que el nombre del repositorio no esté vacío.
        if (repoName.isNotEmpty()) {
            // Crea el objeto de la petición con los datos del nuevo repositorio.
            val repoRequest = RepoRequest(name = repoName, description = repoDescription)
            // Realiza la llamada asíncrona a la API para añadir el repositorio.
            val call = githubApiService.addRepo(repoRequest)
            call.enqueue(object : Callback<Repo> {
                /**
                 * Se llama cuando se recibe una respuesta de la API.
                 */
                override fun onResponse(call: Call<Repo>, response: Response<Repo>) {
                    if (response.isSuccessful) {
                        // Si la creación fue exitosa, muestra un mensaje y cierra la actividad.
                        Toast.makeText(this@CreateRepoActivity, "Repository created successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        // Si hubo un error en la creación, muestra un mensaje de fallo.
                        Toast.makeText(this@CreateRepoActivity, "Failed to create repository", Toast.LENGTH_SHORT).show()
                    }
                }

                /**
                 * Se llama cuando la llamada a la API falla por un problema de red u otro error.
                 */
                override fun onFailure(call: Call<Repo>, t: Throwable) {
                    // Muestra un mensaje de error con el detalle del problema.
                    Toast.makeText(this@CreateRepoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Si el nombre está vacío, pide al usuario que lo complete.
            Toast.makeText(this, "Repository name cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}
