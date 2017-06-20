package com.igniva.staggeredanimated.ui.activity;

import android.Manifest;
import android.app.WallpaperManager;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.igniva.staggeredanimated.Utils.Constants;
import com.igniva.staggeredanimated.db.RecorderDatabase;
import com.igniva.staggeredanimated.db.RecorderDatabaseUtility;
import com.igniva.staggeredanimated.model.GallaryItemPojo;
import com.igniva.staggeredanimated.model.ItemObjects;
import com.igniva.staggeredanimated.ui.adapter.MyPagerAdapter;
import com.igniva.staggeredanimated.R;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class ItemDetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "position";
    public static final String GALARY_TYPE = "galary_type";
    public static final String ALBUMID = "Album_id";

    private ViewPager viewpagerTop, viewPagerBackground;
    public static final int ADAPTER_TYPE_TOP = 1;
    public static final int ADAPTER_TYPE_BOTTOM = 2;
    RecyclerView mRvList;

    private ArrayList<ItemObjects> listItems ;


    private int[] drawablelist = {R.drawable.image1,R.drawable.image2,R.drawable.image3};
    private File mFile;
    private String TAG = "ItemDEtailActivity";
    boolean isSuccess;

    int mSelectedPosition = 0;
    int mPosition = 0;
    int mAlubmId;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    Bitmap mBitmap;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_item);

        if(getIntent()!= null){
            mSelectedPosition = getIntent().getIntExtra(EXTRA_POSITION,0);
            mPosition = getIntent().getIntExtra(GALARY_TYPE,0);
            mAlubmId = getIntent().getIntExtra(ALBUMID,0);

        }

        setUpLayout();
        setUpActionBar();
        
        setupViewPager();


    }
    private void setUpActionBar() {
        mToolbar.setTitle("Wallpaper");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
    private void createFile(String imageUrl) {
        String dirPath =Environment.getExternalStorageDirectory().getPath() + File.separator + "Images";

        File projDir = new File(dirPath);

        if (!projDir.exists())
            projDir.mkdirs();

        String imageName = null;

        String filename = null;


        filename = imageUrl.substring(imageUrl.lastIndexOf("=") + 1, imageUrl.length());

        imageName = "image_" + filename + ".jpg";

        mFile = new File(projDir, imageName);
    }

    /**
     * Initialize all required variables
     */
    private void setUpLayout() {
        viewPagerBackground = (ViewPager) findViewById(R.id.viewPagerbackground);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);



    }

    /**
     * Setup viewpager and it's events
     */
    private void setupViewPager() {
        // Set Top ViewPager Adapter



        // Set Background ViewPager Adapter
        listItems = getAlbumItemListData(String.valueOf(mAlubmId));
        MyPagerAdapter adapterBackground = new MyPagerAdapter(this, listItems, ADAPTER_TYPE_BOTTOM);
        viewPagerBackground.setAdapter(adapterBackground);



//        viewPagerBackground.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            private int index = mSelectedPosition;
//
//            @Override
//            public void onPageSelected(int position) {
//                index = position;
//                mSelectedPosition = position;
//
//            }
//
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                mSelectedPosition = position;
//                int width = viewPagerBackground.getWidth();
//
//                //viewPagerBackground.scrollTo((int) (width * position + width * positionOffset), 0);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//
//            }
//        });

        viewPagerBackground.setCurrentItem(mSelectedPosition);

    }

    /**
     * Handle all click event of activity
     */

    private void setbackdroundImage(int data) {



    }


    public boolean saveImage(Bitmap bitmap){
        isSuccess = true;
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


        try {
            //   f.createNewFile();
            FileOutputStream fo = new FileOutputStream(mFile);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            isSuccess = false;
        }

        return isSuccess;

    }

    public void shareImage(Bitmap bitmap){
        String imageName = "temp_" + System.currentTimeMillis() + ".jpg";
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("image/jpeg");


        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File wallpaperDirectory = new File("/" + Environment.getExternalStorageDirectory() + "/Temp/");
// have the object build the directory structure, if needed.
        wallpaperDirectory.mkdirs();
// create a File object for the output file
        File f = new File(wallpaperDirectory, imageName);


        try {
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }


        share.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///sdcard/temporary_file.jpg"));
        startActivity(Intent.createChooser(share, "Share Image"));

    }
    public String getImageLink(String imageUrl) {




        String imageLink= Constants.DRIVE_URL+imageUrl;

        return imageLink;


    }

    public void  setWallPaperFromDrawable(int drawable){
//        mFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
//                File.separator + "image_" + System.currentTimeMillis() + ".jpg");
        mBitmap = BitmapFactory.decodeResource(getResources(),
                drawable);

        createFile(getImageLink(listItems.get(viewpagerTop.getCurrentItem()).getImage_url()));
        isSuccess = saveImage(mBitmap);
        WallpaperManager myWallpaperManager
                = WallpaperManager.getInstance(getApplicationContext());




        Toast.makeText(ItemDetailActivity.this, "Wallpaper sets successfully", Toast.LENGTH_LONG).show();

        try {
            myWallpaperManager.setBitmap(mBitmap);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),"Unable to set Wallpaper",Toast.LENGTH_SHORT).show();


        }
    }

    public void setWallPaperFromUrl(final String imageLink){

        isSuccess = true;
        Log.i("url ",imageLink);
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Looper.prepare();
                try {

                    mBitmap = Glide.
                            with(ItemDetailActivity.this).
                            load(imageLink).
                            asBitmap().
                            into(-1, -1).
                            get();

                } catch (final Exception e) {
                    Log.e(TAG, e.getMessage());
                    isSuccess = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void dummy) {
                if (null != mBitmap) {
                    // The full bitmap should be available here

                    createFile(imageLink);
                    isSuccess = saveImage(mBitmap);
                    WallpaperManager myWallpaperManager
                            = WallpaperManager.getInstance(getApplicationContext());


                    try {
                        myWallpaperManager.setBitmap(mBitmap);
                        Toast.makeText(getApplicationContext(),"Wallpaper set Succesfully",Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        isSuccess = false;
                        Toast.makeText(getApplicationContext(),"Unable to set Wallpaper",Toast.LENGTH_SHORT).show();


                    }

                }else {
                    Toast.makeText(getApplicationContext(),"Unable to set Wallpaper",Toast.LENGTH_SHORT).show();

                    isSuccess = false;
                }
            }
        }.execute();


    }

    public void saveImageFromDrawable(int drawable) {
        isSuccess = true;

        try {
            mBitmap = BitmapFactory.decodeResource(getResources(),
                    drawable);

            isSuccess = saveImage(mBitmap);

            Toast.makeText(getApplicationContext(),"Image saved Successfully",Toast.LENGTH_SHORT).show();
        }catch (Exception e){
            e.printStackTrace();
            isSuccess = false;
            Toast.makeText(getApplicationContext(),"Unable to save image",Toast.LENGTH_SHORT).show();

        }

    }

    public void saveImageFromUrl(final String imageLink){

        isSuccess = true;


        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                // Looper.prepare();
                try {

                    mBitmap = Glide.
                            with(ItemDetailActivity.this).
                            load(imageLink).
                            asBitmap().
                            into(-1, -1).
                            get();

                } catch (final Exception e) {
                    Log.e(TAG, e.getMessage());
                    isSuccess = false;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void dummy) {
                if (null != mBitmap) {
                    // The full bitmap should be available here

                    isSuccess = saveImage(mBitmap);
                    Toast.makeText(getApplicationContext(),"Image saved Successfully",Toast.LENGTH_SHORT).show();

                }else {
                    Toast.makeText(getApplicationContext(),"Unable to save image",Toast.LENGTH_SHORT).show();

                    isSuccess = false;
                }
            }
        }.execute();

    }

    public void shareImageFromDrawable(int drawable){
        isSuccess = true;
        mBitmap = BitmapFactory.decodeResource(getResources(),
                drawablelist[viewpagerTop.getCurrentItem()]);


        shareImage(mBitmap);

    }

    public void shareImageFromUrl(final String imageLink){

        isSuccess = true;

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                try {
                    mBitmap = Glide.
                            with(ItemDetailActivity.this).
                            load(imageLink).
                            asBitmap().
                            into(-1, -1).
                            get();
                } catch (final Exception e) {


                    isSuccess = false;

//                            Log.e(TAG, e.getMessage());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void dummy) {
                if (null != mBitmap) {
                    // The full bitmap should be available here
                    shareImage(mBitmap);


                }else {
                    Toast.makeText(getApplicationContext(),"Unable to save image",Toast.LENGTH_SHORT).show();

                    isSuccess = false;
                }
                ;
            }
        }.execute();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    switch (requestCode){
                        case 0:
                            createFile(getImageLink(listItems.get(viewpagerTop.getCurrentItem()).getImage_url()));
                            boolean isWallpaperSet;
                            int position = viewpagerTop.getCurrentItem();
                            if (position > 2) {
                                setWallPaperFromUrl(getImageLink(listItems.get(viewpagerTop.getCurrentItem()).getImage_url()));
                            } else {
                                setWallPaperFromDrawable(drawablelist[position]);
                            }

                            break;

                    }



                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
    public ArrayList<ItemObjects> getAlbumItemListData(String album_id) {
        RecorderDatabase recoder = new RecorderDatabase(ItemDetailActivity.this);

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
