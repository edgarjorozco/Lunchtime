package com.edgarjorozco.lunchtime.presentation.search.vm

import android.text.Editable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.edgarjorozco.lunchtime.datasource.network.AutoCompletePrediction
import com.edgarjorozco.lunchtime.domain.DataState
import com.edgarjorozco.lunchtime.domain.LatLng
import com.edgarjorozco.lunchtime.domain.Place
import com.edgarjorozco.lunchtime.repository.AutoCompleteRepository
import com.edgarjorozco.lunchtime.repository.FavoritesRepository
import com.edgarjorozco.lunchtime.repository.NearbySearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject
constructor(
    private val autoCompleteRepo: AutoCompleteRepository,
    private val nearbySearchRepository: NearbySearchRepository,
    private val favoritesRepository: FavoritesRepository
) : ViewModel() {

    private val _searchResultsMode: MutableLiveData<SearchResultsMode> = MutableLiveData(SearchResultsMode.MapMode)
    val searchResultsMode: LiveData<SearchResultsMode> = _searchResultsMode

    fun onSearchResultsModeChangeRequest() {
        when (_searchResultsMode.value) {
            is SearchResultsMode.MapMode -> _searchResultsMode.value = SearchResultsMode.ListMode
            is SearchResultsMode.ListMode -> _searchResultsMode.value = SearchResultsMode.MapMode
            else -> { } // button isn't visible in tablet mode anyway
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
                // todo execute generic initial search
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
            nearbySearchRepository.getNearbyPlaces(latLng, radius?: DEFAULT_SEARCH_RADIUS).onEach {
                _resultsSource.value = SearchResultsSource.NearbySearch
                _placeResults.value = it
            }.launchIn(this)
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
            autoCompleteRepo.getPlaceSuggestions(input.toString(), latLng).onEach {
                _suggestionList.value = it
            }.launchIn(this)
        }
    }

    fun onSuggestionSelected(suggestion: AutoCompletePrediction) {
        viewModelScope.launch {
            autoCompleteRepo.getFullPlaceDetail(suggestion.place_id).onEach {
                when(it) {
                    is DataState.Success -> {
                        it.toData()?.let { place ->
                            _resultsSource.value = SearchResultsSource.SuggestionSelection
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
            favoritesRepository.getFavorites().map { placeMap ->
                _resultsSource.value = SearchResultsSource.Favorites
                _placeResults.value = placeMap
            }.launchIn(this)
        }
    }

    companion object {
        const val DEFAULT_SEARCH_RADIUS = 4000
        const val SEARCH_THRESHOLD = 3
    }
}