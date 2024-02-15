import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.util.reflect.Type
import io.ktor.util.reflect.TypeInfo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import model.Photo
import model.PhotosResponse

data class ListPhotosUiState(
    val photosList: List<Photo> = emptyList()
)

class ListPhotosViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ListPhotosUiState())
    val uiState = _uiState.asStateFlow()

    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
                encodeDefaults = false
            })
        }
    }

    init {
        updatePhotos()
    }

    override fun onCleared() {
        httpClient.close()
        super.onCleared()
    }

    private fun updatePhotos() {
        viewModelScope.launch {
            val photoList = getListPhotos()
            photoList.let {
                _uiState.update {
                    it.copy(photosList = photoList)
                }
            }
        }
    }

    private suspend fun getListPhotos(): List<Photo> {
        val images = httpClient
            .get("https://api.unsplash.com/search/photos?query=dogs&client_id=_I5fH2c27ZhPjCj9wEQ2urCxDsh3MeYpDBybR_uJNwc")
            .body<PhotosResponse>()
        return images.results
    }
}