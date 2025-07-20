package com.infraxus.global.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.BuildWithDetails;
import com.offbytwo.jenkins.model.Job;
import com.offbytwo.jenkins.model.JobWithDetails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class JenkinsService {
    private final JenkinsServer jenkinsServer;

    public JenkinsService(
            @Value("${jenkins.url}") String url,
            @Value("${jenkins.username}") String username,
            @Value("${jenkins.token}") String token
    ) throws URISyntaxException {
        this.jenkinsServer = new JenkinsServer(new URI(url), username, token);
    }

    public Map<String, Job> getAllJobs() throws IOException {
        return jenkinsServer.getJobs();
    }

    public JobWithDetails getJob(String jobName) throws IOException {
        return jenkinsServer.getJob(jobName);
    }

    public void createOrUpdateJob(String jobName, String xml) throws IOException {
        if (jenkinsServer.getJob(jobName) != null) {
            // Job이 이미 존재하는 경우: 업데이트
            jenkinsServer.updateJob(jobName, xml);
        } else {
            // Job이 없는 경우: 새로 생성
            jenkinsServer.createJob(jobName, xml);
        }
    }

    public void deleteJob(String jobName) throws IOException {
        jenkinsServer.deleteJob(jobName);
    }

    public void triggerJob(String jobName) throws IOException {
        Job job = jenkinsServer.getJob(jobName);
        if (job != null) {
            job.build();
        } else {
            throw new IOException("Job not found: " + jobName);
        }
    }

    public BuildWithDetails getLastBuildDetails(String jobName) throws IOException {
        JobWithDetails job = jenkinsServer.getJob(jobName);
        if (job != null) {
            return job.getLastBuild().details();
        } else {
            throw new IOException("Job not found: " + jobName);
        }
    }

    public String getBuildLog(String jobName, int buildNumber) throws IOException {
        JobWithDetails job = jenkinsServer.getJob(jobName);
        if (job != null) {
            return job.getBuildByNumber(buildNumber).details().getConsoleOutputText();
        } else {
            throw new IOException("Job not found: " + jobName);
        }
    }

    public String getJobXml(String jobName) throws IOException {
        return jenkinsServer.getJobXml(jobName);
    }

    public void updateJobXml(String jobName, String xml) throws IOException {
        jenkinsServer.updateJob(jobName, xml);
    }
}
