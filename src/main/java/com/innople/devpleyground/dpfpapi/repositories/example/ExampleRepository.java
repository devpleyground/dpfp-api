package com.innople.devpleyground.dpfpapi.repositories.example;

import com.innople.devpleyground.dpfpapi.domains.example.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExampleRepository extends JpaRepository<Example, Long> {
    public List<Example> findAllByName(String name);
}
