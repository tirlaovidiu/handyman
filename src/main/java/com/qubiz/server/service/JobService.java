package com.qubiz.server.service;

import com.qubiz.server.entity.JobAssignmentStatus;
import com.qubiz.server.entity.JobStatus;
import com.qubiz.server.entity.dto.PhotoDto;
import com.qubiz.server.entity.dto.request.AddJobAssignmentRequest;
import com.qubiz.server.entity.dto.request.AddJobRequest;
import com.qubiz.server.entity.dto.response.ClientJobResponse;
import com.qubiz.server.entity.dto.response.JobAssignmentResponse;
import com.qubiz.server.entity.dto.response.JobResponse;
import com.qubiz.server.entity.model.Job;
import com.qubiz.server.entity.model.JobAssignment;
import com.qubiz.server.entity.model.JobCategory;
import com.qubiz.server.entity.model.Location;
import com.qubiz.server.entity.model.Photo;
import com.qubiz.server.entity.model.User;
import com.qubiz.server.exception.BadAuthenticationException;
import com.qubiz.server.exception.HttpHandyManException;
import com.qubiz.server.repository.JobAssignmentDao;
import com.qubiz.server.repository.JobCategoryDao;
import com.qubiz.server.repository.JobDao;
import com.qubiz.server.repository.LocationDao;
import com.qubiz.server.repository.PhotoDao;
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
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import static com.qubiz.server.util.Constants.MAX_PHOTO_NUMBER_PER_JOB;
import static com.qubiz.server.util.Constants.MAX_PHOTO_SIZE;

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
    private final PhotoDao photoDao;
    private final AmazonClient amazonClient;

    @Autowired
    public JobService(JobDao jobDao, UserDao userDao, JobAssignmentDao assignmentDao, JobCategoryDao jobCategoryDao, MapperFacade mapperFacade, LocationDao locationDao, AmazonClient amazonClient, PhotoDao photoDao) {
        this.jobDao = jobDao;
        this.userDao = userDao;
        this.assignmentDao = assignmentDao;
        this.jobCategoryDao = jobCategoryDao;
        this.mapperFacade = mapperFacade;
        this.locationDao = locationDao;
        this.amazonClient = amazonClient;
        this.photoDao = photoDao;
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
        if (clientJob.getJobCategory() != null) {
            Optional<JobCategory> jobCategory = jobCategoryDao.findById(clientJob.getJobCategory().getId());
            if (!jobCategory.isPresent()) {
                throw new HttpHandyManException("No job category found", HttpStatus.BAD_REQUEST.value());
            }
            updatedJob.setJobCategory(jobCategory.get());
        }
        if (clientJob.getArrivalDate() != 0) {
            Calendar currentTime = Calendar.getInstance();
            if (clientJob.getArrivalDate() < currentTime.getTimeInMillis())
                throw new HttpHandyManException("Arrival date can't be set in the past", HttpStatus.BAD_REQUEST.value());
            updatedJob.setArrivalDate(clientJob.getArrivalDate());
        }
        if (clientJob.getDescription() != null)
            updatedJob.setDescription(clientJob.getDescription());
        if (clientJob.getLocation() != null) {
            Location location = job.get().getLocation();
            location.setAddressName(clientJob.getLocation().getAddressName());
            location.setLatitude(clientJob.getLocation().getLatitude());
            location.setLongitude(clientJob.getLocation().getLongitude());
            updatedJob.setLocation(location);
        }

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

        if (job.get().getPhotos().size() > MAX_PHOTO_NUMBER_PER_JOB)
            throw new HttpHandyManException("You exceeded the maxim number allowed of photos", HttpStatus.BAD_REQUEST.value());
        try {
            BufferedImage image = ImageIO.read(inputImage.getInputStream());
            if (image == null)
                throw new HttpHandyManException("Unsupported image type", HttpStatus.BAD_REQUEST.value());

            int scale;
            if (image.getHeight() > image.getWidth()) {
                scale = image.getHeight() / MAX_PHOTO_SIZE;
            } else {
                scale = image.getWidth() / MAX_PHOTO_SIZE;
            }
            if (scale > 1) {
                int newWidth = image.getWidth() / scale;
                int newHeight = image.getHeight() / scale;

                BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TRANSLUCENT);
                Graphics2D g2 = resizedImg.createGraphics();
                g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                g2.drawImage(image, 0, 0, newWidth, newHeight, null);
                g2.dispose();
                image = resizedImg;
            }
            String fileName = amazonClient.uploadPhoto(image, jobId);
            Photo photo = new Photo();
            photo.setFileName(fileName);
            photo.setAwsUrl(amazonClient.getEndpointUrl() + "/" + fileName);
            photo.setJob(job.get());
            job.get().getPhotos().add(photo);
        } catch (IOException e) {
            e.printStackTrace();
            throw new HttpHandyManException("Failed to store photo", HttpStatus.INTERNAL_SERVER_ERROR.value());
        }
    }

    public void deletePhotoFromJob(int jobId, int photoId, int clientId) {
        Optional<Job> job = jobDao.findById(jobId);
        Optional<Photo> photo = photoDao.findById(photoId);
        if (!job.isPresent())
            throw new HttpHandyManException("Job not found", HttpStatus.NOT_FOUND.value());

        if (job.get().getClient().getId() != clientId)
            throw new HttpHandyManException("You are not allowed to update this job details", HttpStatus.FORBIDDEN.value());

        if (job.get().getJobStatus() == JobStatus.COMPLETED || job.get().getJobStatus() == JobStatus.DELETED || job.get().getJobStatus() == JobStatus.WAITING_FOR_EXPERT)
            throw new HttpHandyManException("You are not allowed to update this job details", HttpStatus.FORBIDDEN.value());

        if (!photo.isPresent()) {
            throw new HttpHandyManException("Photo not found", HttpStatus.NOT_FOUND.value());
        }

        if (photo.get().getJob().equals(job.get()))
            throw new HttpHandyManException("You are not allowed to delete this photo", HttpStatus.FORBIDDEN.value());

        amazonClient.deletePhoto(photo.get().getFileName());
    }

    public PhotoDto getPhotoById(int jobId, int photoId, int clientId) {
        Optional<Job> job = jobDao.findById(jobId);
        Optional<Photo> photo = photoDao.findById(photoId);
        if (!job.isPresent())
            throw new HttpHandyManException("Job not found", HttpStatus.NOT_FOUND.value());

        if (job.get().getClient().getId() != clientId)
            throw new HttpHandyManException("You are not allowed to update this job details", HttpStatus.FORBIDDEN.value());

        if (job.get().getJobStatus() == JobStatus.COMPLETED || job.get().getJobStatus() == JobStatus.DELETED || job.get().getJobStatus() == JobStatus.WAITING_FOR_EXPERT)
            throw new HttpHandyManException("You are not allowed to update this job details", HttpStatus.FORBIDDEN.value());

        if (!photo.isPresent()) {
            throw new HttpHandyManException("Photo not found", HttpStatus.NOT_FOUND.value());
        }

        if (photo.get().getJob().equals(job.get()))
            throw new HttpHandyManException("You are not allowed to view this photo", HttpStatus.FORBIDDEN.value());

        return mapperFacade.map(photo.get(), PhotoDto.class);
    }
}
