package com.igniva.staggeredanimated.ui.adapter;

/**
 * Created by igniva-android-21 on 7/6/17.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.igniva.staggeredanimated.Utils.Constants;
import com.igniva.staggeredanimated.model.ItemObjects;
import com.igniva.staggeredanimated.R;
import com.igniva.staggeredanimated.ui.activity.AlbumDetailActivity;

import java.util.List;

public class GallaryAdapter extends RecyclerView.Adapter<GallaryAdapter.SolventViewHolders> {

    private List<ItemObjects> itemList;
    private Context context;

    public GallaryAdapter(Context context, List<ItemObjects> itemList) {
        this.itemList = itemList;
        this.context = context;
    }

    @Override
    public SolventViewHolders onCreateViewHolder(ViewGroup parent, int viewType) {

        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_album, null);
        SolventViewHolders rcv = new SolventViewHolders(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(final SolventViewHolders holder, final int position) {

        try {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;
            ViewGroup.LayoutParams lp = holder.galaryCoverPhoto.getLayoutParams();

            int aspectratio = width/18;
//
            if(position %4 == 0){

                lp.height = 11*aspectratio;

            } else {

                lp.height = 9*aspectratio;

            }

            Glide
                    .with(context)
                    .load(getImageLink(itemList.get(position).getImage_url()))
                    .placeholder(R.drawable.loading)
                    .thumbnail(.01f)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .into(holder.galaryCoverPhoto);

            Log.i("url",getImageLink(itemList.get(position).getImage_url()));
            holder.galaryName.setText(itemList.get(position).getName());

            Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/Montserrat-Regular.otf");
            holder.galaryName.setTypeface(type);

            holder.galaryCoverPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, AlbumDetailActivity.class);
                    intent.putExtra(AlbumDetailActivity.GALARY_IMAGE_URL, itemList.get(position).getImage_url());
                    intent.putExtra(AlbumDetailActivity.GALARY_NAME,itemList.get(position).getName());
                    intent.putExtra(AlbumDetailActivity.POSITION,position);
                    intent.putExtra(AlbumDetailActivity.ALBUMID,itemList.get(position).getId());

                    ((Activity)context).startActivity(intent);
                }
            });

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return this.itemList.size();
    }
    public class SolventViewHolders extends RecyclerView.ViewHolder {

        public ImageView galaryCoverPhoto;
        private TextView galaryName;

        public SolventViewHolders(View itemView) {
            super(itemView);
            galaryCoverPhoto = (ImageView) itemView.findViewById(R.id.image);
            galaryName = (TextView)itemView.findViewById(R.id.name);
        }


    }
    public String getImageLink(String imageUrl) {

        String imageLink= Constants.DRIVE_URL+imageUrl;

        return imageLink;


    }
}