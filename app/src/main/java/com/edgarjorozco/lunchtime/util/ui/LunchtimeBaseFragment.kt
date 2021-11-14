package com.edgarjorozco.lunchtime.util.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class LunchtimeBaseFragment<T: ViewDataBinding> constructor(@LayoutRes private val layoutId: Int): Fragment() {
    open var dataBinding: T? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        return dataBinding?.root
    }

    open fun showLoading() {
        //todo
    }

    open fun showError(error: String?) {
        //todo
    }
    fun hideSoftKeyBoard() {
        requireActivity().currentFocus?.let {
            val inputMethodManager = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        }
    }
}