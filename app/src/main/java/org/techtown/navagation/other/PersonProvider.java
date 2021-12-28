package org.techtown.navagation.other;

import static java.sql.DriverManager.println;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.techtown.navagation.other.DatabaseHelper;

public class PersonProvider extends ContentProvider {
    private static final String AUTHORITY = "org.techtown.navagation";
    private static final String BASE_PATH = "person";
    public static final Uri CONTENT_URI =Uri.parse("content://"+AUTHORITY+"/"+BASE_PATH);

    private static  final int PERSONS = 1;
    private static final int PERSON_ID = 2;
    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        uriMatcher.addURI(AUTHORITY,BASE_PATH,PERSONS);
        uriMatcher.addURI(AUTHORITY,BASE_PATH+"/#",PERSON_ID);
    }
    private SQLiteDatabase database;
    @Override
    public boolean onCreate() {
        DatabaseHelper helper = new DatabaseHelper(getContext());
        database = helper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;
        switch (uriMatcher.match(uri)){
            case PERSONS:
                println("ok");
                cursor = database.query(DatabaseHelper.TABLE_NAME,
                        DatabaseHelper.ALL_COLIMNS,
                        selection,null,null,null,DatabaseHelper.PERSON_ID+" ASC");
                break;
            default:
                throw new IllegalArgumentException("알수없는 URI");
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        long id = database.insert(DatabaseHelper.TABLE_NAME,null,values);

        if(id>0){
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI,id);
            getContext().getContentResolver().notifyChange(_uri,null);
            return _uri;
        }
        throw new SQLException("추가실패");
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
