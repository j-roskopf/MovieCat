package com.companyname.moviecat.adapters.detail

import android.content.Context
import android.content.Intent
import android.support.v4.view.PagerAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.Glide
import com.companyname.movieapplicationname.R
import com.companyname.moviecat.activities.DetailImageViewActivity
import com.companyname.moviecat.data.Const
import com.companyname.moviecat.models.retrofit.movie_find.Image
import java.util.*

/**
 * Created by Joe on 6/18/2017.
 */

internal class DetailViewImageViewPagerAdapter(var mContext: Context, var images: ArrayList<Image>) : PagerAdapter() {
    var mLayoutInflater: LayoutInflater

    init {
        mLayoutInflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    override fun getCount(): Int {
        return images.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view === `object` as LinearLayout
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = mLayoutInflater.inflate(R.layout.detail_view_image_view_pager_item, container, false)

        val detailViewImage = itemView.findViewById(R.id.detailViewImage) as ImageView
        Glide.with(mContext).load(Const.BASE_IMAGE_PATH + images[position].filePath!!).into(detailViewImage)
        
        detailViewImage.setOnClickListener{
            val intent: Intent = Intent(mContext, DetailImageViewActivity::class.java)
            
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(DetailImageViewActivity.PATH, images[position].filePath!!)
            
            mContext.startActivity(intent)
                    
        }

        container.addView(itemView)

        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as LinearLayout)
    }
}