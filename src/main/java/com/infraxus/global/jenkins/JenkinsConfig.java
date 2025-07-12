package com.infraxus.global.jenkins;

import com.offbytwo.jenkins.JenkinsServer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class JenkinsConfig {
    @Value("${jenkins.url}")
    private String url;

    @Value("${jenkins.username}")
    private String username;

    @Value("${jenkins.token}")
    private String token;

//    @Bean
//    public JenkinsServer jenkinsServer() throws URISyntaxException {
//        return new JenkinsServer(new URI(url), username, token);
//    }
}
