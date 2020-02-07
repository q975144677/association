package component;

import org.springframework.http.HttpStatus;

public class HttpRequestException extends RuntimeException {

    private Integer code;
    public HttpRequestException(String message, Integer code) {
        super(message);
        this.code = code;
    }
    public static HttpRequestException defaultParamErrorException(){
        return new HttpRequestException(PARAM_ERROR, HttpStatus.OK.value());
    }
    public static final String PARAM_ERROR = "参数错误";
    public static final String USER_INFO_ERROR = "获取用户错误";
    public static final String ROLE_ERROR = "获取用户权限异常" ;
}
