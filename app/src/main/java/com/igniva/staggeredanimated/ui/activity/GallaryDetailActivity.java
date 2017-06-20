package com.igniva.staggeredanimated.ui.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Transition;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.igniva.staggeredanimated.Utils.Constants;
import com.igniva.staggeredanimated.db.RecorderDatabase;
import com.igniva.staggeredanimated.db.RecorderDatabaseUtility;
import com.igniva.staggeredanimated.model.GallaryItemPojo;
import com.igniva.staggeredanimated.ui.adapter.GalaryItemAdapter;
import com.igniva.staggeredanimated.model.ItemObjects;
import com.igniva.staggeredanimated.R;

import java.util.ArrayList;
import java.util.List;

public class GallaryDetailActivity extends AppCompatActivity {

    public static final String GALARY_IMAGE_URL = "Image_Url";
    public static final String GALARY_NAME = "Galary_name";
    public static final String POSITION = "position";
    public static final String ALBUMID = "Album_id";
    int mPostion;
    ImageView mIvImage;
    TextView mTvName;

    String mUrl,mGalaryName;
    int mAlbum_id;
    private RecyclerView recyclerView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);

        setUpLayout();
        setUpActionBar();


        try {
            if(getIntent() != null) {
                Intent intent = getIntent();
                mUrl = intent.getStringExtra(GALARY_IMAGE_URL);
                mGalaryName = intent.getStringExtra(GALARY_NAME);
                mPostion = intent.getIntExtra(POSITION, 0);
                mAlbum_id = intent.getIntExtra(ALBUMID,0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        setUpData();

        setUpList();




//
}

    private void setUpActionBar() {
        mToolbar.setTitle("Wallpaper");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    private void setUpList() {
        recyclerView.setHasFixedSize(true);

        StaggeredGridLayoutManager gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
        recyclerView.setLayoutManager(gaggeredGridLayoutManager);

        List<ItemObjects> albumsItemsList = getAlbumItemListData(String.valueOf(mAlbum_id));

        if(albumsItemsList != null){
        GalaryItemAdapter rcAdapter = new GalaryItemAdapter(this, albumsItemsList,mPostion,mAlbum_id);
        recyclerView.setAdapter(rcAdapter);
        }
    }

    private void setUpData() {
        Glide
                .with(this)
                .load(getImageLink(mUrl))
                .asBitmap()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into( mIvImage);

        mTvName.setText(mGalaryName);
    }

    private void setUpLayout() {

        recyclerView = (RecyclerView) findViewById(R.id.galary_list);
         mIvImage = (ImageView)findViewById(R.id.image);
         mTvName = (TextView)findViewById(R.id.name);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

    }

    private Transition enterTransition() {
        ChangeBounds bounds = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            bounds = new ChangeBounds();
            bounds.setDuration(1000);

        }

        return bounds;
    }


    private List<ItemObjects> getListItemData(){

        GallaryItemPojo gallaryItemPojo = GallaryItemPojo.getInstance();
        List<ItemObjects> listViewItems = new ArrayList<ItemObjects>();

        switch (mPostion){
            case  0:

                listViewItems = gallaryItemPojo.getGamesList();

                break;
            case 1:
                listViewItems = gallaryItemPojo.getFanstasyList();

                break;
            case 2:
                listViewItems = gallaryItemPojo.getAbstractList();

                break;
            case 3:
                listViewItems = gallaryItemPojo.getFoodList();

                break;
        }



        return listViewItems;
    }
    public String getImageLink(String imageUrl) {

        String imageLink= Constants.DRIVE_URL+imageUrl;

        return imageLink;


    }

    @Override
    public void onBackPressed() {

        finish();
        overridePendingTransition(0,0);
    }

    public ArrayList<ItemObjects> getAlbumItemListData(String album_id) {
        RecorderDatabase recoder = new RecorderDatabase(GallaryDetailActivity.this);

        recoder.openDatabase();
        ArrayList<ItemObjects> list = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + RecorderDatabaseUtility.MAPPING_TABLE + " INNER JOIN "+RecorderDatabaseUtility.IMAGE_TABLE
                +" ON image_id= id WHERE album_id ="+album_id;

        Cursor cursor = recoder.getData(selectQuery);
        if (cursor.moveToFirst()) {
            do {
                ItemObjects itemObject = new ItemObjects();
                itemObject.setAlbum_id(cursor.getInt(0));
                itemObject.setId(cursor.getInt(1));
                itemObject.setName(cursor.getString(3));
                itemObject.setImage_url(cursor.getString(4));
                itemObject.setThumbImage(cursor.getString(5));
                itemObject.setRating(cursor.getString(6));

                Log.i("Map",cursor.getString(3));
                list.add(itemObject);
                // Adding contact to list
            } while (cursor.moveToNext());
        }

        return list;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

