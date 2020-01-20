package com.awkris.watchamovie.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.awkris.watchamovie.R
import com.awkris.watchamovie.data.model.NetworkState
import com.awkris.watchamovie.presentation.common.ItemMovieClickListener
import com.awkris.watchamovie.presentation.common.MovieListAdapter
import com.awkris.watchamovie.presentation.main.FragmentListener
import com.awkris.watchamovie.presentation.moviedetail.MovieDetailFragment
import com.awkris.watchamovie.utils.closeKeyboard
import kotlinx.android.synthetic.main.fragment_search_movie.*
import org.koin.android.viewmodel.ext.android.viewModel


class SearchFragment : Fragment() {
    private val viewModel: SearchViewModel by viewModel()
    private lateinit var fragmentListener: FragmentListener

    override fun onAttach(context: Context) {
        super.onAttach(context)

        assert(context is FragmentListener)
        fragmentListener = context as FragmentListener
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
        swipe_refresh.setOnRefreshListener { viewModel.search() }
        initRecyclerView()

        srv_movie.setOnQueryTextListener(
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    closeKeyboard(requireContext(), srv_movie.windowToken)
                    if (!query.isNullOrEmpty()) viewModel.search(query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return true
                }
            }
        )
    }

    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(context)
        val adapter = MovieListAdapter().apply {
            itemMovieClickListener = object :
                ItemMovieClickListener {
                override fun onItemClicked(id: Int) {
                    findNavController().navigate(
                        R.id.search_to_movieDetailFragment,
                        MovieDetailFragment.createBundle(id)
                    )
                }
            }
        }

        viewModel.searchList.observe(viewLifecycleOwner, Observer { adapter.submitList(it) })
        viewModel.networkState.observe(viewLifecycleOwner, Observer {
            adapter.networkState = it
            when (it) {
                is NetworkState.Error, NetworkState.Success -> swipe_refresh.isRefreshing = false
            }
        })

        rcv_search_list.layoutManager = layoutManager
        rcv_search_list.adapter = adapter
    }

    companion object {
        fun newInstance() = SearchFragment()
    }
}