package com.insurance.mgmt.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.insurance.mgmt.entity.Jobs;
import com.insurance.mgmt.repository.IJobsRepository;

@Service
public class JobsService {
	@Autowired
    private IJobsRepository jobsRepository;

    public List<Jobs> getAllJobs() {
        return jobsRepository.findAll();
    }

    public Jobs getJobById(String jobId) {
        return jobsRepository.findById(jobId).orElse(null);
    }
}
