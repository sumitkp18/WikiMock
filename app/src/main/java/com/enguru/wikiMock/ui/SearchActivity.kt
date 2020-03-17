package com.enguru.wikiMock.ui

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.enguru.wikiMock.R
import com.enguru.wikiMock.databinding.ActivitySearchBinding
import com.enguru.wikiMock.model.Page
import kotlinx.android.synthetic.main.activity_search.empty_screen_tag
import kotlinx.android.synthetic.main.activity_search.no_network_layout
import kotlinx.android.synthetic.main.activity_search.rv_repo
import kotlinx.android.synthetic.main.activity_search.shimmer_view_container
import kotlinx.android.synthetic.main.layout_no_network.view.retry_btn
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Activity class to search wikipedia and show the list results
 */
class SearchActivity : AppCompatActivity() {

    private val viewModel by viewModel<SearchViewModel>()
    private lateinit var viewBinding: ActivitySearchBinding
    private val listAdapter = ListAdapter()
    private lateinit var query: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initBinding()
        setActionBar()
        initObservers()
        initListeners()
        setUpRecyclerView()
        handleIntent(intent)
        empty_screen_tag.visibility = View.VISIBLE
        shimmer_view_container.visibility = View.GONE
    }

    /**
     * setting up binding
     */
    private fun initBinding() {
        viewBinding = DataBindingUtil.setContentView(this, R.layout.activity_search)
        viewBinding.networkError = viewModel.networkError
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleIntent(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.options_menu, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu.findItem(R.id.search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        shimmer_view_container.startShimmerAnimation()
    }

    override fun onPause() {
        shimmer_view_container.stopShimmerAnimation()
        super.onPause()
    }

    /**
     * Setting the action bar
     */
    private fun setActionBar() {
        //custom view on action bar for center aligned title
        supportActionBar?.run {
            displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
            setCustomView(R.layout.layout_action_bar_title)
            elevation = resources.getDimensionPixelSize(R.dimen.space_zero).toFloat()
        }
    }

    /**
     * Handle Intents
     */
    private fun handleIntent(intent: Intent) {
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringExtra(SearchManager.QUERY)?.run {
                query = this
                empty_screen_tag.visibility = View.GONE
                shimmer_view_container.visibility = View.VISIBLE
                viewModel.fetchQueryResults(this)
            }

        }
    }

    /**
     * initializes observers
     */
    private fun initObservers() {
        viewModel.fetchedResults.observe(this, Observer {
            setAdapter(it)
        })
    }

    /**
     * initializes various listeners
     */
    private fun initListeners() {
        no_network_layout.retry_btn.setOnClickListener {
            shimmer_view_container.startShimmerAnimation()
            shimmer_view_container.visibility = View.VISIBLE
            viewModel.fetchQueryResults(query, fetchFromServer = true)
        }
    }

    /**
     * sets data in the list adapter of recycler view
     */
    private fun setAdapter(repoList: List<Page>?) {
        repoList?.let {
            listAdapter.setData(it)
            listAdapter.notifyDataSetChanged()
            shimmer_view_container.visibility = View.GONE
            shimmer_view_container.stopShimmerAnimation()
        }
    }

    /**
     * Setup for the recycler View
     */
    private fun setUpRecyclerView() {
        rv_repo.layoutManager = LinearLayoutManager(this)
        rv_repo.adapter = listAdapter
    }
}
