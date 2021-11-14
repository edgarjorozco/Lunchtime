package com.edgarjorozco.lunchtime.presentation.search.vm

sealed class SearchResultsMode{
    object MapMode: SearchResultsMode()
    object ListMode: SearchResultsMode()
    object TabletMode: SearchResultsMode()
}