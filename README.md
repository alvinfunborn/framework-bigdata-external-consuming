#### 大数据量的扫库消费和外部统计框架
适用于由于数据量太大无法全部放在内存中计算的场景

##### usage
1. 实现Scroller
2. 实现Consumer

###### config
```$xslt
@Configuration
public class ExternalConsumingConfig {

    @Autowired
    private Consumer<Integer> consumer;
    @Autowired
    private Scroller<Integer> scroller;

    @Bean
    public ExternalConsumingTask<Integer> externalConsumingTask() {
        return new StandardExternalConsumingTask<>(200, scroller, consumer);
    }

    @Bean
    public ExternalConsumingTask<Integer> asynchronizedExternalConsumingTask() {
        return new StandardExternalConsumingTask<>(200, scroller, new AsynchronizedConsumer<>(consumer, 10, 15, 300));
    }
}
```

###### service
```$xslt
@Service
public class TestService {

    @Autowired
    private ExternalConsumingTask<Integer> externalConsumingTask;
    @Autowired
    private ExternalConsumingTask<Integer> asynchronizedExternalConsumingTask;

    public void start() {
        externalConsumingTask.start("0", 100, 50);
    }

    public void startAsynchronized() {
        asynchronizedExternalConsumingTask.start("0", 100, 50);
    }
}
```
