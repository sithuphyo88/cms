package com.ideapro.cms.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static String DB_PATH = Environment.getExternalStorageDirectory().getPath()+ "/data/com.ideapro.tmf/databases/";
    private static String DB_NAME = "tmf.db";
    private static final int DB_VERSION = 1;
    private Context context;

    public DatabaseHelper(Context context) {
        super(context, DB_PATH + DB_NAME, null, DB_VERSION);

        this.context = context;
        try {
            createDatabase();
        } catch (IOException e) {
            throw new Error(e);
        }
    }

    public void createDatabase() throws IOException{
        boolean dbExist = checkDatabase();

        if(!dbExist){
            try {
                copyDatabase(context);
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDatabase(){

        boolean isExist = false;

        String myPath = DB_PATH + DB_NAME;
        File file = new File(myPath);
        isExist = file.exists();
        if(isExist){
            file.setExecutable(true);
        }

        return isExist;
    }

    private static void copyDatabase(Context context) throws IOException{

        InputStream myInput = context.getAssets().open(DB_NAME);

        String outFileName = DB_PATH + DB_NAME;

        File fileDirectory = new File(DB_PATH);
        if(!fileDirectory.exists()){
            fileDirectory.mkdirs();
        }

        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, ConnectionSource connectionSource, int i, int i2) {
        switch (sqLiteDatabase.getVersion()) {
            case 2:
                break;
        }
    }
}