package com.qubiz.server.entity.dto;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 08.07.2018 #
 ******************************
*/
public class RoleDto {
    private int id;
    private String roleName;

    public RoleDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
