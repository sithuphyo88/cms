package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by stp on 4/10/2015.
 */

@DatabaseTable(tableName = "appSettings")
public class AppSettingsEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static String KEY_FILED_NAME = "key";
    public static String VALUE_FILED_NAME = "value";
    public static String SERVICE_LINK = "service_link";
    public static String GATE_ID = "gate_id";
    public static String PHONE_DESCRIPTION = "phone_description";
    public static String IS_USE_BACK_CAMERA = "is_use_back_camera";

    @DatabaseField(id = true)
    public String key;

    @DatabaseField
    public String value;
}