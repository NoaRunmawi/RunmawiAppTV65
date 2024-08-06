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

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.tv.runmawi.DetailActivity;
import com.tv.runmawi.R;
import com.tv.runmawi.model.Movies;

import java.util.List;


public class NewMaulCarouselAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private OnItemStateListener mListener;

    private List<Movies> mPlayList2;
    RVClickListener clickListener;
    private int selectedItem;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String category = "",user_id;


    public NewMaulCarouselAdapter(Context context, List<Movies> videoPlayList, String category, RVClickListener listAdapterClickInterface) {
        mContext = context;
        mPlayList2 = videoPlayList;
        this.clickListener = listAdapterClickInterface;
        // inflater = LayoutInflater.from(mContext);
        selectedItem = 0;
        this.category = category;
    }

    public void setOnItemStateListener(OnItemStateListener listener) {
        mListener = listener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     //   pref = mContext.getSharedPreferences("lookhu_user_detail",MODE_PRIVATE);
     //   user_id = pref.getString("user_id","");
        return new RecyclerViewHolder(View.inflate(mContext, R.layout.item_recyclerview_maul, null));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final RecyclerViewHolder viewHolder = (RecyclerViewHolder) holder;
        final Movies item = mPlayList2.get(position);
        //  viewHolder.mName.setText(item.getTitle());
        //    Log.i("title",item.getTitle());

        Glide.with(mContext).load(item.getThumbnail()).error(mContext.getResources().getDrawable(R.drawable.app_icon)).into(viewHolder.mImageView);


        //  Log.i("position_adapter", String.valueOf(position));
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra(DetailActivity.MOVIE, item);

             /*   Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(
                                mContext,
                                ((ImageCardView) mContext.itemViewHolder.view).getMainImageView(),
                                DetailActivity.SHARED_ELEMENT_NAME)
                        .toBundle(); */
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
                    if(clickListener!=null){
                        // listAdapterClickInterface.onClick((ChannelListViewHolder) viewHolder,list.get(i));
                        clickListener.onSelected((RecyclerViewHolder) viewHolder,position);
                    }
                    //  ((ChannelListViewHolder) viewHolder).text.setTextColor(Color.YELLOW);
                    int temp= selectedItem;
                    selectedItem=viewHolder.getAdapterPosition();
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

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout mRelativeLayout, paidLayout;
        //  TextView mName;
        ImageView mImageView;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            //    mName = (TextView) itemView.findViewById(R.id.tv_item_tip);
            mRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.fl_main_layout);
            paidLayout = (RelativeLayout) itemView.findViewById(R.id.paidLayout);
            mImageView = (ImageView) itemView.findViewById(R.id.iv_item);
            mRelativeLayout.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getAdapterPosition());
                //   mListener.onItemSelected(v, getAdapterPosition());
            }
        }


    }

    public interface OnItemStateListener {
        void onItemClick(View view, int position);
        //  void onItemSelected(View view,int position);

    }

    public interface RVClickListener {

        //   void onClick(ChannelListViewHolder viewHolder, AllModel model);

        void onSelected(RecyclerViewHolder viewHolder, int position);

        // void onFocus(ChannelListViewHolder viewHolder, AllModel model);
    }
}

