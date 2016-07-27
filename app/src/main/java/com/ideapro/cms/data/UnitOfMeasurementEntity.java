package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by asus on 7/26/2016.
 */
@DatabaseTable(tableName = "UOM")
public class UnitOfMeasurementEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String description;
}
