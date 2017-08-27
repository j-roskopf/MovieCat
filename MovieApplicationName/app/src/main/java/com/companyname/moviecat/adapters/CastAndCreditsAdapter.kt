package com.companyname.moviecat.adapters

/**
 * Created by Joe on 3/25/2017.
 */

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.startActivity
import android.support.v7.widget.CardView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.companyname.moviecat.data.Const
import com.companyname.moviecat.kotterknife.bindView
import com.companyname.moviecat.models.retrofit.movie_find.Person
import com.moviecat.joe.R


class CastAndCreditsAdapter(private val context: Context, private val personList: List<Person>) : RecyclerView.Adapter<CastAndCreditsAdapter.PersonViewHolder>() {

    internal var layoutInflater: LayoutInflater

    inner class PersonViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val personItemImage: ImageView by bindView(R.id.personItemImage)
        private val personItemRealName: TextView by bindView(R.id.personItemRealName)
        private val personItemCharacterName: TextView by bindView(R.id.personItemCharacterName)
        private val personItemGender: ImageView by bindView(R.id.personItemGender)
        private val personItemCastLabel: TextView by bindView(R.id.personItemCastLabel)
        private val personItemCardView: CardView by bindView(R.id.personItemCardView)
        private val personProgressBar: ProgressBar by bindView(R.id.personItemImageProgress)


        init {
            ButterKnife.bind(this, view)
        }


        fun setInformation(person: Person, position: Int) {
            setGender(person.gender)
            person.profilePath?.let { setImage(it) }
            setRealName(person.name)
            setCharacterNameOrJob(person.character, person.job)
            setCastOrCrew(person.castOrCredit)
        }

        private fun setCastOrCrew(castOrCredit: String?) {
            personItemCastLabel.text = castOrCredit
        }

        private fun setGender(gender: Int?) {
            when(gender){
                1 -> setFemaleGenderIcons()
                2 -> setMaleGenerIcons()
            }
        }

        private fun setMaleGenerIcons(){
            personItemGender.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.male_icon))
            personItemImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.male_avatar))
        }

        private fun setFemaleGenderIcons(){
            personItemGender.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.female_icon))
            personItemImage.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.female_avatar))
        }

        private fun setCharacterNameOrJob(character: String?, job: String?) {
            character?.let {
                personItemCharacterName.text = character
            }

            job?.let {
                personItemCharacterName.text = job
            }
        }

        private fun  setRealName(name: String?) {
            personItemRealName.text = name
            personItemCardView.setOnClickListener {
                try {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + name))
                    startActivity(context, browserIntent, null)
                }
                catch (e: Exception) {
                    // handler
                }

            }
        }

        /**
         * Set the image
         */
        fun setImage(path: Any){
            Glide.with(context).load(Const.BASE_IMAGE_PATH + path).listener(object : RequestListener<String, GlideDrawable> {
                override fun onException(e: Exception, model: String, target: Target<GlideDrawable>, isFirstResource: Boolean): Boolean {
                    personProgressBar.visibility = View.GONE
                    personItemImage.setImageDrawable(context.getDrawable(android.R.drawable.stat_notify_error))
                    return false
                }

                override fun onResourceReady(resource: GlideDrawable, model: String, target: Target<GlideDrawable>, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
                    personProgressBar.visibility = View.GONE
                    return false
                }
            }).into(personItemImage)

        }

    }


    init {
        layoutInflater = LayoutInflater.from(context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PersonViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.cast_and_credits_list_item, parent, false)

        return PersonViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: PersonViewHolder, position: Int) {
        val person = personList[position]
        holder.setInformation(person, position)
    }


    override fun getItemCount(): Int {
        return personList.size
    }

    companion object {
        val PERSON = "person"
    }
}