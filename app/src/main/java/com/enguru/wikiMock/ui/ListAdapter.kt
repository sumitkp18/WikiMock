package com.enguru.wikiMock.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.enguru.wikiMock.R
import com.enguru.wikiMock.databinding.ItemPageBinding
import com.enguru.wikiMock.model.Page
import com.enguru.wikiMock.repo.URLConstants
import com.enguru.wikiMock.ui.ListAdapter.PageItemViewHolder
import com.squareup.picasso.Picasso

/**
 * Adapter class for the recycler view to show the list of search results
 */
class ListAdapter : RecyclerView.Adapter<PageItemViewHolder>() {

    private val dataSet: ArrayList<Page> = arrayListOf()

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): PageItemViewHolder {

        val view = LayoutInflater.from(viewGroup.context)
        val binding: ItemPageBinding = DataBindingUtil.inflate(view, R.layout.item_page, viewGroup, false)
        return PageItemViewHolder(binding)
    }

    override fun onBindViewHolder(cryptoViewHolder: PageItemViewHolder, position: Int) {
        cryptoViewHolder.bind(dataSet[position])
    }

    /**
     * method to get size of the recycler view's dataset
     */
    override fun getItemCount(): Int {
        return dataSet.size
    }

    /**
     * This method clears the dataset
     */
    fun clearData() {
        this.dataSet.clear()
    }

    /**
     * This method updates the latest data set to the adapter
     * @param data: List of [Page]] objects
     */
    fun setData(data: List<Page>) {
        clearData()
        this.dataSet.addAll(data)
    }

    /**
     * ViewHolder class for a single item of page detail in recycler view
     */
    inner class PageItemViewHolder(private val binding: ItemPageBinding) : RecyclerView.ViewHolder(binding.root) {

        /**
         * Method to bind data
         * @param page page item containing the details
         */
        fun bind(page: Page) {
            with(binding) {
                pageItem = page
                page.thumbnail?.source?.let {
                    Picasso.get().load(it).into(thumbnail)
                }

                itemCard.setOnClickListener {
                    page.title?.let { title ->
                        val intent = DetailActivity.newInstance(context = itemView.context, url = buildURL(title))
                        startActivity(itemView.context, intent, null)
                    }
                }
            }
        }

        private fun buildURL(title: String): String {
            val label = title.replace(" ", "_")
            return "${URLConstants.BASE_URL}${URLConstants.PAGE_ENDPOINT}$label"
        }
    }

}