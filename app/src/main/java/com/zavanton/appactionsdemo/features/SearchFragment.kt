package com.zavanton.appactionsdemo.features

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.zavanton.appactionsdemo.R
import com.zavanton.appactionsdemo.databinding.FragmentSearchBinding

class SearchFragment : Fragment() {

    companion object {
        private const val SEARCH_QUERY = "SEARCH_QUERY"

        fun newInstance(searchQuery: String): SearchFragment =
            SearchFragment().also { fragment ->
                fragment.arguments = Bundle().apply {
                    putString(SEARCH_QUERY, searchQuery)
                }
            }
    }

    private lateinit var binding: FragmentSearchBinding

    private val searchQuery by lazy {
        arguments?.getString(SEARCH_QUERY).orEmpty()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tvSearch.text = getString(R.string.search_query, searchQuery)
        // implement some search logic...
    }
}
