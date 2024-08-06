package com.tv.runmawi.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.tv.runmawi.model.pMovieItem;
import com.tv.runmawi.model.pShowsItem;

import java.util.LinkedList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    String db_asin = null;

    private static final int DATABASE_VERSION = 2;

    String folder, tot_design, price, mob_no;

    private static final String DATABASE_NAME = "runmawi_db";

    private static final String TABLE_NAME1 = "p_movies";
    private static final String TABLE_NAME2 = "p_tv_shows";
    private static final String TABLE_NAME3 = "p_dubbed_movies";

    private static final String ID = "id";
    private static final String VIDEO_ID = "v_id";
    private static final String TV_SHOW_ID = "tv_show_id";
    private static final String PURCHASE_DATE = "p_date";
    private static final String VALIDITY = "validity";




    private static final String[] TABLE1_COLUMNS = {ID, VIDEO_ID, PURCHASE_DATE, VALIDITY};
    private static final String[] TABLE2_COLUMNS = {ID, VIDEO_ID, TV_SHOW_ID, PURCHASE_DATE, VALIDITY};


    private static final String CREATE_TABLE1 = "CREATE TABLE " + TABLE_NAME1 + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + VIDEO_ID + " TEXT UNIQUE," + PURCHASE_DATE + " TEXT," + VALIDITY + " TEXT)";
    private static final String CREATE_TABLE2 = "CREATE TABLE " + TABLE_NAME2 + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + TV_SHOW_ID + " TEXT UNIQUE," + VIDEO_ID + " TEXT," + PURCHASE_DATE + " TEXT," + VALIDITY + " TEXT)";
    private static final String CREATE_TABLE3 = "CREATE TABLE " + TABLE_NAME3 + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT ," + VIDEO_ID + " TEXT UNIQUE," + PURCHASE_DATE + " TEXT," + VALIDITY + " TEXT)";

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(CREATE_TABLE1);
        sqLiteDatabase.execSQL(CREATE_TABLE2);
        sqLiteDatabase.execSQL(CREATE_TABLE3);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME1);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME2);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME3);
        this.onCreate(sqLiteDatabase);

    }

    public int check_movies() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_NAME1;
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public int check_tvshows() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_NAME2;
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }
    public int check_dubbedmovies() {
        SQLiteDatabase db = this.getReadableDatabase();
        String countQuery = "SELECT * FROM " + TABLE_NAME3;
        Cursor cursor = db.rawQuery(countQuery, null);
        return cursor.getCount();
    }

    public int check_pmoviesId(String videoid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME1, TABLE1_COLUMNS, VIDEO_ID + " = ?", new String[]{videoid}, null, null, null, null);
        return cursor.getCount();
    }
    public int check_showId(String showId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME2, TABLE2_COLUMNS, TV_SHOW_ID + " = ?", new String[]{showId}, null, null, null, null);
        return cursor.getCount();
    }
    public int check_dmoviesId(String videoid) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME3, TABLE1_COLUMNS, VIDEO_ID + " = ?", new String[]{videoid}, null, null, null, null);
        return cursor.getCount();
    }

    public void Add_movies(String v_id, String p_date, String valdity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(VIDEO_ID, v_id);
        cv.put(PURCHASE_DATE, p_date);
        cv.put(VALIDITY, valdity);
        db.insertWithOnConflict(TABLE_NAME1, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    public void Add_shows(String shows_id,String v_id, String p_date, String valdity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(TV_SHOW_ID, shows_id);
        cv.put(VIDEO_ID, v_id);
        cv.put(PURCHASE_DATE, p_date);
        cv.put(VALIDITY, valdity);
        db.insertWithOnConflict(TABLE_NAME2, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    public void Add_dubbedmovies(String v_id, String p_date, String valdity) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(VIDEO_ID, v_id);
        cv.put(PURCHASE_DATE, p_date);
        cv.put(VALIDITY, valdity);
        db.insertWithOnConflict(TABLE_NAME3, null, cv, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }
    public List getMovieList() {
        List orders = new LinkedList();
        String query = "SELECT DISTINCT " + VIDEO_ID + ", " + PURCHASE_DATE + ", " + VALIDITY +" FROM " + TABLE_NAME1 + " ORDER BY " + ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery(query, null);
        pMovieItem fv = null;
        if (cr.moveToFirst()) {
            int i = 1;
            do {
                //  int i =1;
                fv = new pMovieItem();

               // fv.setId(cr.getInt(0));
                fv.setV_id(cr.getString(0));
                fv.setP_date(cr.getString(1));
                fv.setValidity(cr.getString(2));

                orders.add(fv);

                i++;
            } while (cr.moveToNext());
        }

        return orders;
    }
    public List getdMovieList() {
        List orders = new LinkedList();
        String query = "SELECT DISTINCT " + VIDEO_ID + ", " + PURCHASE_DATE + ", " + VALIDITY +" FROM " + TABLE_NAME3 + " ORDER BY " + ID;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery(query, null);
        pMovieItem fv = null;
        if (cr.moveToFirst()) {
            int i = 1;
            do {
                //  int i =1;
                fv = new pMovieItem();

                // fv.setId(cr.getInt(0));
                fv.setV_id(cr.getString(0));
                fv.setP_date(cr.getString(1));
                fv.setValidity(cr.getString(2));

                orders.add(fv);

                i++;
            } while (cr.moveToNext());
        }

        return orders;
    }
    public List getShowsList() {
        List orders = new LinkedList();
        String query = "SELECT DISTINCT " + TV_SHOW_ID + ", " + PURCHASE_DATE + ", " + VALIDITY +" FROM " + TABLE_NAME2 + " ORDER BY " + ID;


        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cr = db.rawQuery(query, null);
        pShowsItem fv = null;
        if (cr.moveToFirst()) {
            int i = 1;
            do {
                //  int i =1;
                fv = new pShowsItem();


                fv.setShows_id(cr.getString(0));
                fv.setP_date(cr.getString(1));
                fv.setValidity(cr.getString(2));

                orders.add(fv);

                i++;
            } while (cr.moveToNext());
        }

        return orders;
    }
    public void deletepMovies(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME1);
        db.close();
    }
    public void deletepTvShows(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME2);
        db.close();
    }
    public void deletedMovies(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME3);
        db.close();
    }
 /*   public boolean deleteArticle(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME1, ARTICLE_ID + " = ?", new String[]{name}) > 0;
    }
    public void deleteAll()
{
    //SQLiteDatabase db = this.getWritableDatabase();
   // db.delete(TABLE_NAME,null,null);
    //db.execSQL("delete * from"+ TABLE_NAME);
    db.execSQL("TRUNCATE table" + TABLE_NAME);
    db.close();
}
 */
}