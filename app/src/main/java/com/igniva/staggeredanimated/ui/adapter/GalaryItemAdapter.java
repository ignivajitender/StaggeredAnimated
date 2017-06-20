package com.igniva.staggeredanimated.ui.adapter;

/**
 * Created by igniva-android-21 on 7/6/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.igniva.staggeredanimated.Utils.Constants;
import com.igniva.staggeredanimated.model.ItemObjects;
import com.igniva.staggeredanimated.R;
import com.igniva.staggeredanimated.ui.activity.GallaryDetailActivity;
import com.igniva.staggeredanimated.ui.activity.ItemDetailActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GalaryItemAdapter extends RecyclerView.Adapter<GalaryItemAdapter.SolventViewHolders> {

    private final int mAlbumId;
    private List<ItemObjects> itemList;
    private Context context;
    int width = 0;
    int mPosition = 0;

    private String[] mThumbUrl ;

    public GalaryItemAdapter(Context context, List<ItemObjects> itemList, int position,int albumId) {
        this.itemList = itemList;
        this.context = context;
        mPosition = position;
        mAlbumId = albumId;
        mThumbUrl = context.getResources().getStringArray(R.array.thumb_games);
    }

    @Override
    public SolventViewHolders onCreateViewHolder(final ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list, null);
        SolventViewHolders rcv = new SolventViewHolders(layoutView);


        return rcv;
    }

    @Override
    public void onBindViewHolder(final SolventViewHolders holder, final int position) {

        try {
//




            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int height = displayMetrics.heightPixels;
            int width = displayMetrics.widthPixels;

            ViewGroup.LayoutParams lp = holder.galaryItemPhoto.getLayoutParams();

            int aspectratio = width / 18;

            lp.height = 10 * aspectratio;
            holder.galaryItemPhoto.setLayoutParams(lp);
//            Glide
//                    .with(context)
//                    .load(getImageLink(itemList.get(position).getImage()))
//                    .asBitmap()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
//                    .thumbnail(.3f)
//                    .placeholder(R.drawable.loading)
//                    .into(holder.galaryItemPhoto);
//
//            Glide
//                    .with(context)
//                    .load(getImageLink(itemList.get(position).getImage()))
//                    .asBitmap()
//                    .diskCacheStrategy(DiskCacheStrategy.SOURCE);

                Glide
                        .with(context)
                        .load(getImageLink(itemList.get(position).getThumbImage()))
                        .asBitmap()
                        .placeholder(R.drawable.loading)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .thumbnail(.01f)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                holder.galaryItemPhoto.setImageBitmap(resource);
//                            Glide
//                                    .with(context)
//                                    .load(getImageLink(itemList.get(position).getImage()))
//                                    .asBitmap()
//                                    .diskCacheStrategy(DiskCacheStrategy.SOURCE);
                            }
                        });



            holder.galaryItemPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailActivity.EXTRA_POSITION, position);
                    intent.putExtra(ItemDetailActivity.GALARY_TYPE, mPosition);
                    intent.putExtra(GallaryDetailActivity.ALBUMID,mAlbumId);

                    ActivityOptionsCompat options = ActivityOptionsCompat.
                            makeSceneTransitionAnimation((Activity) context, (View) view, "profile");
                    ((Activity) context).startActivity(intent);
                }
            });


            // holder.galaryItemPhoto.setImageResource(itemList.get(position).getImage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }

    public class SolventViewHolders extends RecyclerView.ViewHolder {

        public ImageView galaryItemPhoto;

        public SolventViewHolders(View itemView) {
            super(itemView);
            galaryItemPhoto = (ImageView) itemView.findViewById(R.id.image);
        }


    }

    public String getImageLink(String imageUrl) {

        String imageLink = Constants.DRIVE_URL + imageUrl;

        return imageLink;


    }
}