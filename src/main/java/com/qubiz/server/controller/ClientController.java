package com.qubiz.server.controller;

import com.qubiz.server.config.UserDetails;
import com.qubiz.server.entity.dto.PhotoDto;
import com.qubiz.server.entity.dto.request.AddJobRequest;
import com.qubiz.server.entity.dto.response.ClientJobResponse;
import com.qubiz.server.entity.dto.response.JobResponse;
import com.qubiz.server.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
        JobResponse jobResponse = jobService.addJob(userDetails.getClientId(), jobRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(jobResponse);
    }

    @PostMapping("/jobs/{id}/photos")
    public ResponseEntity uploadPhoto(@PathVariable("id") int jobId, @RequestParam("image") MultipartFile image) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PhotoDto photoDto = jobService.uploadPhotoToJob(jobId, userDetails.getClientId(), image);
        return ResponseEntity.status(HttpStatus.OK).body(photoDto);
    }

    @DeleteMapping("/jobs/{jobId}/photos/{photoId}")
    public ResponseEntity deletePhoto(@PathVariable("jobId") int jobId, @PathVariable("photoId") int photoId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobService.deletePhotoFromJob(jobId, photoId, userDetails.getClientId());
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/jobs/{jobId}/photos/{photoId}")
    public ResponseEntity getPhotoById(@PathVariable("jobId") int jobId, @PathVariable("photoId") int photoId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PhotoDto photoDto = jobService.getPhotoById(jobId, photoId, userDetails.getClientId());
        return ResponseEntity.status(HttpStatus.OK).body(photoDto);
    }

    @PutMapping("/jobs/{id}")
    public ResponseEntity updateJobById(@PathVariable("id") int jobId, @RequestBody JobResponse updatedJob) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobService.updateJob(userDetails.getClientId(), jobId, updatedJob);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/jobs")
    public ResponseEntity getJobs() {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ClientJobResponse> jobResponses = jobService.getJobs(userDetails.getClientId());
        return ResponseEntity.status(HttpStatus.OK).body(jobResponses);
    }

    @PostMapping("/jobs/{jobId}/assignments/{assignmentId}")
    public ResponseEntity setExpertForTheJob(@PathVariable("jobId") int jobId, @PathVariable("assignmentId") int assignmentId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        jobService.setExpertForTheJob(userDetails.getClientId(), jobId, assignmentId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
