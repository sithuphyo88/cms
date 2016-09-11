package com.ideapro.cms.data;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.io.Serializable;

/**
 * Created by stp on 4/10/2015.
 */

@DatabaseTable(tableName = "user")
public class UserEntity implements Serializable {
    private static final long serialVersionUID = 1L;
    public static final String ROLE_ID = "role";
    public static final int COLUMN_ID = 0;
    public static final String USER_NAME = "name";

    @DatabaseField(id = true)
    public String id;

    @DatabaseField
    public String name;

    @DatabaseField
    public String role;

    @DatabaseField
    public String phone;

    @DatabaseField
    public String email;

    @DatabaseField
    public String password;
}