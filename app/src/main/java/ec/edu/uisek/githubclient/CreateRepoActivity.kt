package ec.edu.uisek.githubclient

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateRepoActivity : AppCompatActivity() {

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
                // Call the function to create the repository using the GitHub API
                createRepository(repoName, repoDescription)
            } else {
                Toast.makeText(this, "Please enter a repository name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createRepository(name: String, description: String) {
        // Implement the logic to create the repository using the GitHub API (POST)
        // You will need to use a library like Retrofit to make the API call
        // Remember to handle the success and error cases

        // For now, we will just show a toast message
        Toast.makeText(this, "Repository created successfully (Not implemented yet)", Toast.LENGTH_SHORT).show()
        finish()
    }
}