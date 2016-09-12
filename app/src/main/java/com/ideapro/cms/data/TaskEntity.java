package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by stp on 4/10/2015.
 */

@DatabaseTable(tableName = "task")
public class TaskEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static String SITE_ID = "site_id";
    public static String TITLE = "title";

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String project_id;

    @DatabaseField
    public String site_id;

    @DatabaseField
    public String title;

    @DatabaseField
    public String description;

    @DatabaseField
    public String startDate;

    @DatabaseField
    public String endDate;

    @DatabaseField
    public String assignee;
}