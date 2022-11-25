package com.innople.devpleyground.dpfpapi.dto.example;

import lombok.Data;
import lombok.NoArgsConstructor;

public class ExampleDto {
    @Data
    @NoArgsConstructor
    public static class ExampleRequest {
        private Long id;
        private String uid;
        private String name;
        private Integer number;
        private String extraColumn01;
        private String extraColumn02;
        private String extraColumn03;
    }

    @Data
    @NoArgsConstructor
    public static class Response {
        private Long id;
        private String uid;
        private String name;
        private Integer number;
        private String extraColumn01;
        private String extraColumn02;
        private String extraColumn03;
    }
}
