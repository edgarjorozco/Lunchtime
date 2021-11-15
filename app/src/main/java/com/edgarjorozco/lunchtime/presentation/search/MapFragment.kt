package com.edgarjorozco.lunchtime.presentation.search

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.edgarjorozco.lunchtime.R
import com.edgarjorozco.lunchtime.databinding.ListItemPlaceBinding
import com.edgarjorozco.lunchtime.domain.DataState
import com.edgarjorozco.lunchtime.domain.Place
import com.edgarjorozco.lunchtime.presentation.search.vm.LocationState
import com.edgarjorozco.lunchtime.presentation.search.vm.SearchResultsSource
import com.edgarjorozco.lunchtime.presentation.search.vm.SearchViewModel
import com.edgarjorozco.lunchtime.util.dpToPx
import com.edgarjorozco.lunchtime.util.generateGooglePlacesPhotoUrl
import com.edgarjorozco.lunchtime.util.metersAcrossMinimumDimension
import com.edgarjorozco.lunchtime.util.toBitmap
import com.google.android.gms.maps.*
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_API_ANIMATION
import com.google.android.gms.maps.GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE
import com.google.android.gms.maps.model.*


class MapFragment: SupportMapFragment(), GoogleMap.OnMarkerClickListener, OnMapReadyCallback,
    GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraIdleListener, GoogleMap.OnMapClickListener {
    companion object {
        const val DEFAULT_ZOOM_LEVEL = 15f
    }

    private val viewModel: SearchViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )
    private var googleMap: GoogleMap? = null
    private var selectedMarker: Marker? = null
    private var cameraChangeReason = REASON_API_ANIMATION
    private var moveToResults = false

    private val activeBitmap by lazy {
        val drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.active_pin)
        if (drawable != null) BitmapDescriptorFactory.fromBitmap(drawable.toBitmap())
        else BitmapDescriptorFactory.defaultMarker()
    }
    private val inactiveBitmap by lazy {
        val drawable = AppCompatResources.getDrawable(requireContext(), R.drawable.inactive_pin)
        if (drawable != null) BitmapDescriptorFactory.fromBitmap(drawable.toBitmap())
        else BitmapDescriptorFactory.defaultMarker()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MapsInitializer.initialize(requireContext())
        getMapAsync(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeMapCenter()
        observeNearbyPlaces()
        observeUserLocationState()
        observeResultsSource()
    }

    private fun observeNearbyPlaces() {
        viewModel.placeResults.observe(viewLifecycleOwner, {
            when(it) {
                is DataState.Success -> {
                    googleMap?.clear()
                    selectedMarker = null
                    val builder = LatLngBounds.builder()
                    it.data?.forEach { (_, place) ->
                        val marker = googleMap?.addMarker(
                            MarkerOptions()
                                .position(LatLng(place.location.lat, place.location.lng))
                                .title(place.name)
                                .icon(inactiveBitmap)
                        )
                        marker?.tag = place
                        builder.include(LatLng(place.location.lat, place.location.lng))
                    }

                    if (moveToResults) {
                        val newBounds = builder.build()
                        val padding = 20f.dpToPx(requireContext())
                        googleMap?.animateCamera(CameraUpdateFactory.newLatLngBounds(newBounds, padding))
                    }
                }
                else -> { }
            }

        })
    }

    private fun observeMapCenter() {
        viewModel.newMapCenterLocation.observe(viewLifecycleOwner, {
            googleMap?.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.lat, it.lng), DEFAULT_ZOOM_LEVEL))
        })
    }

    private fun observeResultsSource() {
        viewModel.resultsSource.observe(viewLifecycleOwner, {
            moveToResults = when(it) {
                SearchResultsSource.Favorites -> true
                else -> false
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun observeUserLocationState() {
        viewModel.userLocationState.observe(viewLifecycleOwner, {
            when (it) {
                is LocationState.None -> { }
                else -> {
                    googleMap?.isMyLocationEnabled = true
                }
            }
        })
    }

    @SuppressLint("PotentialBehaviorOverride")
    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap?.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
        googleMap?.setOnCameraMoveStartedListener(this)
        googleMap?.setOnCameraIdleListener(this)
        googleMap?.setOnMarkerClickListener(this)
        googleMap?.setOnMapClickListener(this)

        googleMap?.setInfoWindowAdapter(
            object : GoogleMap.InfoWindowAdapter {
                override fun getInfoContents(marker: Marker): View? = null
                override fun getInfoWindow(marker: Marker): View {
                    val binding = ListItemPlaceBinding.inflate(layoutInflater)
                    val place = marker.tag as Place
                    binding.place = place
                    binding.favoriteButton.isVisible = false
                    binding.executePendingBindings()
                    // InfoWindow is not dynamic, gets turned into a Bitmap when it's passed back
                    val url = requireContext()
                        .generateGooglePlacesPhotoUrl(binding.placeImage.maxWidth,
                            place.photos?.get(0)?.photoReference?:"")

                    Glide.with(binding.placeImage)
                        .load(url)
                        .centerCrop()
                        .placeholder(R.color.at_grey)
                        .listener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                return false
                            }
                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                                if (dataSource != DataSource.MEMORY_CACHE) marker.showInfoWindow()
                                return false
                            }
                        })
                        .into(binding.placeImage)
                    return binding.root
                }
            }
        )
    }

    override fun onCameraMoveStarted(reason: Int) {
        cameraChangeReason = reason
    }

    override fun onCameraIdle() {
        if (cameraChangeReason == REASON_GESTURE) {
            googleMap?.cameraPosition?.target?.let {
                val radius = googleMap?.metersAcrossMinimumDimension()
                viewModel.onSearchNearby(it.latitude, it.longitude, radius?.toInt())
            }
        }
    }

    override fun onMarkerClick(marker: Marker): Boolean {
        if (selectedMarker != null) selectedMarker?.setIcon(inactiveBitmap)

        selectedMarker = marker
        selectedMarker?.setIcon(activeBitmap)
        return false
    }

    override fun onMapClick(latLng: LatLng) {
        if (selectedMarker != null) selectedMarker?.setIcon(inactiveBitmap)
        selectedMarker = null
    }
}