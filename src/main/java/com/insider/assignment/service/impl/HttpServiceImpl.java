package com.insider.assignment.service.impl;

import com.insider.assignment.service.HttpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class HttpServiceImpl implements HttpService {

    @Autowired
    private RestTemplate restTemplate;

    @Override
    public <T>T makeGetRequest(String api, Class<T> classType) {
        return this.restTemplate.getForObject(api, classType);
    }
}
