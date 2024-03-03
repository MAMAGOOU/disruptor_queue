/**
 * 数据对象工厂，eventFactory的实现类
 *
 * @author 19750
 * @version 1.0
 */
public class SimpleEventFactory<T> implements EventFactory<Request<T>> {

    @Override
    public Request<T> newInstance() {
        return new Request<>();
    }
}
