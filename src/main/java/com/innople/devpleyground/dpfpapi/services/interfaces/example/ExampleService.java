package com.innople.devpleyground.dpfpapi.services.interfaces.example;

import com.innople.devpleyground.dpfpapi.domains.example.Example;

import java.util.List;

public interface ExampleService {
    public Example getOne(Long id) throws Exception;
    public List<Example> findAllByName(String name);
    public Example save(Example domain);
    public boolean deleteSampleDomain(Long id);
}
