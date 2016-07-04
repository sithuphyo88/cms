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
    Dao<RoleEntity, String> roleEntityDao;
    Dao<PermissionEntity, String> permissionEntityDao;
    // project Entity Dao
    Dao<ProjectEntity, String> projectEntityDao;
    // site Entity Dao
    Dao<SiteEntity, String> siteEntityDao;
    // task Entity Dao
    Dao<TaskEntity, String> taskEntityDao;
    // customer Entity Dao
    Dao<CustomerEntity, String> customerEntityDao;


    public DaoFactory(Context context) {
        db = new DatabaseHelper(context);

    }

    public Dao<AppSettingsEntity, String> getAppSettingsDao() throws SQLException {
        if (appSettingsDao == null) {
            appSettingsDao = db.getDao(AppSettingsEntity.class);
        }

        return appSettingsDao;
    }


    // DAO for Role Entity
    public Dao<RoleEntity, String> getRoleEntityDao() throws SQLException {
        if (roleEntityDao == null) {
            roleEntityDao = db.getDao(RoleEntity.class);
        }
        return roleEntityDao;
    }

    //DAO for Permission Entity
    public Dao<PermissionEntity, String> getPermissionEntityDao() throws SQLException {
        if (permissionEntityDao == null) {
            permissionEntityDao = db.getDao(PermissionEntity.class);
        }
        return permissionEntityDao;
    }

    // DAO for Project Entity
    public Dao<ProjectEntity, String> getProjectEntityDao() throws SQLException {
        if (projectEntityDao == null) {
            projectEntityDao = db.getDao(ProjectEntity.class);
        }
        return projectEntityDao;
    }

    // DAO for Site Entity
    public Dao<SiteEntity, String> getSiteEntityDao() throws SQLException {
        if (siteEntityDao == null) {
            siteEntityDao = db.getDao(SiteEntity.class);
        }
        return siteEntityDao;
    }

    // DAO for Task Entity
    public Dao<TaskEntity, String> getTaskEntityDao() throws SQLException {
        if (taskEntityDao == null) {
            taskEntityDao = db.getDao(TaskEntity.class);
        }
        return taskEntityDao;
    }

    // DAO for Customer Entity
    public Dao<CustomerEntity, String> getCustomerEntityDao() throws SQLException {
        if (customerEntityDao == null) {
            customerEntityDao = db.getDao(CustomerEntity.class);
        }
        return customerEntityDao;
    }
}
