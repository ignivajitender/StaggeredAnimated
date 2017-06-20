package com.igniva.staggeredanimated.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.igniva.staggeredanimated.Utils.Constants;
import com.igniva.staggeredanimated.R;
import com.igniva.staggeredanimated.Utils.CustomVolleyRequest;
import com.igniva.staggeredanimated.model.ItemObjects;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.display.CircleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.HashMap;


public class MyPagerAdapter extends PagerAdapter {

    Context context;
    ArrayList<ItemObjects> listItems;
    int adapterType;
    String imageLink;
    HashMap<String,Bitmap> mUrlMap = new HashMap<>();

    public MyPagerAdapter(Context context, ArrayList<ItemObjects> listItems, int adapterType) {
        this.context = context;
        this.listItems = listItems;
        this.adapterType=adapterType;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cover, null);
        try {

            final SubsamplingScaleImageView imageCover = (SubsamplingScaleImageView) view.findViewById(R.id.image);

            ImageView imageView = (ImageView)view.findViewById(R.id.imageview);
            imageCover.setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP);


           // if (position > 0) {

                imageLink = getImageLink(listItems.get(position).getImage_url());
                Log.i("image Url ", imageLink);

            Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading);

            imageCover.setImage(ImageSource.bitmap(largeIcon));


            Glide.with(context)
                        .load(imageLink)
                        .asBitmap()
                        .thumbnail(.0135f)
                        .into(new SimpleTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {

                                imageCover.setImage(ImageSource.bitmap(bitmap));

//                                Glide.with(context)
//                                        .load(imageLink)
//                                        .asBitmap()
//                                        .into(new SimpleTarget<Bitmap>() {
//                                            @Override
//                                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//
//
//                                                imageCover.setImage(ImageSource.bitmap(bitmap));
//
//                                            }
//
//                                            @Override
//                                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                                super.onLoadFailed(e, errorDrawable);
//                                                Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading);
//
//                                                imageCover.setImage(ImageSource.bitmap(largeIcon));
//
//
//                                                //    Toast.makeText(context, "Image loading failed", Toast.LENGTH_SHORT).show();
//
//                                            }
//
//                                            @Override
//                                            public void onLoadStarted(Drawable placeholder) {
//                                                super.onLoadStarted(placeholder);
//
//                                            }
//
//
//                                        });

                            }

                            @Override
                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                                super.onLoadFailed(e, errorDrawable);
                                Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading);

                                imageCover.setImage(ImageSource.bitmap(largeIcon));


                                //    Toast.makeText(context, "Image loading failed", Toast.LENGTH_SHORT).show();

                            }

                            @Override
                            public void onLoadStarted(Drawable placeholder) {
                                super.onLoadStarted(placeholder);

                            }


                        });


            //loadImage(imageLink,imageCover);
            imageCover.setMaxScale(10);
            imageCover.setZoomEnabled(true);

            imageCover.setDoubleTapZoomScale(6);
            imageCover.setDoubleTapZoomStyle(2);

//            }else {
//
//
//                int image = drawableImagesList[position];
//                Glide.with(context)
//                        .load(image)
//                        .asBitmap()
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap bitmap, GlideAnimation anim) {
//
//                                imageCover.setImage(ImageSource.bitmap(bitmap));
//
//                            }
//
//                            @Override
//                            public void onLoadFailed(Exception e, Drawable errorDrawable) {
//                                super.onLoadFailed(e, errorDrawable);
//                                Bitmap largeIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.loading);
//
//                                imageCover.setImage(ImageSource.bitmap(largeIcon));
//
//
//                                //    Toast.makeText(context, "Image loading failed", Toast.LENGTH_SHORT).show();
//
//                            }
//
//                            @Override
//                            public void onLoadStarted(Drawable placeholder) {
//                                super.onLoadStarted(placeholder);
//
//                            }
//
//
//                        });
//
//
//
//
//                imageCover.setMaxScale(10);
//                imageCover.setZoomEnabled(true);
//
//                imageCover.setDoubleTapZoomScale(6);
//                imageCover.setDoubleTapZoomStyle(2);
//
//            }

            container.addView(view);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return listItems.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return (view == object);
    }
    public String getImageLink(String imageUrl) {

            String imageLink= Constants.DRIVE_URL+imageUrl;

            return imageLink;


    }

}