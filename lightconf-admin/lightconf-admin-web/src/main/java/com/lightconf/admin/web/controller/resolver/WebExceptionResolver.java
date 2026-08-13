package com.lightconf.admin.web.controller.resolver;

import com.lightconf.common.model.Messages;
import com.lightconf.common.util.LightConfResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器：统一返回 LightConfResult，避免将内部异常细节泄漏给前端。
 *
 * @author whfstudio
 */
@Slf4j
@RestControllerAdvice
public class WebExceptionResolver {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public LightConfResult resolveException(Exception ex) {
        log.error(">>> controller exception", ex);
        return LightConfResult.build(Messages.SERVER_ERROR_CODE, Messages.SERVER_ERROR_MSG);
    }
}
