package com.qubiz.server.entity;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.Set;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 07.06.2018 #
 ******************************
*/
@Entity
public class Expert extends User {

    private String description;

    @OneToMany
    private Set<JobCategory> jobCategories;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<JobCategory> getJobCategories() {
        return jobCategories;
    }

    public void setJobCategories(Set<JobCategory> jobCategories) {
        this.jobCategories = jobCategories;
    }
}
