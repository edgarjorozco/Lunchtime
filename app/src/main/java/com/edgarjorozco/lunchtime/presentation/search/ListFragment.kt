package com.edgarjorozco.lunchtime.presentation.search

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.edgarjorozco.lunchtime.R
import com.edgarjorozco.lunchtime.databinding.SearchListFragmentBinding
import com.edgarjorozco.lunchtime.models.DataState
import com.edgarjorozco.lunchtime.presentation.placedetail.PlaceDetailActivity
import com.edgarjorozco.lunchtime.presentation.search.vm.SearchViewModel
import com.edgarjorozco.lunchtime.util.dpToPx
import com.edgarjorozco.lunchtime.util.ui.LunchtimeBaseFragment
import com.edgarjorozco.lunchtime.util.ui.SpaceDecoration

class ListFragment: LunchtimeBaseFragment<SearchListFragmentBinding>(R.layout.search_list_fragment) {
    private val viewModel: SearchViewModel by viewModels(
        ownerProducer = { requireParentFragment() }
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dataBinding?.viewModel = viewModel
        dataBinding?.lifecycleOwner = viewLifecycleOwner
        setUpRecycler()
    }

    private fun setUpRecycler() {
        val layoutManager = LinearLayoutManager(requireContext())
        val adapter = PlaceListAdapter (
            { viewModel.onFavoriteListing(it) },
            { openDetail(it.placeId) }
        )

        dataBinding?.placeRecycler?.apply {
            this.layoutManager = layoutManager
            addItemDecoration(SpaceDecoration(DECORATOR_SPACE_DP.dpToPx(requireContext())))
            this.adapter = adapter
        }

        viewModel.placeResults.observe(viewLifecycleOwner, {
            when(it) {
                is DataState.Loading -> showLoading(true)
                is DataState.Error -> {
                    showError(it.toErrorMessage())
                    showLoading(false)
                }
                is DataState.Success -> {
                    adapter.submitList(it.data?.values?.toList())
                    showLoading(false)
                }
            }
        })

    }

    override fun showLoading(isLoading: Boolean) {
        dataBinding?.placeRecycler?.isVisible = !isLoading
        dataBinding?.includeShimmerPlaceholder?.let {
            if (isLoading){
                it.shimmerContainer.isVisible = true
                it.shimmerContainer.startShimmer()
            } else {
                it.shimmerContainer.isVisible = false
                it.shimmerContainer.stopShimmer()
            }
        }
    }

    private fun openDetail(placeId: String) {
        val intent = PlaceDetailActivity.getCallingIntent(requireContext(), placeId)
        startActivity(intent)
    }

    companion object {
        const val DECORATOR_SPACE_DP = 10f
    }
}