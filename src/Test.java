/**
 * 测试类
 * @author 19750
 * @version 1.0
 */
public class Test {
    public static void main(String[] args) {
        // 创建数据对象工厂对象
        SimpleEventFactory<Integer> eventFactory = new SimpleEventFactory<>();
        // 定义环形数组的长度
        int ringBufferSize = 128;
        // 创建环形数组
        RingBuffer buffer = new RingBuffer(ringBufferSize,eventFactory);
        // 创建传输器对象
        EventTranslatorOneArg<Request<Integer>,Integer> eventTranslatorOneArg
                = new EventTranslatorOneArg<Request<Integer>, Integer>() {
            @Override
            public void translateTo(Request<Integer> request, long sequence, Integer arg0) {
                // 在这里将Integer对象设置到Request对象中
                request.setData(arg0);
            }
        };

        // 环形数组会发布128条数据，一旦发布了数据，消费者就能直接获取数据然后进行消费
        for (int i = 0; i < 128; i++) {
            buffer.publishEvent(eventTranslatorOneArg,i);
        }
    }
}
