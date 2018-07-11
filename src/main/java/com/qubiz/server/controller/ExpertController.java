package com.qubiz.server.controller;

import com.qubiz.server.config.UserDetails;
import com.qubiz.server.entity.dto.request.AddJobAssignmentRequest;
import com.qubiz.server.entity.dto.response.JobResponse;
import com.qubiz.server.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 10.07.2018 #
 ******************************
*/
@RestController
@RequestMapping("/experts")
public class ExpertController {

    private final JobService jobService;

    @Autowired
    public ExpertController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping("/jobs")
    public ResponseEntity getJobById(@RequestParam("long") double longitude, @RequestParam("lat") double latitude, @RequestParam(value = "dist", required = false, defaultValue = "1") double distance, @RequestParam(value = "size", defaultValue = "10", required = false) int pageSize, @RequestParam(value = "page", required = false, defaultValue = "0") int page) {
        if (distance > 2.0 || distance < 0)
            distance = 1;
        if (page < 0)
            page = 0;
        if (pageSize > 20 || pageSize < 1)
            pageSize = 10;

        List<JobResponse> jobResponses = jobService.getJobsByCoordinates(latitude, longitude, distance, page, pageSize);
        return ResponseEntity.status(HttpStatus.OK).body(jobResponses);
    }

    @PostMapping("/jobs/{id}")
    public ResponseEntity bidOnJob(@PathVariable("id") int jobId, @RequestBody AddJobAssignmentRequest jobAssignmentRequest) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobService.bidOnJob(userDetails.getClientId(), jobId, jobAssignmentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
