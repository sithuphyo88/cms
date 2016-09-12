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
    public static final String PROJECT_ID = "project_id";
    public static final String SUB_CONTRACTOR_ID = "subContractor_id";
    public static final String DESC = "description";

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String project_id;

    @DatabaseField
    public String subContractor_id;

    @DatabaseField
    public String date;

    @DatabaseField
    public long creditAmount;

    @DatabaseField
    public long paidAmount;

    @DatabaseField
    public String description;
}