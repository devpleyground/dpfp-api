package com.innople.devpleyground.dpfpapi.services.example;

import com.innople.devpleyground.dpfpapi.domains.example.Example;
import com.innople.devpleyground.dpfpapi.repositories.example.ExampleRepository;
import com.innople.devpleyground.dpfpapi.services.interfaces.example.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ExampleServiceImpl implements ExampleService {
    @Autowired
    private ExampleRepository exampleRepository;

    @Override
    public Example getOne(Long id) throws Exception {
        return exampleRepository.findById(id).orElseThrow(() -> new Exception());
    }

    @Override
    public List<Example> findAllByName(String name) {
        return exampleRepository.findAllByName(name);
    }

    @Override
    public Example save(Example domain) {
        return exampleRepository.save(domain);
    }

    @Override
    public boolean deleteSampleDomain(Long id) {
        try {
            exampleRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
