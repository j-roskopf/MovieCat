package com.companyname.moviecat.adapters;

/**
 * Created by Joe on 3/25/2017.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.companyname.movieapplicationname.R;
import com.companyname.moviecat.activities.DetailActivity;
import com.companyname.moviecat.data.Const;
import com.companyname.moviecat.models.MovieSearchResults;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.MovieResultViewHolder> {

    int currentlySelectedItem = 0;

    private Context context;
    private List<MovieSearchResults> movieList;
    private boolean comingFromFavorites = false;

    public class MovieResultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.movieItemPosterImage)
        ImageView movieItemPosterImage;

        @BindView(R.id.movieItemIMDBIcon)
        ImageView movieItemIMDBIcon;

        @BindView(R.id.movieItemMovieType)
        TextView movieItemMovieType;

        @BindView(R.id.movieItemTitle)
        TextView movieItemTitle;

        @BindView(R.id.movieItemYear)
        TextView movieItemYear;

        @BindView(R.id.movieItemCardview)
        CardView movieItemCardview;


        public MovieResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        private void setInformation(MovieSearchResults movie, int position){
            setTitle(movie.getTitle());
            setIMDBOnClick("REPLACE");
            setYear("REPLACE");
            setType("REPLACE");
            setImage("REPLACE");
            setCardOnClick(movie);
        }



        private void setTitle(String title){
            movieItemTitle.setText(title);
        }

        private void setIMDBOnClick(final String imdbId){
            try{
                movieItemIMDBIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Const.IMDB_URL.concat(imdbId))));
                    }
                });
            }catch (Exception e){
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }



        }
        private void setYear(String year){
            movieItemYear.setText(year);
        }

        private void setType(String type){
            movieItemMovieType.setText(type);
        }

        private void setImage(String imageUrl){
            Glide.with(context).load(imageUrl).into(movieItemPosterImage);
        }



        private void setCardOnClick(final MovieSearchResults movie){
            movieItemCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(MovieSearchResultAdapter.MOVIE, movie);
                    context.startActivity(intent);
                }
            });
        }

    }


    public ListViewAdapter(boolean comingFromFavorites, Context context, List<MovieSearchResults> moveList) {
        this.context = context;
        this.movieList = moveList;
        this.comingFromFavorites = comingFromFavorites;
    }

    @Override
    public MovieResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;
        if(comingFromFavorites){
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_view_list, parent, false);
        }else{
            itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_search_result_list_item, parent, false);
        }


        return new MovieResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieResultViewHolder holder, int position) {
        MovieSearchResults movie = movieList.get(position);
        holder.setInformation(movie, position);
    }


    @Override
    public int getItemCount() {
        return movieList.size();
    }
}