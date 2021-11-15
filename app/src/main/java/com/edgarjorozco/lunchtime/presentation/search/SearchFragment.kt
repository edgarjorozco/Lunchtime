package com.edgarjorozco.lunchtime.presentation.search

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.edgarjorozco.lunchtime.R
import com.edgarjorozco.lunchtime.databinding.FragmentSearchBinding
import com.edgarjorozco.lunchtime.datasource.network.AutoCompletePrediction
import com.edgarjorozco.lunchtime.domain.DataState
import com.edgarjorozco.lunchtime.domain.LatLng
import com.edgarjorozco.lunchtime.presentation.search.vm.LocationState
import com.edgarjorozco.lunchtime.presentation.search.vm.SearchResultsSource
import com.edgarjorozco.lunchtime.presentation.search.vm.SearchViewModel
import com.edgarjorozco.lunchtime.util.ui.LunchtimeBaseFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.hilt.android.AndroidEntryPoint
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.OnPermissionDenied
import permissions.dispatcher.RuntimePermissions

@AndroidEntryPoint
@RuntimePermissions
class SearchFragment: LunchtimeBaseFragment<FragmentSearchBinding>(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        requestLocationWithPermissionCheck()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding?.viewModel = viewModel
        dataBinding?.lifecycleOwner = viewLifecycleOwner

        setUpSearchBar()
    }

    private fun setUpSearchBar() {
        val arrayAdapter =
            ArrayAdapter<AutoCompletePrediction>(requireContext(), android.R.layout.simple_dropdown_item_1line)
        dataBinding?.includeSearchHeader?.apply {
            searchBar.setAdapter(arrayAdapter)
            searchBar.setOnItemClickListener { adapter, _, position, _ ->
                val selectedItem = adapter.getItemAtPosition(position)
                if ( selectedItem is AutoCompletePrediction) {
                    viewModel?.onSuggestionSelected(selectedItem)
                    hideSoftKeyBoard()
                }
            }
            searchBarClear.setOnClickListener{
                searchBar.setText("", false)
                viewModel?.onSearchNearby()
                hideSoftKeyBoard()
            }
        }

        viewModel.suggestionList.observe(viewLifecycleOwner, {
            when(it) {
                is DataState.Loading -> showLoading(true)
                is DataState.Error -> {
                    showError(it.toErrorMessage())
                    showLoading(false)
                }
                is DataState.Success -> {
                    it.data?.let { predictions ->
                        arrayAdapter.clear()
                        arrayAdapter.addAll(predictions)
                        arrayAdapter.notifyDataSetChanged()
                        if (predictions.size > 0)
                            dataBinding?.includeSearchHeader?.searchBar?.showDropDown()
                        showLoading(false)
                    }
                }
            }
        })

        viewModel.resultsSource.observe(viewLifecycleOwner,{
            when(it) {
                SearchResultsSource.SuggestionSelection -> { }
                else -> { // clear search bar for nearby and favorites
                    dataBinding?.includeSearchHeader?.searchBar?.apply {
                        setText("", false)
                        clearFocus()
                    }
                }
            }
        })
    }

    @SuppressLint("MissingPermission")
    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    fun requestLocation() {
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it == null) return@addOnSuccessListener
            viewModel.onUserLocationStateChange(LocationState.Precise(LatLng(it.latitude, it.longitude)))
        }
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    fun onLocationDenied() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.lastLocation.addOnSuccessListener {
                if (it == null) return@addOnSuccessListener
                viewModel.onUserLocationStateChange(LocationState.Coarse(LatLng(it.latitude, it.longitude))
                )
            }
        } else {
            viewModel.onUserLocationStateChange(LocationState.None)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        onRequestPermissionsResult(requestCode, grantResults)
    }
}