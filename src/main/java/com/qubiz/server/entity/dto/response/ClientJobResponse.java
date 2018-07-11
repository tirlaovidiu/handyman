package com.qubiz.server.entity.dto.response;

import java.util.List;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 06.07.2018 #
 ******************************
*/
public class ClientJobResponse {
    private JobResponse job;
    private List<JobAssignmentResponse> jobAssignments;

    public ClientJobResponse() {
    }

    public ClientJobResponse(JobResponse job, List<JobAssignmentResponse> jobAssignments) {
        this.job = job;
        this.jobAssignments = jobAssignments;
    }

    public JobResponse getJob() {
        return job;
    }

    public void setJob(JobResponse job) {
        this.job = job;
    }

    public List<JobAssignmentResponse> getJobAssignments() {
        return jobAssignments;
    }

    public void setJobAssignments(List<JobAssignmentResponse> jobAssignments) {
        this.jobAssignments = jobAssignments;
    }
}
