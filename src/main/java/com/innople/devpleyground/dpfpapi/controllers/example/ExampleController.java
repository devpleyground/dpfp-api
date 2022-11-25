package com.innople.devpleyground.dpfpapi.controllers.example;

import com.innople.devpleyground.dpfpapi.common.constants.ErrorConstants.ErrorCode;
import com.innople.devpleyground.dpfpapi.common.exceptions.DpfpCustomException;
import com.innople.devpleyground.dpfpapi.common.utils.ObjectTransfer;
import com.innople.devpleyground.dpfpapi.domains.example.Example;
import com.innople.devpleyground.dpfpapi.dto.example.ExampleDto;
import com.innople.devpleyground.dpfpapi.services.interfaces.example.ExampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Api(tags = "ExampleController")
@Tag(name = "ExampleController", description = "테스트를 위한 Sample APIs")
@RestController
@RequiredArgsConstructor
public class ExampleController {
    @Autowired
    private ExampleService exampleService;

    /**
     * 헬스체크
     * @author kim_jaewon
     * @return String
     */
    @ApiOperation(value = "API가 호출되는지에 대해 테스트해보기 위함", response = String.class, tags = {"ExampleController"})
    @GetMapping(value = { "examples/check" })
    public String getHealth(){
        return (LocalDateTime.now()).toString();
    }

    /**
     * 에러메세지 체크
     * @author kim_jaewon
     * @return void
     */
    @ApiOperation(value = "Exception 발생 시 메세지 확인용 샘플", tags = {"ExampleController"})
    @GetMapping(value = "examples/errormsg")
    public void getErrorMessage() {
        throw new DpfpCustomException(ErrorCode.Invalid_Parameter);
    }

    /**
     * Example Domain 테이블에서 id(으)로 조회
     * @author kim_jaewon
     * @param id:Long
     * @return Example
     */
    @ApiOperation(value = "ID를 받아 받아 조회된 결과를 주는 샘플", tags = {"ExampleController"})
    @GetMapping(value = "examples/{id}")
    public Example getOne(@PathVariable Long id) throws Exception {
        return exampleService.getOne(id);
    }

    /**
     * Example Domain 테이블에서 name으로 조회
     * @author kim_jaewon
     * @param name:String
     * @return List<Example>
     */
    @ApiOperation(value = "name을 받아 조회된 결과를 주는 샘플 | usage : exmples?name=이름", response = List.class, tags = {"ExampleController"})
    @GetMapping(value = "examples")
    public List<Example> findAllByName(@RequestParam("name") String name) {
        return exampleService.findAllByName(name);
    }

    /**
     * Example Domain 테이블에 데이터를 저장 - Dto 사용
     * @author kim_jaewon
     * @param param:ExampleDto.ExampleRequest
     * * @return ExampleDto.Response
     */
    @ApiOperation(value = "DTO을 사용한 저장 샘플", response = ExampleDto.Response.class, tags = {"ExampleController"})
    @PostMapping(value = "examples/save")
    public ExampleDto.Response save(@RequestBody ExampleDto.ExampleRequest param) throws Exception {
        Example example = ObjectTransfer.toObject(param, Example.class);
        Example result = this.exampleService.save(example);
        return ObjectTransfer.toObject(result, ExampleDto.Response.class);
    }

    /**
     *  Example Domain 테이블에 데이터를 삭제
     * @author kim_jaewon
     * @param id
     * @return boolean
     */
    @ApiOperation(value = "실제로 데이터 행이 삭제되는 샘플", response = boolean.class, tags = {"ExampleController"})
    @DeleteMapping(value = "examples/{id}")
    public boolean delete(@PathVariable Long id) {
        try {
            exampleService.delete(id);
            return true;
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
            return false;
        }
    }

    /**
     *  Example Domain 테이블에 데이터를 삭제
     * @author kim_jaewon
     * @param id
     * @return boolean
     */
    @ApiOperation(value = "삭제2, 사용여부 컬럼의 값을 false로 업데이트", response = boolean.class, tags = {"ExampleController"})
    @PutMapping(value = "examples/{id}/delete")
    public boolean changeDeleteFlag(@PathVariable Long id) {
        try {
            Example example = exampleService.getOne(id);
            example.setUsed(true);
            exampleService.save(example);
            return true;
        } catch (Exception ex) {
            System.out.printf(ex.getMessage());
            return false;
        }
    }
}
