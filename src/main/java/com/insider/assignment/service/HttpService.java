package com.insider.assignment.service;

public interface HttpService {
    <T>T makeGetRequest(String api, Class<T> classType);
}
