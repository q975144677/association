package component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaginProto<T> extends Proto {
    private Page page;

    public static <T> PaginProto<T> ok(Proto<T> data, Page page) {
        if (data == null) {
            return fail();
        }
        T obj = data.getData();
        return new PaginProto<T>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), obj, new Page());

    }

    public static <T> PaginProto<T> ok(T data, Page page) {
        if (data == null) {
            return fail();
        }
        return new PaginProto<T>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), data, new Page());
    }

    public static PaginProto fail() {
        return new PaginProto(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(), null, new Page());
    }


    public PaginProto(Integer code, String info, Object data, Page page) {
        super(code, info, data);
        this.page = page;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    static class Page {
        Integer pageIndex = 1;
        Integer pageSize = 10;
        Integer pageCount = 1;
        Integer recordCount = 0;

        public Page(Integer pageCount, Integer recordCount) {
            this.pageCount = pageCount;
            this.recordCount = recordCount;
        }

        public static Page prepare(Integer pageIndex, Integer pageSize, Integer recordCount) {
            int pageCount = Math.max(1, (int) Math.ceil((double) recordCount / (double) pageSize));
            return new Page(pageIndex, pageSize, pageCount, recordCount);
        }
    }
}
