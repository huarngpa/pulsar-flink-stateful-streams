package io.ipolyzos.compute.v1;

import io.ipolyzos.config.AppConfig;
import io.ipolyzos.models.*;
import io.ipolyzos.utils.EnvironmentUtils;
import java.time.Duration;
import org.apache.flink.api.common.eventtime.SerializableTimestampAssigner;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.connector.pulsar.source.PulsarSource;
import org.apache.flink.connector.pulsar.source.enumerator.cursor.StartCursor;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.pulsar.client.api.SubscriptionType;

public class EnrichmentStream {
  public static void main(String[] args) throws Exception {
    // 1. Initialize the execution environment
    StreamExecutionEnvironment env = EnvironmentUtils.initEnvWithWebUI(false);

    // 2. Initialize Sources
    PulsarSource<User> userSource =
        EnvironmentUtils.initPulsarSource(
            AppConfig.USERS_TOPIC,
            "flink-user-consumer",
            SubscriptionType.Exclusive,
            StartCursor.earliest(),
            User.class);

    PulsarSource<Item> itemSource =
        EnvironmentUtils.initPulsarSource(
            AppConfig.ITEMS_TOPIC,
            "flink-items-consumer",
            SubscriptionType.Exclusive,
            StartCursor.earliest(),
            Item.class);

    PulsarSource<Order> orderSource =
        EnvironmentUtils.initPulsarSource(
            AppConfig.ORDERS_TOPIC,
            "flink-orders-consumer",
            SubscriptionType.Exclusive,
            StartCursor.latest(),
            Order.class);

    WatermarkStrategy<Order> watermarkStrategy =
        WatermarkStrategy.<Order>forBoundedOutOfOrderness(Duration.ofSeconds(5))
            .withTimestampAssigner(
                (SerializableTimestampAssigner<Order>) (order, l) -> order.getCreatedAt());

    // 3. Initialize Streams
    DataStream<User> userStream =
        env.fromSource(userSource, WatermarkStrategy.noWatermarks(), "Pulsar User Source")
            .name("pulsarUserSource")
            .uid("pulsarUserSource");

    DataStream<Item> itemStream =
        env.fromSource(itemSource, WatermarkStrategy.noWatermarks(), "Pulsar Items Source")
            .name("pulsarItemSource")
            .uid("pulsarItemSource");

    DataStream<Order> orderStream =
        env.fromSource(orderSource, watermarkStrategy, "Pulsar Orders Source")
            .name("pulsarOrderSource")
            .uid("pulsarOrderSource");

    orderStream.print().uid("print").name("print");
    env.execute("Order Enrichment Stream");
  }
}
