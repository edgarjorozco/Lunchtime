package com.edgarjorozco.lunchtime.presentation.placedetail

import androidx.lifecycle.*
import com.edgarjorozco.lunchtime.models.DataState
import com.edgarjorozco.lunchtime.models.Place
import com.edgarjorozco.lunchtime.repository.PlaceDetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailViewModel @Inject
constructor(
    savedStateHandle: SavedStateHandle,
    private val detailRepository: PlaceDetailRepository
): ViewModel(){

    private val _placeDetail: MutableLiveData<DataState<Place>> = MutableLiveData()
    val placeDetail: LiveData<DataState<Place>> = _placeDetail

    init {
        val placeId = savedStateHandle.get<String>(PlaceDetailFragment.ARG_PLACE_ID)
        placeId?.let {
            viewModelScope.launch {
                detailRepository.getFullPlaceDetail(placeId).onEach {
                    _placeDetail.value = it
                }.launchIn(this)
            }
        }
    }
}