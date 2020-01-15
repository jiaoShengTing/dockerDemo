package com.example.demo.provider;

import io.swagger.annotations.*;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Api(tags = "查询服务")
@RequestMapping("test")
public interface TestService {
    @ApiOperation(value = "查询各级单位上报情况", notes = "查询各级单位上报情况", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name="level",value="要查询到几级单位",dataType="Integer", paramType = "query"),
            @ApiImplicitParam(name="year",value="指定年份，不填则为查询全部",dataType="String", paramType = "query")
    })
    @RequestMapping("testQuery")
    Map<String, Map<String,String>> testQuery(Integer level,String year);
}
