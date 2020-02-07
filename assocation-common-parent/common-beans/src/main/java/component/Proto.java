package component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Proto<T> {
    private Integer code = 200;
    private String info ;
    private T data;
    public static <T> Proto<T> ok(Proto<T> data){
        if(data == null || data.getData() == null){
            return fail();
        }
        T obj = data.getData();
        return new Proto<T>(HttpStatus.OK.value(),HttpStatus.OK.getReasonPhrase(),obj);
    }
    public static <T> Proto<T> ok(T data){
        Proto proto = null ;
        if(data == null){
            return fail();
        }
        proto = new Proto(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data);
        return proto;
    }
    public static <T> Proto<T> fail(){
        return new Proto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),null);
    }
    public static <T> Proto<T> fail(T t){
        return new Proto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),t);
    }

    public static Proto fail(String msg){
        return new Proto<>(HttpStatus.INTERNAL_SERVER_ERROR.value(),msg,null);
    }

 public boolean check(){
        return data != null && code.equals(HttpStatus.OK.value());
 }
}

