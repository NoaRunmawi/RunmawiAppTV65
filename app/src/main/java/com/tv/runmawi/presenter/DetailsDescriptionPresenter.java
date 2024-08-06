package com.tv.runmawi.presenter;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.tv.runmawi.model.Movies;

public class DetailsDescriptionPresenter  extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Movies movie = (Movies) item;

        if (movie != null) {
            viewHolder.getTitle().setText(movie.getTitle());
          //  viewHolder.getSubtitle().setText(movie.getCategory());
            if(movie.getGenre().equals(null) || movie.getCategory().equals("null") ){
                viewHolder.getSubtitle().setText("Genre: "+movie.getGenre());
            }else{
                viewHolder.getSubtitle().setText("Genre: "+movie.getGenre()+" "+" Duration: "+movie.getLength());            }


            viewHolder.getBody().setText(movie.getDescription());
        }
    }
}
