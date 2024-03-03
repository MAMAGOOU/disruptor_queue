/**
 * 专门用来生产生产者数据的工厂
 * @author 19750
 * @param <T>
 */
public interface EventFactory <T>{
    T newInstance();
}
