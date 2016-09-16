package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by stp on 4/10/2015.
 */

@DatabaseTable(tableName = "siteprogresshistory")
public class SiteProgressHistoryEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String TASK_ID = "task_id";

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String siteName;

    @DatabaseField
    public String project_id;

    @DatabaseField
    public String site_id;

    @DatabaseField
    public String task_id;

    @DatabaseField
    public String date;

    @DatabaseField
    public String engineerName;

    @DatabaseField
    public String description;

    @DatabaseField
    public String progress;
}