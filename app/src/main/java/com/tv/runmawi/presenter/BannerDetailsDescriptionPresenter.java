package com.tv.runmawi.presenter;

import androidx.leanback.widget.AbstractDetailsDescriptionPresenter;

import com.tv.runmawi.model.Banneritem;

public class BannerDetailsDescriptionPresenter extends AbstractDetailsDescriptionPresenter {

    @Override
    protected void onBindDescription(ViewHolder viewHolder, Object item) {
        Banneritem movie = (Banneritem) item;

        if (movie != null) {
            viewHolder.getTitle().setText(movie.getTitle());
          //  viewHolder.getSubtitle().setText(movie.getCategory());
            viewHolder.getSubtitle().setText("Genre: "+movie.getGenre());

            viewHolder.getBody().setText(movie.getSummary());
        }
    }
}
