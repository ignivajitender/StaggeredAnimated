package com.igniva.staggeredanimated.Utils;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.igniva.staggeredanimated.R;
import com.igniva.staggeredanimated.ui.activity.GalleryAcivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;



public class UtilsUI {


    private static final String BUTTON_CLICK_EVENT = "click event" ;
    public static boolean galery_status, favourite_status, channels_status;

    static GalleryAcivity m;

    static Boolean isShare = true;

    static String data;

    static EditText share_message_editText;

    private final static String APP_PNAME = "com.igniva.youtubeplayer";


    public static int darker(int color, double factor) {

        int a = Color.alpha(color);
        int r = Color.red(color);
        int g = Color.green(color);
        int b = Color.blue(color);

        return Color.argb(a, Math.max((int) (r * factor), 0), Math.max((int) (g * factor), 0), Math.max((int) (b * factor), 0));

    }

    public static Drawer setNavigationDrawer(Activity activity, final Context context, Toolbar toolbar) {

        int header;

        m = new GalleryAcivity();


        final SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        header = R.color.colorPrimary;

        AccountHeader headerResult = new AccountHeaderBuilder()
                .withActivity(activity)
                .withHeaderBackground(header)
                .build();





        DrawerBuilder drawerBuilder = new DrawerBuilder();

        drawerBuilder.withActivity(activity);

        drawerBuilder.withToolbar(toolbar);

        drawerBuilder.withAccountHeader(headerResult);

        drawerBuilder.addDrawerItems(

                new PrimaryDrawerItem().withName("Videos").withIdentifier(1).withIcon(R.drawable.ic_videocam_black_24dp),

                new PrimaryDrawerItem().withName("Gallery").withIdentifier(2).withIcon(R.drawable.ic_insert_photo_black_24dp),

                new PrimaryDrawerItem().withName("Favourite").withIdentifier(3).withIcon(R.drawable.ic_favorite_black_24dp),

              //  new PrimaryDrawerItem().withName("Gallery").withIcon(R.drawable.ic_menu_gallery).withIdentifier(5),

                new DividerDrawerItem(),



                new SecondaryDrawerItem().withName("Settings").withSelectable(false).withIdentifier(4).withIcon(R.drawable.ic_settings_black_24dp),

                new SecondaryDrawerItem().withName("Rate Us").withSelectable(false).withIdentifier(5).withIcon(R.drawable.ic_rate_review_black_24dp),

                new SecondaryDrawerItem().withName("More Free Apps").withSelectable(false).withIdentifier(6).withIcon(R.drawable.ic_more_black_24dp),

                new SecondaryDrawerItem().withName("Share").withSelectable(false).withIdentifier(7).withIcon(R.drawable.ic_share_black_24dp));


        drawerBuilder.withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position, IDrawerItem iDrawerItem) {

                switch (iDrawerItem.getIdentifier()) {
                    case 1:


                        favourite_status = false;

                        galery_status = false;



                        break;
                    case 2:


                        favourite_status = false;

                        break;

                    case 3:



                        favourite_status = true;

                        galery_status = false;

                        break;

                    case 4:


                        favourite_status = false;

                        galery_status = false;


                        break;

                    case 5:



                        Uri uri = Uri.parse("market://details?id=" + "com.sqwip");

                        Intent rateApp = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        rateApp.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK|
                                Intent.FLAG_ACTIVITY_NEW_TASK
                                );

                        try {

                            context.startActivity(rateApp);

                        } catch (ActivityNotFoundException e) {

                            context.startActivity(new Intent(Intent.ACTION_VIEW,

                                    Uri.parse("http://play.google.com/store/apps/details?id=" + "com.sqwip")));

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                        break;


                    case 6:



                        uri = Uri.parse("market://search?q=pub:saga");

                        Intent moreFreeApps = new Intent(Intent.ACTION_VIEW, uri);
                        // To count with Play market backstack, After pressing back button,
                        // to taken back to our application, we need to add following flags to intent.
                        moreFreeApps.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                                Intent.FLAG_ACTIVITY_MULTIPLE_TASK|
                                Intent.FLAG_ACTIVITY_NEW_TASK);

                        try {

                            context.startActivity(moreFreeApps);

                        } catch (ActivityNotFoundException e) {

                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));

                        }catch (Exception e){

                            e.printStackTrace();

                        }

                        break;

                    case 7:


                        String appname = getAppName(context);
                        LayoutInflater layoutInflater = LayoutInflater.from(context);



                        break;

                    default:

                        break;

                }

                return false;
            }
        });

        return drawerBuilder.build();
    }

    public static String getAppName(Context context){
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo ai;
        try {
            ai = pm.getApplicationInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            ai = null;
        }
        final String applicationName = (String) (ai != null ? pm.getApplicationLabel(ai) : "(unknown)");
        return applicationName;
    }

}