package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;
import java.util.UUID;

/**
 * Created by stp on 4/10/2015.
 */

@DatabaseTable(tableName = "subContractor")
public class SubContractorEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final int COLUMN_ID = 0;
    public static final int COLUMN_NAME = 1;
    public static final int COLUMN_PHONE = 2;
    public static final int COLUMN_EMAIL = 3;
    public static final int COLUMN_ADDRESS = 4;

    @DatabaseField(id = true)
    public String subContracotr_id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String phone;

    @DatabaseField
    public String email;

    @DatabaseField
    public String address;
}