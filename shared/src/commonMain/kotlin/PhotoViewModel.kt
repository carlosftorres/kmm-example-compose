import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.Photo

data class PhotoUiState(
    val photo: Photo? = null
)

class PhotoViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(PhotoUiState())
    val uiState = _uiState.asStateFlow()

    private val httpClient: HttpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    init {
        updatePhoto()
    }

    override fun onCleared() {
        httpClient.close()
        super.onCleared()
    }

    private fun updatePhoto() {
        viewModelScope.launch {
            val photo = getPhoto()
            _uiState.update {
                it.copy(photo = photo)
            }
        }
    }
    private suspend fun getPhoto(): Photo {
        return httpClient.get("https://api.unsplash.com/photos/BJaqPaH6AGQ?client_id=_I5fH2c27ZhPjCj9wEQ2urCxDsh3MeYpDBybR_uJNwc").body()
    }
}