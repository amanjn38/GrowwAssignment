package com.finance.growwassignment.ui.screens

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.finance.growwassignment.R
import com.finance.growwassignment.databinding.FilterViewBinding
import com.finance.growwassignment.databinding.FragmentCharacterBinding
import com.finance.growwassignment.databinding.SortViewBinding
import com.finance.growwassignment.models.Result
import com.finance.growwassignment.paging.CharacterPagingAdapter
import com.finance.growwassignment.paging.LoaderAdapter
import com.finance.growwassignment.utilities.FilterType
import com.finance.growwassignment.utilities.GridSpacingItemDecoration
import com.finance.growwassignment.utilities.OnItemClickListener
import com.finance.growwassignment.viewmodels.CharacterViewModel
import com.finance.growwassignment.viewmodels.MovieViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.tabs.TabLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class CharacterFragment : Fragment(), OnItemClickListener<Result> {
    private var _binding: FragmentCharacterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CharacterViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var adapter: CharacterPagingAdapter
    private var lastCheckedRadioButtonId = -1
    private var isLoading = false
    private var doubleBackToExitPressedOnce = false
    private var selectedFilter: FilterType? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCharacterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        saveMovieDetailsIntoRoom()
        adapter = CharacterPagingAdapter(this)

        val spacing =
            resources.getDimensionPixelSize(R.dimen.grid_spacing) // set your desired spacing
        val gridLayoutManager = GridLayoutManager(context, 2)
        binding.recyclerView.layoutManager = gridLayoutManager
        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.addItemDecoration(GridSpacingItemDecoration(2, spacing, true))
        binding.recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
            header = LoaderAdapter(),
            footer = LoaderAdapter()
        )
