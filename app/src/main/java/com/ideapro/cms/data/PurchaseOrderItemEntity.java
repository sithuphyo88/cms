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