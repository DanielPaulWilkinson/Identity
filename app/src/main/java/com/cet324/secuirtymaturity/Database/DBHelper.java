package com.cet324.secuirtymaturity.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.cet324.secuirtymaturity.ObjectClasses.Password;
import com.cet324.secuirtymaturity.ObjectClasses.User;
import com.cet324.secuirtymaturity.PasswordSecurity.Encryption;

import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 * Created by DanielWilkinson on 1/1/2017.
 */
public class  DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "passwordapplication.db";
    private static final int DATABASE_VERSION = 6;
    //Constants for table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PASSWORDS = "passwords";
    //public static final String TABLE_USER_PASS  = "userPasswords";
    //columns for user table
    public static final String USERS_ID = "_id";
    public static final String USERS_EMAIL = "usersEmail";
    public static final String USERS_APP_PASS_ORIG = "usersAppPass";
    public static final String USERS_APP_PASS = "usersAppPassOrig";

    public static final String USERS_APP_ENCRYPTION_SALT = "usersAppSalt";
    public static final String USERS_APP_ENCRYPTION_KEY = "usersAppKey";
    //columns for users stored passwords
    public static final String PASSWORD_ID = "_id";
    public static final String PASSWORD_NAME = "passwordName";
    public static final String PASSWORD_DATE = "passwordDate";
    public static final String PASSWORD_EMAIL = "passwordEmail";
    public static final String PASSWORD_FEQUENCY_OF_CHANGE = "passwordChange";
    public static final String PASSWORD_FOR = "passwordFor";

    //column to join password and user tables
    // public static final String USERS_PASS_ID = "_userpassid";
    //all columns for users table
    public static final String[] ALL_COLUMNS_USERS =
            {USERS_ID, USERS_EMAIL, USERS_APP_PASS, USERS_APP_ENCRYPTION_KEY,USERS_APP_ENCRYPTION_SALT,USERS_APP_PASS_ORIG};
    //all columns for password
    public static final String[] ALL_COLUMNS_PASSWORD =
            {PASSWORD_ID, PASSWORD_NAME, PASSWORD_FOR, PASSWORD_DATE, PASSWORD_EMAIL, PASSWORD_FEQUENCY_OF_CHANGE};


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    public void onCreate(SQLiteDatabase db) {
        //create required tables
         String CREATE_TABLE_USERS = "CREATE TABLE "
                + TABLE_USERS + "(" +
                USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                USERS_EMAIL + " TEXT," +
                USERS_APP_PASS + " TEXT," +
                USERS_APP_ENCRYPTION_SALT + " TEXT," +
                USERS_APP_ENCRYPTION_KEY+ " TEXT," +
        USERS_APP_PASS_ORIG+ " TEXT)";
        //table passwords
          String CREATE_TABLE_PASSWORD = "CREATE TABLE "
                + TABLE_PASSWORDS + "(" +
                PASSWORD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                PASSWORD_NAME + " TEXT," +
                PASSWORD_DATE + " TEXT," +
                PASSWORD_EMAIL + " TEXT," +
                PASSWORD_FEQUENCY_OF_CHANGE + " TEXT," +
                PASSWORD_FOR + " TEXT)";

        db.execSQL(CREATE_TABLE_PASSWORD);
        db.execSQL(CREATE_TABLE_USERS);
        //  db.execSQL(CREATE_TABLE_USER_PASS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //drop tables when a new version is avalable
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PASSWORDS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        //  db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PASS);
        this.onCreate(db);
    }

    //constructor


    //========================================USER DATABASE ACTIONS=================================
    public void addUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERS_EMAIL, user.getEmail());
        values.put(USERS_APP_PASS, user.getPass());
        values.put(USERS_APP_ENCRYPTION_KEY, user.getKey());
        values.put(USERS_APP_ENCRYPTION_SALT, user.getSalt());
        values.put(USERS_APP_PASS_ORIG, user.getOriginal());
        db.insert(TABLE_USERS,
                null,
                values);
        db.close();

    }

    public void addPassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(PASSWORD_NAME, password.getName());
        values.put(PASSWORD_FOR, password.getCompany());
        values.put(PASSWORD_DATE, password.getDate());
        values.put(PASSWORD_EMAIL, password.getEmail());
        values.put(PASSWORD_FEQUENCY_OF_CHANGE, password.getFrequencyOfChange());
        db.insert(TABLE_PASSWORDS,
                null,
                values);
        db.close();
    }

    public void CurrentUserSalt(){

    }
    public void CurrentUserKey(){

    }

    public long getPasswordCount(String userEmail) {
        SQLiteDatabase db = getWritableDatabase();
        long taskCount = DatabaseUtils.longForQuery(db, "SELECT COUNT (*) FROM " + TABLE_PASSWORDS + " WHERE " + PASSWORD_EMAIL + " = '" + userEmail + "'",
                new String[]{});
        return taskCount;
    }

    //============================================SELECT============================================
    public Cursor queryDatabase(String tablelocation, String[] columns, String selection,
                                String[] arguments, String having,
                                String orderBy, String limit) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(tablelocation,//table location
                        columns,//coloumn selection
                        selection,//which columns the user would like
                        arguments,//where
                        null,//group
                        having,//group query
                        orderBy,//order
                        limit//how many?
                );
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            return cursor;
        }
        return cursor;
    }

    public User getUser(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_USERS,
                        ALL_COLUMNS_USERS,
                        DBHelper.USERS_EMAIL + " = '" + String.valueOf(email) + "'",
                        null,
                        null,
                        null,
                        null,
                        null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            String getUserID = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_ID)));
            String getUserEmail = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_EMAIL)));
            String getUserPass = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_APP_PASS)));
            String getUserKey = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_APP_ENCRYPTION_KEY)));
            String getUserSalt = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_APP_ENCRYPTION_SALT)));
            String getUserOrig = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_APP_PASS_ORIG)));

