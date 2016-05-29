package com.ideapro.cms.data;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by stp on 4/10/2015.
 */
public class DaoFactory {
    DatabaseHelper db;
    Dao<AppSettingsEntity, String> appSettingsDao;

    public DaoFactory(Context context)
    {
        db = new DatabaseHelper(context);

    }

    public Dao<AppSettingsEntity, String> getAppSettingsDao() throws SQLException {
        if (appSettingsDao == null) {
            appSettingsDao = db.getDao(AppSettingsEntity.class);
        }

        return appSettingsDao;
    }
}
