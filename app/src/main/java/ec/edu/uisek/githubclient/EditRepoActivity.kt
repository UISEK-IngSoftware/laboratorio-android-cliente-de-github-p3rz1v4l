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
 * Actividad que gestiona el formulario para editar un repositorio existente en GitHub.
 */
class EditRepoActivity : AppCompatActivity() {
    /**
     * Instancia del servicio de la API de GitHub, obtenida del cliente Retrofit centralizado.
     */
    private val githubApiService: GithubApiService by lazy {
        RetrofitClient.gitHubApiService
    }

    /**
     * Objeto de View Binding para acceder a las vistas del layout `activity_repo_form.xml`.
     */
    private lateinit var binding: ActivityRepoFormBinding
    private lateinit var repoName: String
    private lateinit var repoOwner: String

    /**
     * Se llama cuando la actividad es creada. Inicializa la vista, recoge los datos del repositorio
     * y configura los listeners de los botones.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRepoFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Recoge el nombre y el propietario del repositorio pasados desde MainActivity.
        repoName = intent.getStringExtra("repo_name") ?: ""
        repoOwner = intent.getStringExtra("repo_owner") ?: ""

        // Rellena los campos del formulario con los datos actuales del repositorio.
        binding.repoNameInput.setText(repoName)
        binding.repoDescriptionInput.setText(intent.getStringExtra("repo_description"))

        // Configura el listener del botón "Guardar" para que llame a la función updateRepo.
        binding.saveButton.setOnClickListener {
            updateRepo()
        }

        // Configura el listener del botón "Cancelar" para que cierre la actividad.
        binding.cancelButton.setOnClickListener {
            finish()
        }
    }

    /**
     * Recoge los datos del formulario, realiza la llamada a la API para actualizar el repositorio
     * y gestiona la respuesta.
     */
    private fun updateRepo() {
        val newRepoName = binding.repoNameInput.text.toString()
        val repoDescription = binding.repoDescriptionInput.text.toString()

        // Valida que el nuevo nombre del repositorio no esté vacío.
        if (newRepoName.isNotEmpty()) {
            // Crea el objeto de la petición con los datos actualizados.
            val repoRequest = RepoRequest(name = newRepoName, description = repoDescription)
            // Realiza la llamada asíncrona a la API para actualizar el repositorio.
            val call = githubApiService.updateRepo(repoOwner, repoName, repoRequest)
            call.enqueue(object : Callback<Repo> {
                /**
                 * Se llama cuando se recibe una respuesta de la API.
                 */
                override fun onResponse(call: Call<Repo>, response: Response<Repo>) {
                    if (response.isSuccessful) {
                        // Si la actualización fue exitosa, muestra un mensaje y cierra la actividad.
                        Toast.makeText(this@EditRepoActivity, "Repository updated successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        // Si hubo un error, muestra un mensaje de fallo.
                        Toast.makeText(this@EditRepoActivity, "Failed to update repository", Toast.LENGTH_SHORT).show()
                    }
                }

                /**
                 * Se llama cuando la llamada a la API falla.
                 */
                override fun onFailure(call: Call<Repo>, t: Throwable) {
                    // Muestra un mensaje de error.
                    Toast.makeText(this@EditRepoActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            // Si el nombre está vacío, pide al usuario que lo complete.
            Toast.makeText(this, "Repository name cannot be empty", Toast.LENGTH_SHORT).show()
        }
    }
}
