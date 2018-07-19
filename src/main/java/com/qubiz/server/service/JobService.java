package com.qubiz.server.service;

import com.qubiz.server.entity.JobAssignmentStatus;
import com.qubiz.server.entity.JobStatus;
import com.qubiz.server.entity.dto.request.AddJobAssignmentRequest;
import com.qubiz.server.entity.dto.request.AddJobRequest;
import com.qubiz.server.entity.dto.response.ClientJobResponse;
import com.qubiz.server.entity.dto.response.JobAssignmentResponse;
import com.qubiz.server.entity.dto.response.JobResponse;
import com.qubiz.server.entity.model.Job;
import com.qubiz.server.entity.model.JobAssignment;
import com.qubiz.server.entity.model.JobCategory;
import com.qubiz.server.entity.model.User;
import com.qubiz.server.exception.BadAuthenticationException;
import com.qubiz.server.exception.HttpHandyManException;
import com.qubiz.server.repository.JobAssignmentDao;
import com.qubiz.server.repository.JobCategoryDao;
import com.qubiz.server.repository.JobDao;
import com.qubiz.server.repository.LocationDao;
import com.qubiz.server.repository.UserDao;
import ma.glasnost.orika.MapperFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 04.07.2018 #
 ******************************
*/
@Service
public class JobService {

    private final JobDao jobDao;
    private final UserDao userDao;
    private final JobAssignmentDao assignmentDao;
    private final JobCategoryDao jobCategoryDao;
    private final MapperFacade mapperFacade;
    private final LocationDao locationDao;

    @Autowired
    public JobService(JobDao jobDao, UserDao userDao, JobAssignmentDao assignmentDao, JobCategoryDao jobCategoryDao, MapperFacade mapperFacade, LocationDao locationDao) {
        this.jobDao = jobDao;
        this.userDao = userDao;
        this.assignmentDao = assignmentDao;
        this.jobCategoryDao = jobCategoryDao;
        this.mapperFacade = mapperFacade;
        this.locationDao = locationDao;
    }

    public ClientJobResponse getJobById(int clientId, int jobId) {
        Optional<Job> job = jobDao.findById(jobId);
        if (!job.isPresent())
            throw new HttpHandyManException("Job not found", HttpStatus.NOT_FOUND.value());

        if (job.get().getClient().getId() != clientId)
            throw new HttpHandyManException("You are not allowed to view this job details", HttpStatus.FORBIDDEN.value());

        List<JobAssignmentResponse> jobAssignmentResponses = mapperFacade.mapAsList(assignmentDao.findAllByJob(job.get()), JobAssignmentResponse.class);

        JobResponse jobResponse = mapperFacade.map(job.get(), JobResponse.class);

        return new ClientJobResponse(jobResponse, jobAssignmentResponses);
    }

    public List<JobCategory> getCategories() {
        return jobCategoryDao.findAll();
    }

    @Transactional
    public JobResponse addJob(int clientId, AddJobRequest jobRequest) {
        //TODO validate jobRequest
        Optional<User> user = userDao.findById(clientId);
        user.orElseThrow(() -> new BadAuthenticationException("No user match for your session"));

        Optional<JobCategory> category = jobCategoryDao.findById(jobRequest.getJobCategoryId());
        category.orElseThrow(() -> new HttpHandyManException("Invalid selected category", HttpStatus.BAD_REQUEST.value()));

        Job job = mapperFacade.map(jobRequest, Job.class);
        job.setClient(user.get());
        job.setJobCategory(category.get());
        job.setJobStatus(JobStatus.DRAFT);

        return mapperFacade.map(jobDao.save(job), JobResponse.class);
    }

    public List<ClientJobResponse> getJobs(int clientId) {
        Optional<User> user = userDao.findById(clientId);
        user.orElseThrow(() -> new BadAuthenticationException("No user match for your session"));

        return mapperFacade.mapAsList(jobDao.findJobsByClient(user.get()), ClientJobResponse.class);
    }

    public List<JobResponse> getJobsByCoordinates(double latitude, double longitude, double distance, int page, int pageSize) {
        Sort orders = new Sort(Sort.Direction.DESC, "arrivalDate");
        Page<Job> jobs = jobDao.findAllJobsByLocation_LatitudeBetweenAndLocation_LongitudeBetweenAndJobStatus(PageRequest.of(page, pageSize, orders), latitude - distance, latitude + distance, longitude - distance, longitude + distance, JobStatus.IN_PROGRESS);

        return mapperFacade.mapAsList(jobs.getContent(), JobResponse.class);
    }

