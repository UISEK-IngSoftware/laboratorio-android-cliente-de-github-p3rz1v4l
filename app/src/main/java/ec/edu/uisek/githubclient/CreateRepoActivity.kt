package ec.edu.uisek.githubclient

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.models.RepoRequest
import ec.edu.uisek.githubclient.services.GithubApiService
import ec.edu.uisek.githubclient.services.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CreateRepoActivity : AppCompatActivity() {

    private val apiService: GithubApiService by lazy {
        RetrofitClient.gitHubApiService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_repo)

        val repoNameEditText = findViewById<EditText>(R.id.repo_name_edit_text)
        val repoDescriptionEditText = findViewById<EditText>(R.id.repo_description_edit_text)
        val createRepoButton = findViewById<Button>(R.id.create_repo_button)

        createRepoButton.setOnClickListener {
            val repoName = repoNameEditText.text.toString()
            val repoDescription = repoDescriptionEditText.text.toString()

            if (repoName.isNotEmpty()) {
                createRepository(repoName, repoDescription)
            } else {
                Toast.makeText(this, "Please enter a repository name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createRepository(name: String, description: String) {
        val repoRequest = RepoRequest(name, description)
        apiService.addRepo(repoRequest).enqueue(object : Callback<Repo> {
            override fun onResponse(call: Call<Repo>, response: Response<Repo>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@CreateRepoActivity, "Repository created successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@CreateRepoActivity, "Failed to create repository", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Repo>, t: Throwable) {
                Toast.makeText(this@CreateRepoActivity, "Failed to create repository", Toast.LENGTH_SHORT).show()
            }
        })
    }
}