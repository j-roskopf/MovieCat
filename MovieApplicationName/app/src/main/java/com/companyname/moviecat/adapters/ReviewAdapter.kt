package com.companyname.moviecat.adapters

/**
 * Created by Joe on 3/25/2017.
 */

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v7.widget.RecyclerView
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import butterknife.ButterKnife
import com.moviecat.joe.R
import com.companyname.moviecat.kotterknife.bindView
import com.companyname.moviecat.models.ReviewResult
import com.companyname.moviecat.models.ReviewResults

class ReviewAdapter(private val context: Context, private val reviewResults: ReviewResults) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    internal var layoutInflater: LayoutInflater

    inner class ReviewViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val reviewItemReviewText: TextView by bindView(R.id.reviewItemReviewText)
        private val reviewItemTMDBLink: ImageView by bindView(R.id.reviewItemTMDBLink)
        private val reviewItemName: TextView by bindView(R.id.reviewItemName)


        init {
            ButterKnife.bind(this, view)
        }


        fun setInformation(review: ReviewResult?, position: Int) {
            //set information

            setName(review)
            setReview(review)
            setLink(review)
        }

        private fun setLink(review: ReviewResult?) {
            reviewItemTMDBLink.setOnClickListener {
                try {
                    context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(review?.url)))

                } catch (e: Exception) {
                    Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show()
                }
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun setReview(review: ReviewResult?) {
            reviewItemReviewText.text = review?.content

            reviewItemReviewText.setOnTouchListener(View.OnTouchListener { v, event ->
                // Disallow the touch request for parent scroll on touch of child view
                v.parent.requestDisallowInterceptTouchEvent(true)
                false
            })

            reviewItemReviewText.movementMethod = ScrollingMovementMethod()
        }

        private fun setName(review: ReviewResult?) {
            reviewItemName.text = review?.author
        }

    }


    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.review_list_item, parent, false)

        return ReviewViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val review = reviewResults.results?.get(position)
        holder.setInformation(review, position)
    }


    override fun getItemCount(): Int {
        return reviewResults.results?.size ?: 0
    }

}