    @Transactional
    public void bidOnJob(int expertId, int jobId, AddJobAssignmentRequest jobAssignmentRequest) {
        Optional<User> user = userDao.findById(expertId);
        if (!user.isPresent())
            throw new BadAuthenticationException("No user match for your session");
        Optional<Job> job = jobDao.findById(jobId);
        if (!job.isPresent())
            throw new HttpHandyManException("No job found", HttpStatus.NOT_FOUND.value());
        if (job.get().getJobStatus() != JobStatus.IN_PROGRESS)
            throw new HttpHandyManException("This job don't accept any more bids", HttpStatus.BAD_REQUEST.value());
        List<JobAssignment> jobAssignments = assignmentDao.findAllByJob(job.get());
        for (JobAssignment assignment : jobAssignments) {
            if (assignment.getExpert() == user.get())
                throw new HttpHandyManException("You already bid on this job.", HttpStatus.BAD_REQUEST.value());
        }
        JobAssignment jobAssignment = mapperFacade.map(jobAssignmentRequest, JobAssignment.class);
        jobAssignment.setAssignmentStatus(JobAssignmentStatus.IN_PROGRESS);
        jobAssignment.setExpert(user.get());
        jobAssignment.setJob(job.get());
        assignmentDao.save(jobAssignment);
    }

    @Transactional
    public void setExpertForTheJob(int clientId, int jobId, int assignmentId) {
        Optional<User> client = userDao.findById(clientId);
        if (!client.isPresent())
            throw new BadAuthenticationException("No user match for your session");

        Optional<JobAssignment> expertAssignment = assignmentDao.findById(assignmentId);
        if (!expertAssignment.isPresent())
            throw new HttpHandyManException("No job assignment found", HttpStatus.BAD_REQUEST.value());

        Optional<Job> job = jobDao.findById(jobId);
        if (!job.isPresent())
            throw new HttpHandyManException("No job found", HttpStatus.NOT_FOUND.value());

        if (job.get().getJobStatus() == JobStatus.WAITING_FOR_EXPERT)
            throw new HttpHandyManException("This job already have an expert assigned", HttpStatus.BAD_REQUEST.value());

        List<JobAssignment> assignments = assignmentDao.findAllByJob(job.get());
        for (JobAssignment jobAssignment : assignments) {
            if (jobAssignment.equals(expertAssignment.get())) {
                jobAssignment.setAssignmentStatus(JobAssignmentStatus.REJECTED);
            } else {
                jobAssignment.setAssignmentStatus(JobAssignmentStatus.ACCEPTED);
            }
        }
        job.get().setJobStatus(JobStatus.WAITING_FOR_EXPERT);
        //TODO send notifications to users
    }

    @Transactional
    public void updateJob(int clientId, int jobId, JobResponse clientJobResponse) {
        Optional<Job> job = jobDao.findById(jobId);
        if (!job.isPresent())
            throw new HttpHandyManException("Job not found", HttpStatus.NOT_FOUND.value());

        if (job.get().getClient().getId() != clientId)
            throw new HttpHandyManException("You are not allowed to update this job details", HttpStatus.FORBIDDEN.value());

        if (job.get().getJobStatus() == JobStatus.COMPLETED || job.get().getJobStatus() == JobStatus.DELETED || job.get().getJobStatus() == JobStatus.WAITING_FOR_EXPERT)
            throw new HttpHandyManException("You are not allowed to update this job details", HttpStatus.FORBIDDEN.value());

        Job updatedJob = job.get();
        Job clientJob = mapperFacade.map(clientJobResponse, Job.class);
        if (clientJob.getJobStatus() != null)
            updatedJob.setJobStatus(clientJob.getJobStatus());
        if (clientJob.getJobCategory() != null)
            updatedJob.setJobCategory(clientJob.getJobCategory());
        if (clientJob.getArrivalDate() != 0)
            updatedJob.setArrivalDate(clientJob.getArrivalDate());
        if (clientJob.getDescription() != null)
            updatedJob.setDescription(clientJob.getDescription());
        if (clientJob.getLocation() != null)
            updatedJob.setLocation(clientJob.getLocation());

        jobDao.save(updatedJob);
    }

    @Transactional
    public void uploadPhotoToJob(int jobId, int clientId, MultipartFile inputImage) {
        Optional<Job> job = jobDao.findById(jobId);
        if (!job.isPresent())
            throw new HttpHandyManException("Job not found", HttpStatus.NOT_FOUND.value());

        if (job.get().getClient().getId() != clientId)
            throw new HttpHandyManException("You are not allowed to update this job details", HttpStatus.FORBIDDEN.value());

        if (job.get().getJobStatus() == JobStatus.COMPLETED || job.get().getJobStatus() == JobStatus.DELETED || job.get().getJobStatus() == JobStatus.WAITING_FOR_EXPERT)
            throw new HttpHandyManException("You are not allowed to update this job details", HttpStatus.FORBIDDEN.value());

        try {
            Image image = ImageIO.read(inputImage.getInputStream());
            //TODO save image in amazon
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpHandyManException("Failed to store photo", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }
}
