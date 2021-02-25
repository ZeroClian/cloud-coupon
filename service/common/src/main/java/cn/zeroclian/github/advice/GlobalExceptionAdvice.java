package cn.zeroclian.github.advice;

import cn.zeroclian.github.exception.CouponException;
import cn.zeroclian.github.vo.CommonResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Desciption 全局异常处理
 * @Author ZeroClian
 * @Date 2021-02-25-22:43
 */
@RestControllerAdvice
public class GlobalExceptionAdvice {

    /**
     * 对 CouponException 进行统一处理
     * @param req
     * @param ex
     * @return
     */
    @ExceptionHandler(value = CouponException.class)
    public CommonResponse<String> handleCouponException(HttpServletRequest req, CouponException ex) {
        CommonResponse<String> response = new CommonResponse<>(-1, "business error!");
        response.setData(ex.getMessage());
        return response;
    }
}
