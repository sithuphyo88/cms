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

    @DatabaseField(id = true)
    public UUID subContracotr_id = UUID.randomUUID();

    @DatabaseField
    public String name;

    @DatabaseField
    public String phone;

    @DatabaseField
    public String email;

    @DatabaseField
    public String address;
}