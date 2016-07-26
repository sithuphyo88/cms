package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by stp on 4/10/2015.
 */

@DatabaseTable(tableName = "purchaseOrderItem")
public class PurchaseOrderItemEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_PURCHASE_ORDER_ID =1;
    public static final int COLUMN_PURCHASE_ORDER_DATE =2;
    public static final int COLUMN_TARGETED_DATE =3;
    public static final int COLUMN_MATERIAL_CATEGORY =4;
    public static final int COLUMN_MATERIAL_ITEM =5;
    public static final int COLUMN_UOM =6;
    public static final int COLUMN_ORDER_QUANTITY =7;
    public static final int COLUMN_RECEIVE_QUANTITY =8;
    public static final int COLUMN_REMARKS =9;

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String purchaseOrderId;

    @DatabaseField
    public String purchaseOrderDate;

    @DatabaseField
    public String targetedDate;

    @DatabaseField
    public String materialCategory;

    @DatabaseField
    public String materialItem;

    @DatabaseField
    public String uom;

    @DatabaseField
    public int orderedQuantity;

    @DatabaseField
    public int receivedQuantity;

    @DatabaseField
    public String remarks;
}