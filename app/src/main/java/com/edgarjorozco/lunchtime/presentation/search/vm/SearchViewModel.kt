package com.edgarjorozco.lunchtime.presentation.search.vm

import android.text.Editable
import androidx.lifecycle.*
import com.edgarjorozco.lunchtime.datasource.network.AutoCompletePrediction
import com.edgarjorozco.lunchtime.models.DataState
import com.edgarjorozco.lunchtime.models.LatLng
import com.edgarjorozco.lunchtime.models.Place
import com.edgarjorozco.lunchtime.repository.FavoritesRepository
import com.edgarjorozco.lunchtime.repository.NearbySearchRepository
import com.edgarjorozco.lunchtime.usecases.AutoCompleteUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject
constructor(
    private val autoCompleteUseCases: AutoCompleteUseCases,
    private val nearbySearchRepository: NearbySearchRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _searchResultsMode: MutableLiveData<SearchResultsMode> = MutableLiveData(SearchResultsMode.MapMode)
    val searchResultsMode: LiveData<SearchResultsMode> = _searchResultsMode

    fun onSearchResultsModeChangeRequest() {
        when (_searchResultsMode.value) {
            is SearchResultsMode.MapMode -> _searchResultsMode.value = SearchResultsMode.ListMode
            is SearchResultsMode.ListMode -> _searchResultsMode.value = SearchResultsMode.MapMode
            else -> { } // button isn't visible in tablet mode
        }
    }

    private val _newMapCenterLocation: MutableLiveData<LatLng> = MutableLiveData()
    val newMapCenterLocation: LiveData<LatLng> = _newMapCenterLocation

    private val _userLocationState: MutableLiveData<LocationState> = MutableLiveData()
    val userLocationState: LiveData<LocationState> = _userLocationState

    fun onUserLocationStateChange(location: LocationState) {
        _userLocationState.value = location
        when (location) {
            is LocationState.None -> {
                _newMapCenterLocation.value = DEFAULT_SEARCH_LOCATION
                onSearchNearby(DEFAULT_SEARCH_LOCATION.lat, DEFAULT_SEARCH_LOCATION.lng)
            }
            else -> {
                val userLatLng = location.toData()!!
                _newMapCenterLocation.value = userLatLng
                onSearchNearby(userLatLng.lat, userLatLng.lng)
            }
        }
    }

    private val _placeResults: MutableLiveData<DataState<Map<String, Place>?>> = MutableLiveData()
    val placeResults: LiveData<DataState<Map<String, Place>?>> = _placeResults

    private val _resultsSource: MutableLiveData<SearchResultsSource> = MutableLiveData()
    val resultsSource: LiveData<SearchResultsSource> = _resultsSource

    fun onSearchNearby(lat: Double? = null, lng: Double? = null, radius: Int? = DEFAULT_SEARCH_RADIUS) {
        val latLng =
            (if (lat == null || lng == null)
                _newMapCenterLocation.value
            else LatLng(lat, lng))
                ?: return

        viewModelScope.launch {
            nearbySearchRepository.getNearbyPlaces(latLng, radius?: DEFAULT_SEARCH_RADIUS).onStart {
                _resultsSource.value = SearchResultsSource.NearbySearch
            }.onEach {
                _placeResults.value = it
            }.launchIn(viewModelScope)
        }
    }

    private val _suggestionList: MutableLiveData<DataState<ArrayList<AutoCompletePrediction>?>> = MutableLiveData()
    val suggestionList: LiveData<DataState<ArrayList<AutoCompletePrediction>?>> = _suggestionList

    fun onFetchPlaceSuggestions(input: Editable?) {
        if (input == null || input.length < SEARCH_THRESHOLD) {
            if (_suggestionList.value?.toData() != null) _suggestionList.value = DataState.Success(null)
            return
        }

        viewModelScope.launch {
            val latLng = _newMapCenterLocation.value?.let { LatLng(it.lat, it.lng) }
            autoCompleteUseCases.getPlaceSuggestions(input.toString(), latLng).onEach {
                _suggestionList.value = it
            }.launchIn(this)
        }
    }

    fun onSuggestionSelected(suggestion: AutoCompletePrediction) {
        viewModelScope.launch {
            autoCompleteUseCases.getFullPlaceDetail(suggestion.place_id).onStart {
                _resultsSource.value = SearchResultsSource.SuggestionSelection
            }.onEach {
                when(it) {
                    is DataState.Success -> {
                        it.toData()?.let { place ->
                            _placeResults.value = DataState.Success(mapOf(Pair(place.placeId, place)))
                            _newMapCenterLocation.value = place.location
                        }
                    }
                    is DataState.Error -> _placeResults.value = it
                    DataState.Loading -> _placeResults.value = DataState.Loading
                }
            }.launchIn(this)
        }
    }

    fun onFavoriteListing(place: Place) {
        viewModelScope.launch {
            favoritesRepository.favorite(place, place.isFavorite)
        }
    }

    fun onFavoritesRequested() {
        viewModelScope.launch {
            favoritesRepository.getFavorites().onStart{
                _resultsSource.value = SearchResultsSource.Favorites
            }.onEach {
                _placeResults.value = it
            }.launchIn(this)
        }
    }

    companion object {
        const val DEFAULT_SEARCH_RADIUS = 4000
        const val SEARCH_THRESHOLD = 3
        val DEFAULT_SEARCH_LOCATION = LatLng(34.0522, -118.2437) // Los Angeles
    }
}