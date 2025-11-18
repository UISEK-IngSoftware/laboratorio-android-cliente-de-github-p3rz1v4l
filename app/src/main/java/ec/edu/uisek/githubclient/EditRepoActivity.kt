package ec.edu.uisek.githubclient

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class EditRepoActivity : AppCompatActivity() {

    private lateinit var repoNameEditText: EditText
    private lateinit var repoDescriptionEditText: EditText
    private lateinit var updateRepoButton: Button
    private lateinit var repoName: String

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
                // Call the function to update the repository using the GitHub API
                updateRepository(repoName, newRepoName, newRepoDescription)
            } else {
                Toast.makeText(this, "Please enter a repository name", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateRepository(oldName: String, newName: String, description: String) {
        // Implement the logic to update the repository using the GitHub API (PATCH or PUT)
        // You will need to use a library like Retrofit to make the API call
        // Remember to handle the success and error cases

        // For now, we will just show a toast message
        Toast.makeText(this, "Repository updated successfully (Not implemented yet)", Toast.LENGTH_SHORT).show()
        finish()
    }
}