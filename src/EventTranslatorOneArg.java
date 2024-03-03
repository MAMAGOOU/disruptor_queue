/**
 * 传输器，将生产者发布的真正数据，传输到对应的对象中
 * @author 19750
 * @param <T>
 * @param <A>
 */
public interface EventTranslatorOneArg<T,A> {
    void translateTo(T event,long sequence,A arg0);
}
