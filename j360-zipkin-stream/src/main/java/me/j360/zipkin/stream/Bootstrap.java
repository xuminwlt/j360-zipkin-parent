package me.j360.zipkin.stream;


import zipkin.collector.CollectorComponent;
import zipkin.collector.InMemoryCollectorMetrics;
import zipkin.collector.kafka.KafkaCollector;
import zipkin.storage.elasticsearch.ElasticsearchStorage;

/**
 * Package: me.j360.trace.example.consumer1
 * User: min_xu
 * Date: 2016/9/26 下午4:02
 * 说明：原生启动方式
 */
public class Bootstrap {

    private static String zkHost = "172.16.10.125";
    private static String kafkaHost = zkHost;

    public static void main(String args[]){
        InMemoryCollectorMetrics metrics = new InMemoryCollectorMetrics();
        final CollectorComponent component = KafkaCollector.builder().zookeeper(zkHost+":2181").metrics(metrics).storage(ElasticsearchStorage.builder().build()).streams(1).build();
        component.start();
    }

}