//        binding.recyclerView.setHasFixedSize(true)
        viewModel.getCharactersSorted(null).observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
        }

        initViews()

    }

    private fun showSortView() {

        sortBottomSheetDialog.show()
    }

    private fun saveMovieDetailsIntoRoom() {
        movieViewModel.getMovies()
    }

    private fun initViews() {
        initSortBottomSheetDialog()
        initFilterBottomSheetDialog()
        setClickListeners()

    }

    private fun setClickListeners() {

        binding.sortButton.setOnClickListener {
            System.out.println("testingBottom2")
            showSortView()
        }

        binding.filterButton.setOnClickListener {
            showFilterView()
        }
    }


    private fun showFilterView() {
        filterBottomSheetDialog.show()
    }

    private fun initSortBottomSheetDialog() {
        sortViewBinding = SortViewBinding.inflate(layoutInflater)

        sortBottomSheetDialog = BottomSheetDialog(requireActivity())
        sortBottomSheetDialog.setContentView(sortViewBinding.root)
        sortBottomSheetDialog.dismissWithAnimation = true
        lastCheckedRadioButtonId = -1
        initSortPageViews()
    }

    private fun initFilterBottomSheetDialog() {
        filterViewBinding = FilterViewBinding.inflate(layoutInflater)

        filterBottomSheetDialog = BottomSheetDialog(requireActivity())
        filterBottomSheetDialog.setContentView(filterViewBinding.root)
        filterBottomSheetDialog.dismissWithAnimation = true

        initFilterPageViews()
    }

    private fun initFilterPageViews() {
        filterViewBinding.btApply.setOnClickListener {
            hideFilterView()

            applyFilter()
        }
        val colorNames = resources.getStringArray(R.array.color_names)
        val hairColorAdapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            colorNames
        )
        filterViewBinding.spHairColor.adapter = hairColorAdapter

        filterViewBinding.spHairColor.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    // Handle the selected item
                    handleFilterSelection(FilterType.HAIR_COLOR)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    // Handle case where nothing is selected
                }
            }

        filterViewBinding.seebarHeight.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // You can add logic here if needed
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No specific action needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                handleFilterSelection(FilterType.HEIGHT)
            }
        })

        filterViewBinding.seebarMass.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                // You can add logic here if needed
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                // No specific action needed
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                handleFilterSelection(FilterType.MASS)
            }
        })
    }

    private fun applyFilter() {
        when (selectedFilter) {
            FilterType.HAIR_COLOR -> {
                // Apply hair color filter
                val selectedHairColor = filterViewBinding.spHairColor.selectedItem.toString()
                viewModel.getCharactersFilterByHairColor(selectedHairColor)
                    .observe(viewLifecycleOwner) { pagingData ->
                        adapter.submitData(lifecycle, pagingData)
                    }

            }

            FilterType.HEIGHT -> {
                // Apply height filter
                val selectedHeight = filterViewBinding.seebarHeight.progress.toString()
                System.out.println("testingheight" + selectedHeight)
                viewModel.getCharactersFilterByHeight("65", selectedHeight)
                    .observe(viewLifecycleOwner) { pagingData ->
                        adapter.submitData(lifecycle, pagingData)
                    }

            }

            FilterType.MASS -> {
                // Apply mass filter
                val selectedMass = filterViewBinding.seebarMass.progress.toString()
                viewModel.getCharactersFilterByMass("15", selectedMass)
                    .observe(viewLifecycleOwner) { pagingData ->
                        adapter.submitData(lifecycle, pagingData)
                    }

            }

            else -> {
                // No filter selected
                // You can show a message or handle it based on your requirement
            }
        }
    }

    private fun handleFilterSelection(filterType: FilterType) {
        // Clear previous selection
        selectedFilter = null

        // Set the current selection
        selectedFilter = filterType

        // You can add visual indication of the selected filter if needed
    }


    private fun hideFilterView() {
        filterBottomSheetDialog.dismiss()
    }

    private fun initSortPageViews() {


        sortViewBinding.btApply.setOnClickListener {
            hideSortView()

            val checkedId = sortViewBinding.sortRadioGroup.checkedRadioButtonId

            if (lastCheckedRadioButtonId == checkedId) {
                return@setOnClickListener
            }

            when (checkedId) {
                R.id.sortByName -> {
                    showLoadingIndicator()
                    viewModel.getCharactersSortedByName()
                        .observe(viewLifecycleOwner) { pagingData ->
                            adapter.submitData(lifecycle, pagingData)
//                            binding.recyclerView.postDelayed({
//                                binding.recyclerView.layoutManager?.scrollToPosition(0)
//                            }, 200)
                        }
                    hideLoadingIndicator()
                }

                R.id.sortByHeight -> {
                    showLoadingIndicator()
                    viewModel.getCharactersSortedByHeight()
                        .observe(viewLifecycleOwner) { pagingData ->
                            adapter.submitData(lifecycle, pagingData)
                            binding.recyclerView.postDelayed({
                                binding.recyclerView.layoutManager?.scrollToPosition(0)
                            }, 200)
                        }
                    hideLoadingIndicator()
                }

                R.id.sortByMass -> {
                    showLoadingIndicator()
                    viewModel.getCharactersSortedByMass()
                        .observe(viewLifecycleOwner) { pagingData ->
                            adapter.submitData(lifecycle, pagingData)
                            binding.recyclerView.postDelayed({
                                binding.recyclerView.layoutManager?.scrollToPosition(0)
                            }, 200)
                        }
                    hideLoadingIndicator()
                }

                R.id.sortByCreated -> {
                    showLoadingIndicator()
                    viewModel.getCharactersSortedByDateCreated()
                        .observe(viewLifecycleOwner) { pagingData ->
                            adapter.submitData(lifecycle, pagingData)
                            binding.recyclerView.postDelayed({
                                binding.recyclerView.layoutManager?.scrollToPosition(0)
                            }, 200)
                        }
                    hideLoadingIndicator()
                }

                R.id.sortByEdited -> {
                    showLoadingIndicator()
                    viewModel.getCharactersSortedByDateEdited()
                        .observe(viewLifecycleOwner) { pagingData ->
                            adapter.submitData(lifecycle, pagingData)
                            binding.recyclerView.postDelayed({
                                binding.recyclerView.layoutManager?.scrollToPosition(0)
                            }, 200)
                        }
                    hideLoadingIndicator()
                }
            }

            lastCheckedRadioButtonId = checkedId
        }
    }

    private fun hideSortView() {

        sortBottomSheetDialog.dismiss()
    }

    private lateinit var sortBottomSheetDialog: BottomSheetDialog
    private lateinit var filterBottomSheetDialog: BottomSheetDialog
    private lateinit var sortViewBinding: SortViewBinding

    private lateinit var filterViewBinding: FilterViewBinding
    private fun shouldDisableFilterSortButton(shouldDisable: Boolean) {

        binding.filterButton.isEnabled = !shouldDisable
        binding.sortButton.isEnabled = !shouldDisable
    }

    override fun onItemClick(data: Result) {
        val action =
            CharacterFragmentDirections.actionCharacterFragmentToFilmsFragment(
                result = data
            )
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        callback.remove()
    }

    private fun showLoadingIndicator() {
        isLoading = true
        binding.progressBar.visibility = View.VISIBLE
        binding.recyclerView.visibility = View.INVISIBLE
    }

    private fun hideLoadingIndicator() {
        isLoading = false
        binding.progressBar.visibility = View.GONE
        binding.recyclerView.visibility = View.VISIBLE
    }

    private fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            requireActivity().finish()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(requireContext(), "Please click BACK again to exit", Toast.LENGTH_SHORT)
            .show()

        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            onBackPressed()

        }
    }
}