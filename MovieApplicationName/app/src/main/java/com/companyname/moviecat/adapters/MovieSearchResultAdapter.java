package com.companyname.moviecat.adapters;

/**
 * Created by Joe on 3/25/2017.
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.companyname.movieapplicationname.R;
import com.companyname.moviecat.activities.DetailActivity;
import com.companyname.moviecat.data.Const;
import com.companyname.moviecat.events.MovieRatingEvent;
import com.companyname.moviecat.events.RatingListEvent;
import com.companyname.moviecat.firebase.MasterFavoriteList;
import com.companyname.moviecat.models.MovieSearchResults;
import com.companyname.moviecat.util.ListUtil;
import com.varunest.sparkbutton.SparkButton;
import com.varunest.sparkbutton.SparkEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

public class MovieSearchResultAdapter extends RecyclerView.Adapter<MovieSearchResultAdapter.MovieResultViewHolder> {

    public static final String MOVIE = "movie";

    private static final String IMDB_URL = "http://www.imdb.com/title/";

    int currentlySelectedItem = 0;

    private Context context;
    private List<MovieSearchResults> movieList;
    private ArrayList<MovieSearchResults> firebaseMovieFavorites;
    private ArrayList<MovieSearchResults> firebaseMovieRatings;
    private boolean comingFromFavorites = false;

    LayoutInflater layoutInflater;

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

        @BindView(R.id.movieItemFavorite)
        SparkButton movieItemFavorite;

        @BindView(R.id.movieItemAdd)
        SparkButton movieItemAdd;

        @BindView(R.id.movieItemRate)
        SparkButton movieItemRate;

        @BindView(R.id.movieItemSummary)
        TextView movieItemSummary;

        public MovieResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }

        private void setInformation(MovieSearchResults movie, int position) {

            checkIfAlreadyFavorited(movie.getId(), position);
            setTitle(movie.getTitle());
            setIMDBOnClick(movie.getId());
            setYear(movie.getReleaseDate());
            setType(movie.getOriginalLanguage().toUpperCase());
            if (movie.getPosterPath() != null) {
                setImage(movie.getPosterPath());
            }
            setSummary(movie.getOverview());
            setFavoriteLogic(position);
            setRatingForMovieFromList(movie);
            setRatingLogic(movie);
            setCardOnClick(movie);

            if (!comingFromFavorites)
                setupAddButton(movie);
        }

        /**
         * Checks if it's already favorited so in setFavoriteLogic we know to show the heart as colored in or not
         *
         * @param id
         * @param position
         */
        private void checkIfAlreadyFavorited(int id, int position) {

            if (firebaseMovieFavorites == null)
                return;

            boolean alreadyFavorited = false;
            for (MovieSearchResults movieSearchResults : firebaseMovieFavorites) {
                if (movieSearchResults.getId().intValue() == id) {
                    alreadyFavorited = true;
                    break;
                }
            }
            movieList.get(position).setFavorited(alreadyFavorited);
        }

        private void setRatingForMovieFromList(MovieSearchResults movieSearchResults) {
            for (MovieSearchResults movie : firebaseMovieRatings) {
                if (movie.getId().equals(movieSearchResults.getId())) {
                    //update the rating of the object to be displayed
                    movieSearchResults.setUserRating(movie.getUserRating());
                }
            }
        }

        private void setTitle(String title) {
            movieItemTitle.setText(title);
        }

        private void setSummary(String summary) {
            movieItemSummary.setText(summary);
        }

        private void setIMDBOnClick(final int id) {
            try {
                movieItemIMDBIcon.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Const.THE_MOVIE_DB_MOVIE_VIEW.concat(String.valueOf(id)))));
                    }
                });
            } catch (Exception e) {
                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
            }


        }

        private void setYear(String year) {
            //Expected format is yyyy-MM-dd
            if (year.length() >= 4) {
                movieItemYear.setText(year.substring(0, 4));
            } else {
                movieItemYear.setText(year);
            }
        }

        private void setType(String type) {
            movieItemMovieType.setText(type);
        }

        private void setImage(String imageUrl) {
            Glide.with(context).load(Const.BASE_IMAGE_PATH.concat(imageUrl)).into(movieItemPosterImage);
        }

        private void setFavoriteLogic(final int position) {
            movieItemFavorite.setEventListener(new SparkEventListener() {
                @Override
                public void onEvent(ImageView button, boolean buttonState) {
                    if (buttonState) {
                        movieList.get(position).setFavorited(true);
                        MasterFavoriteList.getInstance().add(movieList.get(position));
                    } else {
                        if (position < movieList.size()) {
                            movieList.get(position).setFavorited(false);
                            int positionRemoved = removeItemAndReturnPosition(movieList.get(position));
                            if (positionRemoved != -1 && comingFromFavorites) {
                                notifyItemRemoved(positionRemoved);
                            }

                        }
                    }
                }
            });


            if (movieList.get(position).isFavorited()) {
                movieItemFavorite.setChecked(true);
            } else {
                movieItemFavorite.setChecked(false);
            }
        }

        public int removeItemAndReturnPosition(Object o) {
            int positionRemoved = -1;
            MovieSearchResults movieSearchResults = (MovieSearchResults) o;

            for (int i = 0; i < MasterFavoriteList.getInstance().getFirebaseMovieFavoritesList().size(); i++) {
                MovieSearchResults msr = MasterFavoriteList.getInstance().getFirebaseMovieFavoritesList().get(i);
                if (msr.getId().equals(movieSearchResults.getId())) {
                    MasterFavoriteList.getInstance().getFirebaseMovieFavorites().remove(String.valueOf(msr.getId()));
                    positionRemoved = i;
                    break;
                }
            }

            MasterFavoriteList.getInstance().getFirebaseMovieFavorites().save();
            return positionRemoved;
        }

        private void setCardOnClick(final MovieSearchResults movie) {
            movieItemCardview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra(MovieSearchResultAdapter.MOVIE, movie);
                    context.startActivity(intent);
                }
            });
        }

        private void setupAddButton(final MovieSearchResults movie) {
            movieItemAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAddMovieDialog(movie);
                }
            });
        }

        /**
         * Handles logic for when the star is clicked in the adapter
         *
         * @param movie
         */
        private void setRatingLogic(final MovieSearchResults movie) {

            if (ListUtil.movieInList(firebaseMovieRatings, movie)) {
                movieItemRate.setChecked(true);
            } else {
                movieItemRate.setChecked(false);
            }

            movieItemRate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    final MaterialRatingBar ratingBar = (MaterialRatingBar) layoutInflater.inflate(R.layout.rating_layout, null);
                    ratingBar.setNumStars(5);
                    ratingBar.setRating((float) movie.getUserRating());
                    ratingBar.setStepSize(0.5f);

                    FrameLayout container = new FrameLayout(context);
                    FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

                    params.leftMargin = Const.dpToPx(16);
                    params.rightMargin = Const.dpToPx(16);

                    ratingBar.setLayoutParams(params);
                    container.addView(ratingBar);

                    AlertDialog alertDialog = new AlertDialog.Builder(context, R.style.AlertDialogTheme)
                            .setTitle("Add Rating")
                            .setMessage("Enter a rating out of 5 stars!")
                            .setView(container)

                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    movieItemRate.setChecked(true);
                                    movie.setUserRating(ratingBar.getRating());
                                    EventBus.getDefault().post(new MovieRatingEvent(movie));
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int whichButton) {
                                    movieItemRate.setChecked(false);
                                }
                            }).create();

                    alertDialog.show();
                }
            });

        }

        private void showAddMovieDialog(final MovieSearchResults movie) {
            EventBus.getDefault().post(new RatingListEvent(true, movie));
        }


    }


    public MovieSearchResultAdapter(boolean comingFromFavorites, ArrayList<MovieSearchResults> firebaseMovieRatings, ArrayList<MovieSearchResults> firebaseMovieFavorites,
                                    Context context, List<MovieSearchResults> moveList) {
        this.context = context;
        this.movieList = moveList;
        this.firebaseMovieRatings = firebaseMovieRatings;
        this.firebaseMovieFavorites = firebaseMovieFavorites;
        this.comingFromFavorites = comingFromFavorites;

        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MovieResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.movie_search_result_list_item, parent, false);

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