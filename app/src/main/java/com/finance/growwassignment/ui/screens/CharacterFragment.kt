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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.finance.growwassignment.R
import com.finance.growwassignment.databinding.FilterViewBinding
import com.finance.growwassignment.databinding.FragmentCharacterBinding
import com.finance.growwassignment.databinding.SortViewBinding
import com.finance.growwassignment.models.CharacterResult
import com.finance.growwassignment.paging.CharacterPagingAdapter
import com.finance.growwassignment.paging.LoaderAdapter
import com.finance.growwassignment.utilities.FilterType
import com.finance.growwassignment.utilities.GridSpacingItemDecoration
import com.finance.growwassignment.utilities.OnItemClickListener
import com.finance.growwassignment.viewmodels.CharacterViewModel
import com.finance.growwassignment.viewmodels.MovieViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class CharacterFragment : Fragment(), OnItemClickListener<CharacterResult> {
    private var _binding: FragmentCharacterBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CharacterViewModel by viewModels()
    private val movieViewModel: MovieViewModel by viewModels()
    private lateinit var adapter: CharacterPagingAdapter
    private var lastCheckedRadioButtonId = -1
    private var isLoading = false
    private var doubleBackToExitPressedOnce = false
    private var selectedFilter: FilterType? = null
    private var sortOption: String? = null
    private var filterOption: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
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
        viewModel.getCharacters().observe(viewLifecycleOwner) { pagingData ->
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

        filterViewBinding.btReset.setOnClickListener {
            getCharacters()
            clearFilters()
            hideFilterView()
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
                    handleFilterSelection(FilterType.EYE_COLOR)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                }
            }

        filterViewBinding.seebarHeight.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                handleFilterSelection(FilterType.HEIGHT)
            }
        })

        filterViewBinding.seebarMass.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                handleFilterSelection(FilterType.MASS)
            }
        })
    }

    private fun clearFilters() {
        filterViewBinding.spHairColor.setSelection(0)
        filterViewBinding.seebarHeight.progress = 0
        filterViewBinding.seebarMass.progress = 0
    }

    private fun applyFilter() {
        when (selectedFilter) {
            FilterType.EYE_COLOR -> {
                val selectedEyeColor = filterViewBinding.spHairColor.selectedItem.toString()
                filterOption = "hairColor" + ", " + selectedEyeColor
                filterbasedOnHairColor(selectedEyeColor)

            }

            FilterType.HEIGHT -> {
                val selectedHeight = filterViewBinding.seebarHeight.progress.toString()
                filterOption = "height" + ", " + "65" + "," + selectedHeight
                filterByHeight(selectedHeight)

            }

            FilterType.MASS -> {
                val selectedMass = filterViewBinding.seebarMass.progress.toString()
                filterOption = "mass" + ", " + "15" + "," + selectedMass
                filterByMass(selectedMass)

            }

            else -> {
                // No filter selected
                // You can show a message or handle it based on your requirement
            }
        }
    }

    private fun filterByMass(selectedMass: String) {
        viewModel.getCharactersFilterByMass("15", selectedMass)
            .observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
                binding.recyclerView.postDelayed({
                    binding.recyclerView.layoutManager?.scrollToPosition(0)
                }, 200)
            }
    }

    private fun filterByHeight(selectedHeight: String) {
        viewModel.getCharactersFilterByHeight("65", selectedHeight)
            .observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
                binding.recyclerView.postDelayed({
                    binding.recyclerView.layoutManager?.scrollToPosition(0)
                }, 200)
            }
    }

    private fun filterbasedOnHairColor(selectedEyeColor: String) {
        viewModel.getCharactersFilterByHairColor(selectedEyeColor)
            .observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
                binding.recyclerView.postDelayed({
                    binding.recyclerView.layoutManager?.scrollToPosition(0)
                }, 200)
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

        sortViewBinding.btReset.setOnClickListener {
            getCharacters()
            hideSortView()
            sortViewBinding.sortRadioGroup.clearCheck();
        }
        sortViewBinding.btApply.setOnClickListener {
            hideSortView()

            val checkedId = sortViewBinding.sortRadioGroup.checkedRadioButtonId

            if (lastCheckedRadioButtonId == checkedId) {
                return@setOnClickListener
            }

            when (checkedId) {
                R.id.sortByName -> {
                    showLoadingIndicator()
                    sortOption = "name"
                    sortByName()
                    hideLoadingIndicator()
                }

                R.id.sortByHeight -> {
                    showLoadingIndicator()
                    sortOption = "height"
                    sortByHeight()
                    hideLoadingIndicator()
                }

                R.id.sortByMass -> {
                    showLoadingIndicator()
                    sortOption = "mass"
                    sortByMass()
                    hideLoadingIndicator()
                }

                R.id.sortByCreated -> {
                    showLoadingIndicator()
                    sortOption = "created"
                    sortByCreated()
                    hideLoadingIndicator()
                }

                R.id.sortByEdited -> {
                    showLoadingIndicator()
                    sortOption = "edited"
                    sortByEdited()
                    hideLoadingIndicator()
                }
            }

            lastCheckedRadioButtonId = checkedId
        }
    }

    private fun sortByMass() {
        viewModel.getCharactersSortedByMass()
            .observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
                binding.recyclerView.postDelayed({
                    binding.recyclerView.layoutManager?.scrollToPosition(0)
                }, 200)
            }
    }

    private fun sortByEdited() {
        viewModel.getCharactersSortedByDateEdited()
            .observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
                binding.recyclerView.postDelayed({
                    binding.recyclerView.layoutManager?.scrollToPosition(0)
                }, 200)
            }
    }

    private fun sortByCreated() {
        viewModel.getCharactersSortedByDateCreated()
            .observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
                binding.recyclerView.postDelayed({
                    binding.recyclerView.layoutManager?.scrollToPosition(0)
                }, 200)
            }
    }

    private fun sortByHeight() {
        viewModel.getCharactersSortedByHeight()
            .observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
                binding.recyclerView.postDelayed({
                    binding.recyclerView.layoutManager?.scrollToPosition(0)
                }, 200)
            }
    }

    private fun sortByName() {
        viewModel.getCharactersSortedByName()
            .observe(viewLifecycleOwner) { pagingData ->
                adapter.submitData(lifecycle, pagingData)
                binding.recyclerView.postDelayed({
                    binding.recyclerView.layoutManager?.scrollToPosition(0)
                }, 200)
            }
    }

    private fun getCharacters() {
        viewModel.getCharacters().observe(viewLifecycleOwner) { pagingData ->
            adapter.submitData(lifecycle, pagingData)
            binding.recyclerView.postDelayed({
                binding.recyclerView.layoutManager?.scrollToPosition(0)
            }, 200)
        }
    }

    private fun hideSortView() {

        sortBottomSheetDialog.dismiss()
    }

    private lateinit var sortBottomSheetDialog: BottomSheetDialog
    private lateinit var filterBottomSheetDialog: BottomSheetDialog
    private lateinit var sortViewBinding: SortViewBinding

    private lateinit var filterViewBinding: FilterViewBinding
    override fun onItemClick(data: CharacterResult) {
        val action =
            CharacterFragmentDirections.actionCharacterFragmentToFilmsFragment(
                result = data,
                sortOption,
                filterOption
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

        Handler(Looper.getMainLooper()).postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000)
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {

            onBackPressed()

        }
    }
}