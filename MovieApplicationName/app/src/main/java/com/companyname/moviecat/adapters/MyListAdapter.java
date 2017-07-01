package com.companyname.moviecat.adapters;

/**
 * Created by Joe on 3/25/2017.
 */

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.companyname.movieapplicationname.R;
import com.companyname.moviecat.events.AddListEvent;
import com.companyname.moviecat.events.ListViewEvent;
import com.companyname.moviecat.models.UserList;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.MovieResultViewHolder> {

    private static final String IMDB_URL = "http://www.imdb.com/title/";
    public static final String LIST_NAME = "list_name";
    public static final String LIST = "list";

    private Context context;
    private ArrayList<UserList> firebaseMovieLists;
    boolean addToList;

    public class MovieResultViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.listItemListName)
        TextView listItemListName;

        @BindView(R.id.listItemCardview)
        CardView listItemCardview;

        public MovieResultViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }

        private void setInformation(final UserList userList, int position){
            listItemListName.setText(userList.getListName());
            if(addToList){
                listItemCardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new AddListEvent(userList));
                    }
                });
            }else{
                listItemCardview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EventBus.getDefault().post(new ListViewEvent(userList.getListName(), userList));
                    }
                });
            }

        }

    }


    public MyListAdapter(boolean addToList, ArrayList<UserList> firebaseMovieLists, Context context) {
        this.addToList = addToList;
        this.context = context;
        this.firebaseMovieLists = firebaseMovieLists;
    }

    @Override
    public MovieResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_list_item, parent, false);

        return new MovieResultViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MovieResultViewHolder holder, int position) {
        UserList userList = firebaseMovieLists.get(position);
        holder.setInformation(userList, position);
    }


    @Override
    public int getItemCount() {
        return firebaseMovieLists.size();
    }

}