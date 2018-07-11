package com.qubiz.server.controller;

import com.qubiz.server.config.UserDetails;
import com.qubiz.server.entity.model.Job;
import com.qubiz.server.service.JobService;
import com.qubiz.server.entity.dto.request.AddJobRequest;
import com.qubiz.server.entity.dto.response.ClientJobResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 04.07.2018 #
 ******************************
*/
@RestController
@RequestMapping("/clients")
public class ClientController {

    private final JobService jobService;

    @Autowired
    public ClientController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs/{id}")
    public ResponseEntity getJobById(@PathVariable("id") int jobId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ClientJobResponse job = jobService.getJobById(userDetails.getClientId(), jobId);
        return ResponseEntity.status(HttpStatus.OK).body(job);
    }

    @PostMapping("/jobs")
    public ResponseEntity addJob(@RequestBody AddJobRequest jobRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Job job = jobService.addJob(userDetails.getClientId(), jobRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @GetMapping("/jobs")
    public ResponseEntity getJobs() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Job> jobs = jobService.getJobs(userDetails.getClientId());
        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }

    @PostMapping("/jobs/{jobId}/assignments/{assignmentId}")
    public ResponseEntity setExpertForTheJob(@PathVariable("jobId") int jobId, @PathVariable("assignmentId") int assignmentId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobService.setExpertForTheJob(userDetails.getClientId(), jobId, assignmentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
