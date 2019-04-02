package com.djcps.library.controller;

import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.djcps.library.common.RetResponse;
import com.djcps.library.common.RetResult;
/**
 * @author djsxs
 *
 */
@RestControllerAdvice
public class ControllerExceptionHandleAdvice {
	
    public static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandleAdvice.class);
	
    @ExceptionHandler
    public RetResult<Object> handler(HttpServletRequest req, HttpServletResponse res, Exception e) {
        logger.info("Restful Http请求发生异常...");
        if (res.getStatus() == HttpStatus.BAD_REQUEST.value()) {
            logger.info("修改返回状态值为200");
            res.setStatus(HttpStatus.OK.value());
        }
        if (e instanceof NullPointerException) {
            logger.error("代码00：" + e.getMessage(), e);
            return RetResponse.makeRsp(500, "发生空指针异常");
        } else if (e instanceof IllegalArgumentException) {
            logger.error("代码01：" + e.getMessage(), e);
            return RetResponse.makeRsp(500, "请求参数类型不匹配");
        } else if (e instanceof SQLException) {
            logger.error("代码02：" + e.getMessage(), e);
            return RetResponse.makeRsp(500,"数据库访问异常");
        } else {
            logger.error("代码99：" + e.getMessage(), e);
            return RetResponse.makeRsp(500,"服务器代码发生异常,请联系管理员");
        }
    }
}
