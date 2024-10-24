package com.soul.emr.admin.controller;

import com.netflix.graphql.dgs.DgsComponent;
import com.netflix.graphql.dgs.DgsMutation;
import com.netflix.graphql.dgs.DgsQuery;
import com.netflix.graphql.dgs.InputArgument;
import com.soul.emr.doctor.service.EmployeeService;
import com.soul.emr.model.entity.modelbusinessgroup.businessgroupdb.BusinessGroupDB;
import com.soul.emr.model.entity.modelbusinessgroup.graphqlentity.BusinessGroupInput;
import com.soul.emr.model.entity.modelemployee.graphqlentity.EmployeeInfoInput;
import com.soul.emr.model.entity.modelonetimepassword.graphqlentity.OneTimePasswordInput;
import com.soul.emr.model.entity.modelemployee.registrationdb.EmployeeInfoDB;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.graphql.data.GraphQlRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.Map;

@GraphQlRepository
@DgsComponent
@CrossOrigin
@Validated
public class AdminGraphQLController
{
    private final EmployeeService employeeService;

    @Autowired
    public AdminGraphQLController(EmployeeService employeeService){
        super();
        this.employeeService = employeeService;
    }

    //This authorization is added as one Admin can create another admin, so initially one admin will be created with default admin
     @DgsMutation
    @PreAuthorize("hasAuthority('ADMIN')")
    public EmployeeInfoDB createAdminInfo(@InputArgument(value = "employeeInfoInput") EmployeeInfoInput employeeInfoInput) {
        // Call your service to create the student
        return employeeService.registerUser(employeeInfoInput);
    }

    //this authorization is added as one Admin can create another admin, so initially one admin will be created with default admin
    @DgsMutation
    public Map<String, Object> sendOTPForAdminRegistration(@InputArgument(value = "oneTimePasswordInput") OneTimePasswordInput oneTimePasswordInput) {
        // Call your service to create the Admin
        return employeeService.generateOtp(oneTimePasswordInput);
    }

   //this authorization is added as one Admin can create another admin, so initially one admin will be created with default
	@DgsMutation
    public Map<String, Object> verifyOTPForAdminRegistration(@InputArgument(value="oneTimePasswordInput") OneTimePasswordInput oneTimePasswordInput){
        // Call your service to create the Admin
        return employeeService.verifyOtp(oneTimePasswordInput);
    }
    
    
    @DgsQuery
    @PreAuthorize("hasAuthority('ADMIN')")
    public Page <BusinessGroupDB> getBusinessGroup(@InputArgument(value = "page") Integer page, @InputArgument(value = "size") Integer size)
    {
        return employeeService.getBusinessGroup(page, size);
    }

}
