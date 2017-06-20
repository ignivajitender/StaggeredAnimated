package com.igniva.staggeredanimated.db;

/**
 * Created by Rohit on 28/11/16.
 */

public class RecorderDatabaseUtility {
    public static String CREATE_TABLE_IMAGES = "CREATE TABLE " + "tbl_images_mstr" +"(" + "id" +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "name" +" TEXT,"+ "image_url" +" TEXT," + "thumb_url" +" TEXT," + "rating" +" TEXT"+")";

    public static String CREATE_TABLE_ALBUMS = "CREATE TABLE " + "tbl_albums_mstr" +"(" + "album_id" +" INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "album_name" +" TEXT,"+ "image_url" +" TEXT," + "thumb_url" +" TEXT," + "rating" +" TEXT"+")";

    public static String CREATE_TABLE_MAPPING = "CREATE TABLE " + "tbl_mapping_mstr" +"(" + "album_id" +" INTEGER,"+
            "image_id" +" INTEGER  PRIMARY KEY"+")";
    public static final String ALBUM_TABLE = "tbl_albums_mstr";
    public static final String IMAGE_TABLE = "tbl_images_mstr";
    public static final String MAPPING_TABLE = "tbl_mapping_mstr";


    public static final String TABLE_HISTORY = "Table_HISTORY";

    public static final String DROP_TABLE_IF_EXISTS = "";
    public static String ALL_TABLES_QUERY="";
    public static String WHOLE_TABLE="SELECT * FROM Table_HISTORY";

    public static String TABLE_LOGIN="";

    public static String KEY_ID= "video_id";
    public static String KEY_NAME = "Name";
    public static String KEY_DESC = "Description";
    public static String KEY_TIME = "Time";
    public static String KEY_STATUS = "Status";
    public static String KEY_VIDEO_PATH = "Video_Path";
    public static String KEY_DURATION= "Duration";

}
