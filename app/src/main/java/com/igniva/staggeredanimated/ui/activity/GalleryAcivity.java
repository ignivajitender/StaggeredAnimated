package com.igniva.staggeredanimated.ui.activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.igniva.staggeredanimated.db.RecorderDatabase;
import com.igniva.staggeredanimated.db.RecorderDatabaseOperation;
import com.igniva.staggeredanimated.db.RecorderDatabaseUtility;
import com.igniva.staggeredanimated.model.GallaryItemPojo;
import com.igniva.staggeredanimated.ui.adapter.GallaryAdapter;
import com.igniva.staggeredanimated.model.ItemObjects;
import com.igniva.staggeredanimated.R;
import com.igniva.staggeredanimated.Utils.UtilsUI;
import com.jpardogo.listbuddies.lib.views.ListBuddiesLayout;
import com.mikepenz.materialdrawer.Drawer;

import java.util.ArrayList;
import java.util.List;

public class GalleryAcivity extends AppCompatActivity  implements ListBuddiesLayout.OnBuddyItemClickListener {

    int[] mScrollConfig;
    private String[] mImagesLeft,mImagesRight;
    StaggeredGridLayoutManager staggeredGridLayoutManager;
    private DrawerLayout mDrawerLayout;
    Toolbar mToolbar;
    RecyclerView mRecyleVIew;
    RecorderDatabase recoder = new RecorderDatabase(GalleryAcivity.this);

    GallaryItemPojo mGallaryItemPojo;
    List<ItemObjects> gallaryList;
    GallaryAdapter gallaryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
// set an enter transition
        recoder.openDatabase();

        mGallaryItemPojo = GallaryItemPojo.getInstance();

        setContentView(R.layout.activity_main);

        setUpLayout();


        storeData();


        Drawer drawer = UtilsUI.setNavigationDrawer(GalleryAcivity.this, GalleryAcivity.this, mToolbar);
        mDrawerLayout = drawer.getDrawerLayout();


        initList();

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = firebaseDatabase.getReference("Albums");
        DatabaseReference imageDatabaseReference = firebaseDatabase.getReference("Images");
        DatabaseReference mappingDatabaseReference = firebaseDatabase.getReference("Mapping");

        Query firebaseAlbumQuery = databaseReference.orderByChild("id");
        Query firebaseImageQuery = imageDatabaseReference.orderByChild("id");
        Query firebaseMappingQuery = mappingDatabaseReference.orderByChild("image_id");

