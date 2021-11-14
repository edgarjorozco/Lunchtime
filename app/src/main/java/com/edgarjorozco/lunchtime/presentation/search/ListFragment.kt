package com.edgarjorozco.lunchtime.presentation.search

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.edgarjorozco.lunchtime.R
import com.edgarjorozco.lunchtime.databinding.SearchListFragmentBinding
import com.edgarjorozco.lunchtime.domain.DataState
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
        dataBinding?.placeRecycler?.layoutManager = layoutManager
        dataBinding?.placeRecycler?.addItemDecoration(SpaceDecoration(DECORATOR_SPACE_DP.dpToPx(requireContext())))

        val adapter = PlaceListAdapter {
            viewModel.onFavoriteListing(it)
        }
        dataBinding?.placeRecycler?.adapter = adapter

        viewModel.placeResults.observe(viewLifecycleOwner, {
            when(it) {
                is DataState.Error -> showError(it.toErrorMessage())
                DataState.Loading -> showLoading()
                is DataState.Success -> adapter.submitList(it.data?.values?.toList())
            }
        })

    }

    companion object {
        const val DECORATOR_SPACE_DP = 10f
    }
}