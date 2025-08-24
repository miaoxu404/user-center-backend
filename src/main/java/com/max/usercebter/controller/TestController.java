package com.max.usercebter.controller;

import com.max.usercebter.common.BaseResponse;
import com.max.usercebter.common.ErrorCode;
import com.max.usercebter.exception.BusinessException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/business-exception")
    public BaseResponse testBusinessException(@RequestParam(defaultValue = "false") boolean trigger) {
        if (trigger) {
            throw new BusinessException(ErrorCode.PRAMS_ERROR, "测试业务异常");
        }
        return BaseResponse.success("正常响应");
    }

    @GetMapping("/runtime-exception")
    public BaseResponse testRuntimeException(@RequestParam(defaultValue = "false") boolean trigger) {
        if (trigger) {
            throw new RuntimeException("测试运行时异常");
        }
        return BaseResponse.success("正常响应");
    }

    @GetMapping("/null-pointer")
    public BaseResponse testNullPointerException(@RequestParam(defaultValue = "false") boolean trigger) {
        if (trigger) {
            String str = null;
            return BaseResponse.success(str.length()); // 这会抛出NullPointerException
        }
        return BaseResponse.success("正常响应");
    }
}
