package com.qubiz.server.entity.dto;

import java.net.URI;
import java.util.Set;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 04.07.2018 #
 ******************************
*/
public class UserProfileDto {
    private int id;
    private String username;
    private String firstName;
    private String lastName;
    private Set<RoleDto> roles;
    private URI profilePictureUrl;
    private int paymentType;
    private String expertDescription;

    public UserProfileDto() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(Set<RoleDto> roles) {
        this.roles = roles;
    }

    public URI getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(URI profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public int getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(int paymentType) {
        this.paymentType = paymentType;
    }

    public String getExpertDescription() {
        return expertDescription;
    }

    public void setExpertDescription(String expertDescription) {
        this.expertDescription = expertDescription;
    }
}