        ChildEventListener videoChildEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String prevChildKey) {
                try {
                    ItemObjects post = dataSnapshot.getValue(ItemObjects.class);

                    Log.i("key",dataSnapshot.getRef().getParent().toString());
                   String parent_key = dataSnapshot.getRef().getParent().toString();

                    String root_key = parent_key.substring(parent_key.lastIndexOf("/")+1,parent_key.length());
                    switch (root_key){
                        case "Albums":
                            ContentValues contentValues = RecorderDatabaseOperation.createContentValues("album_id", "album_name", "image_url", "thumb_url", "rating", post.getId(), post.getName(), post.getImage_url(), post.getThumbImage(), post.getRating());
                            addData(contentValues, RecorderDatabaseUtility.ALBUM_TABLE);
                            initList();
                            break;
                        case "Images":
                            ContentValues contentValues1 = RecorderDatabaseOperation.createContentValues("id", "name", "image_url", "thumb_url", "rating", post.getId(), post.getName(), post.getImage_url(), post.getThumbImage(), post.getRating());
                            addData(contentValues1, RecorderDatabaseUtility.IMAGE_TABLE);

                            break;
                        case "Mapping":
                            ContentValues contentValues2 = RecorderDatabaseOperation.createContentValues("album_id", "image_id", post.getAlbum_id(), post.getImage_id());
                            addData(contentValues2, RecorderDatabaseUtility.MAPPING_TABLE);

                            break;
                    }

                }catch (Exception e){
                    e.printStackTrace();
                }


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                ItemObjects post = dataSnapshot.getValue(ItemObjects.class);
//                ContentValues contentValues = RecorderDatabaseOperation.createContentValues("album_id", "album_name", "image_url", "thumb_url", "rating", post.getAlbum_id(), post.getName(), post.getImage_url(), post.getThumbImage(), post.getRating());
//                addData(contentValues, RecorderDatabaseUtility.ALBUM_TABLE);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }

        };

        ValueEventListener albumValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("doneeee 1", dataSnapshot.toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        firebaseAlbumQuery.addChildEventListener(videoChildEventListener);
        firebaseMappingQuery.addChildEventListener(videoChildEventListener);
        firebaseImageQuery.addChildEventListener(videoChildEventListener);
        firebaseAlbumQuery.addListenerForSingleValueEvent(albumValueEventListener);
        firebaseImageQuery.addListenerForSingleValueEvent(albumValueEventListener);
        firebaseMappingQuery.addListenerForSingleValueEvent(albumValueEventListener);



    }

    private ArrayList<ItemObjects> getAlbumListData() {

        ArrayList<ItemObjects> albumList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + RecorderDatabaseUtility.ALBUM_TABLE;

        Cursor cursor = recoder.getData(selectQuery);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ItemObjects itemObject = new ItemObjects();
                itemObject.setId(cursor.getInt(0));
                itemObject.setName(cursor.getString(1));
                itemObject.setImage_url(cursor.getString(2));
                itemObject.setThumbImage(cursor.getString(3));
                itemObject.setRating(cursor.getString(4));

                albumList.add(itemObject);
                // Adding contact to list
            } while (cursor.moveToNext());
        }
        return albumList;
    }

    private void storeData() {
        ArrayList<ItemObjects> gamesList = getAlbumItemListData("1");


        if(gamesList.size() == 0) {

            String[] albumImage = getResources().getStringArray(R.array.galary_cover_images);
            String[] albumName = getResources().getStringArray(R.array.galary_cover_name);
            String[] thumbImages = getResources().getStringArray(R.array.thumb_games);

            int j = 0;
            for (int i = 0; i < albumImage.length; i++) {
                if (j == thumbImages.length) {
                    j = 0;
                }
                ContentValues contentValue = new ContentValues();
                contentValue.put("album_name", albumName[i]);
                contentValue.put("image_url", albumImage[i]);
                contentValue.put("thumb_url", thumbImages[j]);

                contentValue.put("rating", i);
                recoder.insertData(contentValue, RecorderDatabaseUtility.ALBUM_TABLE);
                j++;
            }

            j = 0;

            String[] galaryImages = getResources().getStringArray(R.array.games);
            for (int i = 0; i < galaryImages.length; i++) {
                if (j == thumbImages.length) {
                    j = 0;
                }
                ContentValues contentValue = new ContentValues();
                contentValue.put("name", "games");
                contentValue.put("image_url", galaryImages[i]);
                contentValue.put("thumb_url", thumbImages[j]);
                contentValue.put("rating", i);
                recoder.insertData(contentValue, RecorderDatabaseUtility.IMAGE_TABLE);
                j++;
            }

            j = 0;

            String[] fantasyImages = getResources().getStringArray(R.array.fantasy);
            for (int i = 0; i < fantasyImages.length; i++) {
                if (j == thumbImages.length) {
                    j = 0;
                }
                ContentValues contentValue = new ContentValues();
                contentValue.put("name", "fantasy");
                contentValue.put("image_url", fantasyImages[i]);
                contentValue.put("thumb_url", thumbImages[j]);
                contentValue.put("rating", i);
                recoder.insertData(contentValue, RecorderDatabaseUtility.IMAGE_TABLE);
                j++;
            }


            j = 0;

            String[] abstractImages = getResources().getStringArray(R.array.abstracts);
            for (int i = 0; i < abstractImages.length; i++) {
                if (j == thumbImages.length) {
                    j = 0;
                }
                ContentValues contentValue = new ContentValues();
                contentValue.put("name", "abstract");
                contentValue.put("image_url", abstractImages[i]);
                contentValue.put("thumb_url", thumbImages[j]);
                contentValue.put("rating", i);
                recoder.insertData(contentValue, RecorderDatabaseUtility.IMAGE_TABLE);
                j++;
            }

            j = 0;

            String[] foodImages = getResources().getStringArray(R.array.food);
            for (int i = 0; i < foodImages.length; i++) {
                if (j == thumbImages.length) {
                    j = 0;
                }
                ContentValues contentValue = new ContentValues();
                contentValue.put("name", "food");
                contentValue.put("image_url", foodImages[i]);
                contentValue.put("thumb_url", thumbImages[j]);
                contentValue.put("rating", i);
                recoder.insertData(contentValue, RecorderDatabaseUtility.IMAGE_TABLE);
                j++;
            }

            j = 1;
            for (int i = 0; i < galaryImages.length; i++) {
                ContentValues contentValue = new ContentValues();
                contentValue.put("album_id", 1);
                contentValue.put("image_id", j);
                recoder.insertData(contentValue, RecorderDatabaseUtility.MAPPING_TABLE);
                j++;
            }


            for (int i = 0; i < fantasyImages.length; i++) {
                ContentValues contentValue = new ContentValues();
                contentValue.put("album_id", 2);
                contentValue.put("image_id", j);
                recoder.insertData(contentValue, RecorderDatabaseUtility.MAPPING_TABLE);
                j++;
            }
            for (int i = 0; i < abstractImages.length; i++) {
                ContentValues contentValue = new ContentValues();
                contentValue.put("album_id", 3);
                contentValue.put("image_id", j);
                recoder.insertData(contentValue, RecorderDatabaseUtility.MAPPING_TABLE);
                j++;
            }
            for (int i = 0; i < foodImages.length; i++) {
                ContentValues contentValue = new ContentValues();
                contentValue.put("album_id", 4);
                contentValue.put("image_id", j);
                recoder.insertData(contentValue, RecorderDatabaseUtility.MAPPING_TABLE);
                j++;
            }

        }


    }



    private void initList() {

            staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, 1);
            mRecyleVIew.setLayoutManager(staggeredGridLayoutManager);
            gallaryList = getAlbumListData();
            gallaryAdapter = new GallaryAdapter(GalleryAcivity.this, gallaryList);
            mRecyleVIew.setAdapter(gallaryAdapter);

    }

    private void setUpLayout() {

         mRecyleVIew = (RecyclerView)findViewById(R.id.recyclerview);
        mRecyleVIew.setHasFixedSize(true);

        mToolbar = (Toolbar)findViewById(R.id.toolbar);
        mToolbar.setTitle("Wallpaper");
         mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }


    private List<ItemObjects> getListItemData(){
        String[] galaryCoverImages = getResources().getStringArray(R.array.galary_cover_images);

        String[] galaryName = getResources().getStringArray(R.array.galary_cover_name);


        List<ItemObjects> listViewItems = new ArrayList<ItemObjects>();

        for(int i = 0;i<galaryCoverImages.length;i++) {

            ItemObjects itemObject = new ItemObjects();
            itemObject.setName(galaryName[i]);
            itemObject.setImage_url(galaryCoverImages[i]);
            listViewItems.add(itemObject);

        }
        return listViewItems;
    }

    @Override
    public void onBuddyItemClicked(AdapterView<?> parent, View view, int buddy, int position, long id) {

        Intent intent = new Intent(this, GallaryDetailActivity.class);
                ActivityOptionsCompat options = ActivityOptionsCompat.
                        makeSceneTransitionAnimation(this, view, "profile");
                startActivity(intent, options.toBundle());
    }


    public void getAlbumData() {

       // String selectQuery = "SELECT  * FROM " + RecorderDatabaseUtility.IMAGE_TABLE +" WHERE album_id="+0;

      //  Cursor cursor = recoder.getData(selectQuery);

      //  ArrayList<ItemObjects> arrayList = new ArrayList<>();
        // looping through all rows and adding to list




    }


    public ArrayList<ItemObjects> getAlbumItemListData(String album_id) {
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

    public void addData(ContentValues contentValues,String tableName){

        recoder.insertData(contentValues, tableName);

    }


}
