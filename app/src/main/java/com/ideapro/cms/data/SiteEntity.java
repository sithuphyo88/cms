package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by stp on 4/10/2015.
 */

@DatabaseTable(tableName = "site")
public class SiteEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String NAME = "name";
    public static String PROJECT_ID = "project_id";

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String project_id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String address;

    @DatabaseField
    public String startDate;

    @DatabaseField
    public String endDate;

    @DatabaseField
    public String progress;
}