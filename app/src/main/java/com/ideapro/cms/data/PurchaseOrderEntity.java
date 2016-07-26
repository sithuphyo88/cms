package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by stp on 4/10/2015.
 */

@DatabaseTable(tableName = "purchaseOrder")
public class PurchaseOrderEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String PROJECT_ID = "projectId";

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String projectId;

    @DatabaseField
    public String purchaseOrderNo;

    @DatabaseField
    public String date;

    @DatabaseField
    public int receivedNumber;

    @DatabaseField
    public int purchasedNumber;
}