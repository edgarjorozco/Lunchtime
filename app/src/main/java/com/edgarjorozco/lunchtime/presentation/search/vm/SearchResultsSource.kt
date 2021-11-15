package com.edgarjorozco.lunchtime.presentation.search.vm

sealed class SearchResultsSource {
    object NearbySearch: SearchResultsSource()
    object SuggestionSelection: SearchResultsSource()
    object Favorites: SearchResultsSource()
}