user.setOriginal(getUserOrig);
            user.setSalt(getUserSalt);
            user.setEmail(getUserEmail);
            user.setPass(getUserPass);
            user.setKey(getUserKey);
            return user;
        }
        return user;
    }
    public String returnpasswordbyemail(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_PASSWORDS,
                        ALL_COLUMNS_PASSWORD,
                        DBHelper.PASSWORD_EMAIL + " = '" + String.valueOf(email) + "'  AND " +
                                DBHelper.PASSWORD_NAME + " = '" + String.valueOf(password) + "'",
                        null,
                        null,
                        null,
                        null,
                        null);
        if (cursor != null && cursor.moveToFirst()) {

            String passwords = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PASSWORD_NAME)));
            return passwords;
        }
        return null;
    }
    public Password getPassword(String _id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_PASSWORDS,
                        ALL_COLUMNS_PASSWORD,
                        DBHelper.PASSWORD_ID + " = '" + String.valueOf(_id) + "'",
                        null,
                        null,
                        null,
                        null,
                        null);
        Password password = null;
        if (cursor != null && cursor.moveToFirst()) {
            password = new Password();
            String getPasswordFor = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PASSWORD_FOR)));
            String getPasswordName = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PASSWORD_NAME)));
            String getPasswordChange = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PASSWORD_FEQUENCY_OF_CHANGE)));
            String getPasswordDate = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PASSWORD_DATE)));
            String getPasswordEmail = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PASSWORD_EMAIL)));

            password.setEmail(getPasswordEmail);
            password.setCompany(getPasswordFor);
            password.setFrequencyOfChange(getPasswordChange);
            password.setDate(getPasswordDate);
            password.setName(getPasswordName);
            return password;
        }
        return password;
    }


    public User getUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_USERS,
                        ALL_COLUMNS_USERS,
                        DBHelper.USERS_EMAIL + " = '" + String.valueOf(email) + "' AND " +
                                DBHelper.USERS_APP_PASS + " = '" + String.valueOf(password) + "'",
                        null,
                        null,
                        null,
                        null,
                        null);
        User user = null;
        if (cursor != null && cursor.moveToFirst()) {
            user = new User();
            String getUserID = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_ID)));
            String getUserEmail = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_EMAIL)));
            String getUserPass = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_APP_PASS)));
            String getUserKey = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_APP_ENCRYPTION_KEY)));
            String getUserSalt = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_APP_ENCRYPTION_SALT)));
            String getUserOrig = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_APP_PASS_ORIG)));
            user.setOriginal(getUserOrig);
            user.setSalt(getUserSalt);
            user.setEmail(getUserEmail);
            user.setPass(getUserPass);
            user.setKey(getUserKey);
            return user;
        }
        return user;
    }
    public String getPasswordbyEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_PASSWORDS,
                        ALL_COLUMNS_PASSWORD,
                        DBHelper.PASSWORD_EMAIL + " = '" + String.valueOf(email) + "'",
                        null,
                        null,
                        null,
                        null,
                        null);
        if (cursor != null && cursor.moveToFirst()) {
            String emails = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.PASSWORD_EMAIL)));
            return emails;
        }
        return null;
    }
    public String getUserID(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor =
                db.query(TABLE_USERS,
                        ALL_COLUMNS_USERS,
                        DBHelper.USERS_EMAIL + " = '" + String.valueOf(email) + "'",
                        null,
                        null,
                        null,
                        null,
                        null);
        if (cursor != null && cursor.moveToFirst()) {
            String getuserID = String.valueOf(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.USERS_ID)));
            return getuserID;
        }
        return null;
    }

    public int updateUser(User user, String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(USERS_EMAIL, user.getEmail());
        values.put(USERS_APP_PASS, user.getPass());
        values.put(USERS_APP_ENCRYPTION_KEY, user.getKey());
        values.put(USERS_APP_ENCRYPTION_SALT, user.getSalt());
        values.put(USERS_APP_PASS_ORIG, user.getOriginal());
        int i = db.update(TABLE_USERS,
                values,
                DBHelper.USERS_ID + " = '" + String.valueOf(id) + "'",
                null);
        db.close();
        return i;
    }

    public void deletePassword(String id) {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_PASSWORDS,
                    PASSWORD_ID + " = ?",
                    new String[]{String.valueOf(id)});
            db.close();



    }
}