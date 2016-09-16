package com.ideapro.cms.data;

import android.content.Context;

import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;
import java.util.UUID;

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
    // subContractor Dao
    Dao<SubContractorEntity, String> subContratorEntityDao;
    // material Dao
    Dao<MaterialEntity, String> materialEntityDao;
    // material Category Dao
    Dao<MaterialCategoryEntity, String> materialCategoryEntityDao;
    // user Dao
    Dao<UserEntity, String> userEntityDao;
    // purchaseOrder Dao
    Dao<PurchaseOrderEntity, String> purchaseOrderEntityDao;
    // purchaseOrderItem Dao
    private Dao<PurchaseOrderItemEntity, String> purchaseOrderItemEntityDao;
    // UOM Dao
    private Dao<UnitOfMeasurementEntity, String> unitOfMeasurementEntityDao;
    // Sub Contractor Cash Flow Dao
    private Dao<SubContractorCashFlowEntity, String> subContractorCashFlowEntityDao;
    // Site Progress History Dao
    Dao<SiteProgressHistoryEntity, String> siteProgressHistoryDao;


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

    // DAO for Sub Contractor Entity
    public Dao<SubContractorEntity, String> getSubContractorEntityDao() throws SQLException {
        if (subContratorEntityDao == null) {
            subContratorEntityDao = db.getDao(SubContractorEntity.class);
        }
        return subContratorEntityDao;
    }

    // DAO for material Entity
    public Dao<MaterialEntity, String> getMaterialEntityDao() throws SQLException {
        if (materialEntityDao == null) {
            materialEntityDao = db.getDao(MaterialEntity.class);
        }
        return materialEntityDao;
    }

    // DAO for material group Entity
    public Dao<MaterialCategoryEntity, String> getMaterialCategoryEntityDao() throws SQLException {
        if (materialCategoryEntityDao == null) {
            materialCategoryEntityDao = db.getDao(MaterialCategoryEntity.class);
        }
        return materialCategoryEntityDao;
    }

    // DAO for User Entity
    public Dao<UserEntity, String> getUserEntityDao() throws SQLException {
        if (userEntityDao == null) {
            userEntityDao = db.getDao(UserEntity.class);
        }
        return userEntityDao;
    }

    // DAO for PurchaseOrder Entity
    public Dao<PurchaseOrderEntity, String> getPurchaseOrderEntityDao() throws SQLException {
        if (purchaseOrderEntityDao == null) {
            purchaseOrderEntityDao = db.getDao(PurchaseOrderEntity.class);
        }
        return purchaseOrderEntityDao;
    }

    // DAO for PurchaseOrderItem Entity
    public Dao<PurchaseOrderItemEntity, String> getPurchaseOrderItemDao() throws SQLException {
        if (purchaseOrderItemEntityDao == null) {
            purchaseOrderItemEntityDao = db.getDao(PurchaseOrderItemEntity.class);
        }
        return purchaseOrderItemEntityDao;
    }

    // DAO for Unit Of Measurement Entity
    public Dao<UnitOfMeasurementEntity, String> getUnitOfMeasurementDao() throws SQLException {
        if (unitOfMeasurementEntityDao == null) {
            unitOfMeasurementEntityDao = db.getDao(UnitOfMeasurementEntity.class);
        }
        return unitOfMeasurementEntityDao;
    }

    // DAO for Sub Contractor Cash Flow Entity
    public Dao<SubContractorCashFlowEntity, String> getSubContractorCashFlowEntityDao() throws SQLException {
        if (subContractorCashFlowEntityDao == null) {
            subContractorCashFlowEntityDao = db.getDao(SubContractorCashFlowEntity.class);
        }
        return subContractorCashFlowEntityDao;
    }

    // DAO for Sub Contractor Cash Flow Entity
    public Dao<SiteProgressHistoryEntity, String> getSiteProgressHistoryDao() throws SQLException {
        if (siteProgressHistoryDao == null) {
            siteProgressHistoryDao = db.getDao(SiteProgressHistoryEntity.class);
        }
        return siteProgressHistoryDao;
    }
}

