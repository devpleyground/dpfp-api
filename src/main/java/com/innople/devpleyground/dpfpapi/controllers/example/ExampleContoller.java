package com.innople.devpleyground.dpfpapi.controllers.example;



import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@Api(tags = "ExampleContoller")
@Tag(name = "ExampleContoller", description = "테스트를 위한 Sample APIs")
@RestController
@RequiredArgsConstructor
public class ExampleContoller {
    /**
     * 헬스체크
     * @author kim_jaewon
     * @return Date
     */
    @ApiOperation(value = "API가 호출되는지에 대해 테스트해보기 위함", response = String.class, tags = {"ExampleContoller"})
    @GetMapping(value = { "sample/check" })
    public String getHealth(){
        return (LocalDateTime.now()).toString();
    }
}
