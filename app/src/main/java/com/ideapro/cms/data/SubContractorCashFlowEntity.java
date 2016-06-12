package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by stp on 4/10/2015.
 */

@DatabaseTable(tableName = "subContractorCashFlow")
public class SubContractorCashFlowEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String date;

    @DatabaseField
    public long creditAmount;

    @DatabaseField
    public long paidAmount;

    @DatabaseField
    public String description;
}