package com.awkris.watchamovie.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.presentation.common.ItemMovieClickListener
import com.awkris.watchamovie.presentation.common.MovieListAdapter
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailFragment
import com.awkris.watchamovie.presentation.search.lastkeyword.KeywordClickListener
import com.awkris.watchamovie.presentation.search.lastkeyword.KeywordsAdapter
import com.awkris.watchamovie.utils.closeKeyboard
import kotlinx.android.synthetic.main.fragment_refreshable_list.*
import kotlinx.android.synthetic.main.fragment_search_movie.*
import org.koin.android.viewmodel.ext.android.sharedViewModel

class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by sharedViewModel()
    private val keywordsAdapter = KeywordsAdapter().also {
        it.itemClickListener = object : KeywordClickListener {
            override fun onKeywordClicked(s: String) {
                srv_movie.setQuery(s, true)
                srv_movie.clearFocus()
            }

        }
    }
    private val searchListAdapter = MovieListAdapter().also {
        it.itemMovieClickListener = object : ItemMovieClickListener {
            override fun onItemClicked(id: Int) {
                findNavController().navigate(
                    R.id.search_to_movieDetailFragment,
                    MovieDetailFragment.createBundle(id)
                )
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_search_movie, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        swipe_refresh.isEnabled = false
        initObserver()
        initRecyclerView()

        srv_movie.setOnQueryTextFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                rcv_list.adapter = keywordsAdapter
            } else {
                rcv_list.adapter = searchListAdapter
            }
        }

        srv_movie.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    closeKeyboard(requireContext(), srv_movie.windowToken)
                    srv_movie.clearFocus()
                    if (!query.isNullOrEmpty()) {
                        viewModel.search(query)
                        viewModel.saveKeyword(query)
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            }
        )
    }

    override fun onResume() {
        super.onResume()
        rcv_list.adapter = searchListAdapter
    }

    override fun onPause() {
        srv_movie.clearFocus()
        super.onPause()
    }

    override fun onDestroy() {
        viewModel.dispose()
        super.onDestroy()
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        rcv_list.layoutManager = layoutManager
    }

    private fun initObserver() {
        viewModel.searchList.observe(viewLifecycleOwner, Observer {
            searchListAdapter.submitList(it)
        })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            searchListAdapter.setNetworkState(it)
            when (it) {
                is NetworkState.Error, NetworkState.Success -> {
                    if (swipe_refresh.isRefreshing) swipe_refresh.isRefreshing = false
                }
            }
        })
        viewModel.lastKeywords.observe(viewLifecycleOwner, Observer {
            keywordsAdapter.submitList(it)
        })
    }
}