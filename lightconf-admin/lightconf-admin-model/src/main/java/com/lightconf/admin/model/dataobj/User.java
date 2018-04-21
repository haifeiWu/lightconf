package com.lightconf.admin.model.dataobj;

import java.io.Serializable;

public class User implements Serializable {
    private Integer id;

    private String userName;

    private String password;

    private Byte permission;

    private String permissionProjects;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Byte getPermission() {
        return permission;
    }

    public void setPermission(Byte permission) {
        this.permission = permission;
    }

    public String getPermissionProjects() {
        return permissionProjects;
    }

    public void setPermissionProjects(String permissionProjects) {
        this.permissionProjects = permissionProjects == null ? null : permissionProjects.trim();
    }
}