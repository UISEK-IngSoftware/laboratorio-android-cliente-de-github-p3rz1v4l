package ec.edu.uisek.githubclient.services

import ec.edu.uisek.githubclient.models.Repo
import ec.edu.uisek.githubclient.models.RepoRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * Interfaz que define los endpoints de la API de GitHub utilizando Retrofit.
 * Cada método corresponde a una operación específica de la API (obtener, crear, editar, eliminar repos).
 */
interface GithubApiService {
    /**
     * Obtiene la lista de repositorios del usuario autenticado.
     * @param sort Criterio para ordenar los resultados (por defecto: "created").
     * @param direction Dirección de la ordenación (por defecto: "desc").
     * @return Una llamada [Call] que, al ejecutarse, devuelve una lista de [Repo].
     */
    @GET("user/repos")
    fun getRepos(
        @Query("sort") sort: String = "created",
        @Query("direction") direction: String = "desc"
    ): Call<List<Repo>>

    /**
     * Crea un nuevo repositorio para el usuario autenticado.
     * @param repoRequest Objeto con los datos del nuevo repositorio (nombre, descripción, etc.).
     * @return Una llamada [Call] que devuelve el [Repo] recién creado.
     */
    @POST("user/repos")
    fun addRepo(
        @Body repoRequest: RepoRequest
    ): Call<Repo>

    /**
     * Actualiza un repositorio existente.
     * @param owner El nombre del propietario del repositorio.
     * @param repo El nombre del repositorio a actualizar.
     * @param repoRequest Objeto con los nuevos datos para el repositorio.
     * @return Una llamada [Call] que devuelve el [Repo] actualizado.
     */
    @PATCH("repos/{owner}/{repo}")
    fun updateRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String,
        @Body repoRequest: RepoRequest
    ): Call<Repo>

    /**
     * Elimina un repositorio.
     * @param owner El nombre del propietario del repositorio.
     * @param repo El nombre del repositorio a eliminar.
     * @return Una llamada [Call] sin cuerpo de respuesta ([Unit]), ya que la API devuelve un 204 No Content.
     */
    @DELETE("repos/{owner}/{repo}")
    fun deleteRepo(
        @Path("owner") owner: String,
        @Path("repo") repo: String
    ): Call<Unit>
}
