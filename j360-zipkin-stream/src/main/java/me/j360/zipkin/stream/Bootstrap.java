package me.j360.zipkin.stream;


import okhttp3.OkHttpClient;
import zipkin.collector.CollectorComponent;
import zipkin.collector.InMemoryCollectorMetrics;
import zipkin.collector.kafka.KafkaCollector;
import zipkin.storage.StorageComponent;
import zipkin.storage.elasticsearch.http.ElasticsearchHttpStorage;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Package: me.j360.trace.example.consumer1
 * User: min_xu
 * Date: 2016/9/26 下午4:02
 * 说明：原生启动方式
 */
public class Bootstrap {
    public final static String testKafkaUrl = "123.59.27.205:9092";
    private static String kafkaServers = "qa-service:9092";
    private static String zkServers = "123.59.27.210:2181";
    private static Map<String, String> overrides = new HashMap<>();
    private static String zipkinName = "zipkin";

    public static void main(String args[]){
        overrides.put("enable.auto.commit", "true");
        overrides.put("auto.commit.interval.ms", "1000");
        overrides.put("session.timeout.ms", "30000");
        overrides.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        overrides.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        OkHttpClient client = new OkHttpClient.Builder().build();
        StorageComponent storageComponent = toBuilder(client).build();

        InMemoryCollectorMetrics metrics = new InMemoryCollectorMetrics();
        final CollectorComponent component = KafkaCollector.builder()
                .groupId("j360-zipkin-stream")
                .topic(zipkinName)
                .zookeeper(zkServers)
                //.bootstrapServers(kafkaServers)
                .overrides(overrides)
                .metrics(metrics)
                .storage(storageComponent)
                .streams(1)
                .build();
        component.start();
    }

    public static ElasticsearchHttpStorage.Builder toBuilder(OkHttpClient client) {
        List<String> hosts = Collections.singletonList("http://123.59.27.210:9200");

        ElasticsearchHttpStorage.Builder builder = ElasticsearchHttpStorage.builder(client);
        if (hosts != null) builder.hosts(hosts);
        return builder
                .index(zipkinName)
                .dateSeparator('-')
                //.pipeline(pipeline)
                .maxRequests(100)
                .indexShards(5)
                .indexReplicas(1);
    }

}
