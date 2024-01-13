package com.finance.growwassignment.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.paging.map
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.finance.growwassignment.MovieDatabase
import com.finance.growwassignment.R
import com.finance.growwassignment.databinding.FragmentFilmsBinding
import com.finance.growwassignment.models.Result
import com.finance.growwassignment.paging.CharacterMovieAdapter
import com.finance.growwassignment.paging.CharacterMoviePagingSource
import com.finance.growwassignment.utilities.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class FilmsFragment : Fragment() {
    private var _binding: FragmentFilmsBinding? = null
    private val binding get() = _binding!!
    private var characterData: Result? = null
    private val adapter = CharacterMovieAdapter()

    @Inject
    lateinit var movieDatabase: MovieDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFilmsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments
        setupRecyclerView()

        // Check if the arguments bundle is not null
        if (arguments != null) {
            // Retrieve the Result object from the bundle
            characterData = arguments.getSerializable("result") as Result?

            // Use the result object as needed
            if (characterData != null) {
                val films: List<String> = characterData!!.films

                lifecycleScope.launch {
                    // Load moviesName list into PagingData
                    val moviesPagingData = Pager(
                        config = PagingConfig(pageSize = 10),
                        pagingSourceFactory = { CharacterMoviePagingSource(films, movieDatabase) }
                    ).flow.cachedIn(lifecycleScope)
                    moviesPagingData.collectLatest {
                        it.map { movieResult ->
                        }
                        adapter.submitData(it)
                    }
                }
            }
        }
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter

        val spacing =
            resources.getDimensionPixelSize(R.dimen.grid_spacing) // set your desired spacing
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, true))    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
