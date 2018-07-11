package com.qubiz.server.config;

import com.qubiz.server.entity.dto.request.AddJobAssignmentRequest;
import com.qubiz.server.entity.dto.request.AddJobRequest;
import com.qubiz.server.entity.dto.response.JobAssignmentResponse;
import com.qubiz.server.entity.dto.response.JobCategoryResponse;
import com.qubiz.server.entity.dto.response.JobResponse;
import com.qubiz.server.entity.dto.LocationDto;
import com.qubiz.server.entity.dto.RoleDto;
import com.qubiz.server.entity.dto.UserProfileDto;
import com.qubiz.server.entity.model.Job;
import com.qubiz.server.entity.model.JobAssignment;
import com.qubiz.server.entity.model.JobCategory;
import com.qubiz.server.entity.model.Location;
import com.qubiz.server.entity.model.Role;
import com.qubiz.server.entity.model.User;
import ma.glasnost.orika.CustomMapper;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.MappingContext;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.stereotype.Component;

/*
 ******************************
 # Created by Tirla Ovidiu #
 # 06.07.2018 #
 ******************************
*/
@Component
public class MappingConfig extends ConfigurableMapper {
    protected void configure(MapperFactory factory) {
        factory.classMap(User.class, UserProfileDto.class)
                .exclude("id")
                .byDefault()
                .customize(
                        new CustomMapper<User, UserProfileDto>() {
                            @Override
                            public void mapAtoB(User user, UserProfileDto userProfileDto, MappingContext context) {
                                userProfileDto.setId(user.getId());
                            }
                        }
                )
                .register();

        factory.classMap(Location.class, LocationDto.class)
                .byDefault()
                .register();

        factory.classMap(JobCategory.class, JobCategoryResponse.class)
                .byDefault()
                .register();

        factory.classMap(JobAssignment.class, JobAssignmentResponse.class)
                .field("expert", "expertProfile")
                .field("assignmentStatus", "jobAssignmentStatus")
                .byDefault()
                .register();

        factory.classMap(Job.class, JobResponse.class)
                .field("id", "jobId")
                .field("client", "clientProfile")
                .field("jobCategory", "jobCategory")
                .field("description", "jobDescription")
                .field("location", "jobLocation")
                .byDefault()
                .register();

        factory.classMap(Role.class, RoleDto.class)
                .byDefault()
                .register();

        factory.classMap(AddJobRequest.class, Job.class)
                .byDefault()
                .register();
        factory.classMap(AddJobAssignmentRequest.class, JobAssignment.class)
                .byDefault()
                .register();

    }
}
