/**
 * @author 19750
 * @version 1.0
 */
public class Request<T> {
    private T data;

    public T getData() {
        return data;
    }

    public Request<T> setData(T data) {
        this.data = data;
        return this;
    }
}
