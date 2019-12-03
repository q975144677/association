package component;

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

    public static <T> PaginProto<T> getPaginResult(PaginProto<T> data, PaginProto.Page page) {
        return PaginProto.ok(data, page);
    }

    public static <T> PaginProto<T> getPaginResult(T data, PaginProto.Page page) {
        return PaginProto.ok(data, page);
    }

    public static <T> PaginProto<T> paginFail() {
        return PaginProto.fail();
    }
}
