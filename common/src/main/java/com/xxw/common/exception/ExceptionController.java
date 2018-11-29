package com.xxw.common.exception;

import com.xxw.common.web.ResponseResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

/**
 * @author xxw
 * @date 2018/8/11
 */
@Controller
@RequestMapping({"${server.error.path:${error.path:/error}}"})
public class ExceptionController implements ErrorController {

    @Autowired
    private ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping(produces = "text/html")
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
        Map<String, Object> model = Collections.unmodifiableMap(this.errorAttributes.getErrorAttributes(
                new ServletWebRequest(request), false));
        response.setCharacterEncoding(StandardCharsets.UTF_8.toString());
        return new ModelAndView("error", model);
    }

    @RequestMapping
    @ResponseBody
    public ResponseResult error(HttpServletRequest request, HttpServletResponse response) {
        response.setStatus(HttpStatus.OK.value());
        Throwable throwable = this.errorAttributes.getError(new ServletWebRequest(request));
        if (throwable instanceof BusinessException) {
            BusinessException businessException = (BusinessException) throwable;
            return ResponseResult.build(businessException.getCode(), businessException.getMessage());
        }
        else if (throwable instanceof MethodArgumentNotValidException) {
            String message = ((MethodArgumentNotValidException) throwable).getBindingResult().getFieldError()
                    .getDefaultMessage();
            return ResponseResult.fail(message);
        }
        return ResponseResult.fail(throwable.getMessage());
    }
}
