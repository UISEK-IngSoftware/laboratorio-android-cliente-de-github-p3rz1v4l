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

class EditRepoActivity : AppCompatActivity() {

    private lateinit var repoNameEditText: EditText
    private lateinit var repoDescriptionEditText: EditText
    private lateinit var updateRepoButton: Button
    private lateinit var repoName: String

    private val apiService: GithubApiService by lazy {
        RetrofitClient.gitHubApiService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_repo)

        repoNameEditText = findViewById(R.id.repo_name_edit_text)
        repoDescriptionEditText = findViewById(R.id.repo_description_edit_text)
        updateRepoButton = findViewById(R.id.update_repo_button)

        repoName = intent.getStringExtra("repo_name").orEmpty()
        val repoDescription = intent.getStringExtra("repo_description").orEmpty()

        repoNameEditText.setText(repoName)
        repoDescriptionEditText.setText(repoDescription)

        updateRepoButton.setOnClickListener {
            val newRepoName = repoNameEditText.text.toString()
            val newRepoDescription = repoDescriptionEditText.text.toString()

            if (newRepoName.isNotEmpty()) {
                updateRepository(repoName, newRepoName, newRepoDescription)
            } else {
                Toast.makeText(this, "Please enter a repository name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateRepository(owner: String, repo: String, description: String) {
        val repoRequest = RepoRequest(repo, description)
        apiService.updateRepo(owner, repo, repoRequest).enqueue(object : Callback<Repo> {
            override fun onResponse(call: Call<Repo>, response: Response<Repo>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@EditRepoActivity, "Repository updated successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this@EditRepoActivity, "Failed to update repository", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Repo>, t: Throwable) {
                Toast.makeText(this@EditRepoActivity, "Failed to update repository", Toast.LENGTH_SHORT).show()
            }
        })
    }
}