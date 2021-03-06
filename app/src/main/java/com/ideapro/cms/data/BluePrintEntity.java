package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by stp on 4/10/2015.
 */

@DatabaseTable(tableName = "blueprint")
public class BluePrintEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String name;

    public boolean isSelected;
}