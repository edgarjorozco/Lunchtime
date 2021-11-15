package com.edgarjorozco.lunchtime.presentation.placedetail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.edgarjorozco.lunchtime.R
import com.edgarjorozco.lunchtime.databinding.FragmentPlaceDetailBinding
import com.edgarjorozco.lunchtime.models.DataState
import com.edgarjorozco.lunchtime.models.Place
import com.edgarjorozco.lunchtime.util.ui.LunchtimeBaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlaceDetailFragment: LunchtimeBaseFragment<FragmentPlaceDetailBinding>(R.layout.fragment_place_detail) {
    private val viewModel: PlaceDetailViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding?.toolbar?.setNavigationOnClickListener {
            activity?.finish()
        }
        viewModel.placeDetail.observe(viewLifecycleOwner, {
            when (it) {
                is DataState.Error -> {
                    showLoading(false)
                    showError(it.toErrorMessage())
                }
                is DataState.Loading -> showLoading(true)
                is DataState.Success -> {
                    showLoading(false)
                    bindData(it.data!!)
                }
            }
        })
    }

    private fun bindData(place: Place) {
        dataBinding?.place = place
        place.photos?.let {
            dataBinding?.photoPager?.adapter = PhotoPagerAdapter(it)
        }
    }

    companion object {
        const val ARG_PLACE_ID = "PlaceDetailFragment.placeId"
        fun newInstance(placeId: String?): PlaceDetailFragment {
            val arguments = Bundle()
            arguments.putString(ARG_PLACE_ID, placeId)
            val fragment = PlaceDetailFragment()
            fragment.arguments = arguments
            return fragment
        }
    }
}