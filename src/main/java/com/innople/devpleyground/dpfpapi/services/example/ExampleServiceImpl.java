package com.innople.devpleyground.dpfpapi.services.example;

import com.innople.devpleyground.dpfpapi.common.constants.ErrorConstants;
import com.innople.devpleyground.dpfpapi.common.exceptions.DpfpCustomException;
import com.innople.devpleyground.dpfpapi.domains.example.Example;
import com.innople.devpleyground.dpfpapi.repositories.example.ExampleRepository;
import com.innople.devpleyground.dpfpapi.services.interfaces.example.ExampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(isolation = Isolation.READ_UNCOMMITTED)
public class ExampleServiceImpl implements ExampleService {
    @Autowired
    private ExampleRepository exampleRepository;

    @Override
    public Example getOne(Long id) throws Exception {
        return exampleRepository.findById(id).orElseThrow(() -> new DpfpCustomException(ErrorConstants.ErrorCode.Invalid_KeyOrID));
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
    public boolean delete(Long id) {
        try {
            exampleRepository.deleteById(id);
            return true;
        } catch (Exception ex) {
            throw ex;
        }
    }
}
