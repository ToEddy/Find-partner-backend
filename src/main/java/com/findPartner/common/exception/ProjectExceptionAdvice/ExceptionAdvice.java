package com.findPartner.common.exception.ProjectExceptionAdvice;

import com.findPartner.common.exception.BusinessException;
import com.findPartner.common.exception.ErrorCode;
import com.findPartner.common.exception.SystemException;
import com.findPartner.domain.R.ResultData;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @RestControllerAdvice 是一个组合注解，由@ControllerAdvice、@ResponseBody组成，
 * 而@ControllerAdvice继承了@Component，因此@RestControllerAdvice本质上是个Component，
 * 用于定义@ExceptionHandler，@InitBinder和@ModelAttribute方法，适用于所有使用@RequestMapping方法。
 */

/**
 * 项目异常统一处理类 (aop)思想
 *
 * @author eddy
 * @createTime 2023/2/11
 */
@RestControllerAdvice
public class ExceptionAdvice {

    /**
     * 业务异常处理类
     *
     * @param businessEx
     * @return
     */
    @ExceptionHandler(BusinessException.class)
    public ResultData doBusinessException(BusinessException businessEx) {
        return new ResultData(businessEx.getCode(), businessEx.getMessage(), businessEx.getDescription());
    }

    /**
     * 系统异常处理类
     *
     * @param systemEx
     * @return
     */
    @ExceptionHandler(SystemException.class)
    public ResultData doSystemException(SystemException systemEx) {
        return new ResultData(systemEx.getCode(), systemEx.getMessage(), systemEx.getDescription());
    }

    /**
     * 非自定义异常的处理
     *
     * @param ex
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResultData doException(Exception ex) {
        return new ResultData(ErrorCode.SYSTEM_ERR_CODE);
    }
}
