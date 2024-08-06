package com.tv.runmawi.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tv.runmawi.DetailActivity;
import com.tv.runmawi.R;
import com.tv.runmawi.VideoExampleWithExoPlayerActivity;
import com.tv.runmawi.model.Movies;

import java.util.List;


public class EpisodeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;


    private List<Movies> mPlayList2;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String category = "",user_id;


    public EpisodeAdapter(Context context, List<Movies> videoPlayList, String category) {
        mContext = context;
        mPlayList2 = videoPlayList;
        // inflater = LayoutInflater.from(mContext);
        this.category = category;
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     //   pref = mContext.getSharedPreferences("lookhu_user_detail",MODE_PRIVATE);
     //   user_id = pref.getString("user_id","");
        return new RecyclerViewHolder(View.inflate(mContext, R.layout.episode_item, null));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
        final Movies item = mPlayList2.get(position);
        viewHolder.mName.setText(item.getTitle());
        //    Log.i("title",item.getTitle());

        Glide.with(mContext).load(item.getThumbnail()).error(mContext.getResources().getDrawable(R.drawable.app_icon)).into(viewHolder.mImageView);


        //  Log.i("position_adapter", String.valueOf(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            /*    Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(DetailActivity.MOVIE, item);
                mContext.startActivity(intent); */
                Intent intent = new Intent(mContext, VideoExampleWithExoPlayerActivity.class);
                intent.putExtra(DetailActivity.MOVIE, item);
                intent.putExtra("video_type", "video");
                mContext.startActivity(intent);

            }
        });
        holder.itemView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // run scale animation and make it bigger
                    holder.getAdapterPosition();
                    //   Log.i("position_adapter2", String.valueOf(holder.getAdapterPosition()));
                    Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_in_tv);
                    viewHolder.itemView.startAnimation(anim);
                    anim.setFillAfter(true);
                    //  selectedItem=viewHolder.getAdapterPosition();
                    //   viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.recycle_focus));

                    //  ((ChannelListViewHolder) viewHolder).text.setTextColor(Color.YELLOW);

                    //    viewHolder.itemView.setBackground(mContext.getResources().getDrawable(R.drawable.recycle_focus));

                    //     notifyItemChanged(temp);
                } else {
                    // run scale animation and make it smaller
                    Animation anim = AnimationUtils.loadAnimation(mContext, R.anim.scale_out_tv);
                    viewHolder.itemView.startAnimation(anim);
                    anim.setFillAfter(true);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return mPlayList2.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {

        RelativeLayout mRelativeLayout, paidLayout;
        TextView mName;
        ImageView mImageView;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.tv_item_tip);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.fl_main_layout);
            paidLayout = (RelativeLayout) itemView.findViewById(R.id.paidLayout);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_item);


        }



    }



}

