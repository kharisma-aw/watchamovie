package com.awkris.watchamovie.presentation.home

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.awkris.watchamovie.R
import com.awkris.watchamovie.presentation.main.FragmentListener
import kotlinx.android.synthetic.main.fragment_viewpager.*

class HomeFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_viewpager, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewpager.adapter = HomeViewPagerAdapter(childFragmentManager)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_home, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_search -> {
                findNavController().navigate(R.id.searchFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}