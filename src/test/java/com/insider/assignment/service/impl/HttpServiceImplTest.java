package com.insider.assignment.service.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.List;

public class HttpServiceImplTest {

    @InjectMocks
    private HttpServiceImpl httpService = new HttpServiceImpl();

    @Mock
    private RestTemplate restTemplate;

    @Before
    public void setUp() {
        restTemplate = Mockito.mock(RestTemplate.class);
        ReflectionTestUtils.setField(httpService, "restTemplate", restTemplate);
    }

    @Test
    public void testMakeGetRequest() {
        httpService.makeGetRequest("http://localhost:8086/top-stories", List.class);
    }
}
