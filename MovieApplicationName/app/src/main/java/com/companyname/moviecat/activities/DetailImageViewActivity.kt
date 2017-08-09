package com.companyname.moviecat.activities

import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.companyname.movieapplicationname.R
import com.companyname.moviecat.data.Const
import com.companyname.moviecat.kotterknife.bindView
import com.google.firebase.analytics.FirebaseAnalytics
import com.liuguangqiang.swipeback.SwipeBackActivity
import com.liuguangqiang.swipeback.SwipeBackLayout

class DetailImageViewActivity : SwipeBackActivity() {

    companion object{
        var PATH: String = "path"
    }

    private val detailViewImageActivity: ImageView by bindView(R.id.detailViewImageActivity)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_image_view)
        setDragEdge(SwipeBackLayout.DragEdge.LEFT)

        FirebaseAnalytics.getInstance(this).setCurrentScreen(this, "DetailImageViewActivity", null /* class override */)

        if(intent.hasExtra(PATH)){
            val filePath: String = intent.extras.getString(PATH)

            Glide.with(this).load(Const.BASE_IMAGE_PATH + filePath).into(detailViewImageActivity)
        }else{
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show()
        }


    }
}
