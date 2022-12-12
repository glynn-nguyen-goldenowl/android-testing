package com.example.androidtesting.ui.picker

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import com.example.androidtesting.data.DataResult
import com.example.androidtesting.data.source.ShoppingRepository
import com.example.androidtesting.data.source.remote.ImageResult
import com.example.androidtesting.util.Async
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImagePickViewModel  @Inject constructor(
    val shoppingRepository: ShoppingRepository,
    val stateHandle: SavedStateHandle
    ) : ViewModel() {

    private val _uiState : MutableStateFlow<ImageListUiState> = MutableStateFlow(ImageListUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: ImageListUiEvent){
        when(event){
            is ImageListUiEvent.Search ->{
                searchImage(event.searchIndex)
            }
        }
    }

    var searchJob: Job? = null
    private fun searchImage(searchIndex: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(200L)
            shoppingRepository.searchImage(searchIndex)
                .map { Async.Success(it) }
                .onStart<Async<DataResult<List<ImageResult>>>> { emit(Async.Loading) }
                .map { produceUiState(it) }
                .collect{
                    _uiState.emit(it)
                }

        }

    }

    private fun produceUiState(it: Async<DataResult<List<ImageResult>>>) : ImageListUiState {
        return when(it){
            Async.Loading ->{
                _uiState.value.copy(isLoading = true)
            }
            is Async.Success ->{
                when(it.data){
                    is DataResult.Success ->{
                        ImageListUiState(imageUrls = it.data.data.map { it.url })
                    }
                    is DataResult.Error ->{
                        ImageListUiState(errorMessage = it.data.error.message)
                    }
                }
            }
        }
    }


}