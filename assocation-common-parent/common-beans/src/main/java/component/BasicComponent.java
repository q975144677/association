package component;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

public class BasicComponent {

    public static <T> Proto<T> getResult(Proto<T> data) {
        return Proto.ok(data);
    }

    public static <T> Proto<T> getResult(T data) {
        return Proto.ok(data);
    }

    public static <T> Proto<T> fail() {
        return Proto.fail();
    }

    public static <T> Proto<T> fail(String msg) {
        return Proto.fail(msg);
    }
    public static <T> PaginProto<T> getPaginResult(PaginProto<T> data, PaginProto.Page page) {
        return PaginProto.ok(data, page);
    }

    public static <T> PaginProto<T> getPaginResult(T data, PaginProto.Page page) {
        return PaginProto.ok(data, page);
    }

    public static <T> PaginProto<T> paginFail() {
        return PaginProto.fail();
    }

    public static RuntimeException throwDefaultHttpRequestException(){
        throw new HttpRequestException("PARAM ERROR" , HttpStatus.OK.value());
    }

    public Operator getOperator(HttpServletRequest request){
        return (Operator)request.getAttribute("operator");
    }
    //操作人
    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Operator{
        String guid;
        String name ;
        String roleGuid ;
        Integer schoolId ;
        String username ;
        String password;
        String mobilePhone ;
        Date createTime ;
        Date updateTime ;
        List<String> associations;
        String avatar;
        String schoolName ;
    }
}
