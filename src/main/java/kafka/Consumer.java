package kafka;

import mappers.MongoUniqueId;
import objects.*;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.UUIDDeserializer;
import org.json.JSONObject;
import repositories.BorrowingRepository;
import repositories.BorrowingRepositoryConsumer;

import java.text.MessageFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Consumer {
    static String consumerGroupId = "group1";
    static String topicName;
    static List<KafkaConsumer<UUID, String>> consumerGroup;
    private static BorrowingRepositoryConsumer borrowingRepository = new BorrowingRepositoryConsumer();
    private static void initConsumerGroup() {
        consumerGroup = new ArrayList<>();
        Properties properties = new Properties();
        properties.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka2:9292, kafka3:9392");
        properties.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
        properties.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, UUIDDeserializer.class.getName());
        properties.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, "read_committed");
        for (int i = 0; i < 2; i++) {
            KafkaConsumer<UUID, String> consumer = new KafkaConsumer<>(properties);
            consumer.subscribe(List.of(topicName));
            consumerGroup.add(consumer);
        }
    }
    private static void consume(KafkaConsumer<UUID, String> consumer) {
        initConsumerGroup();
        try {
            consumer.poll(0);
            Set<TopicPartition> assignment = consumer.assignment();
            System.out.println(consumer.groupMetadata().groupId());
//            consumer.seekToBeginning(assignment);
            Duration timeout = Duration.of(100, ChronoUnit.MILLIS);
            MessageFormat format = new MessageFormat("Consumer {5} Topic {0} Partition {1} Offset {2, number, integer} Key {3} Value {4} siemanotoja");
            while (true) {
                ConsumerRecords<UUID, String> records = consumer.poll(timeout);
                for (ConsumerRecord<UUID, String> record : records) {
                    Borrowing borrowing = mapRecordToBorrowing(record);
                    borrowingRepository.create(borrowing);

                    System.out.println(format.format(new Object[]{record.topic(), record.partition(), record.offset(), record.key(), record.value(), consumer.groupMetadata().groupId()}));
                }
                consumer.commitSync();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void consumeTopicsByGroup(String name) throws InterruptedException {
        topicName = name;
        initConsumerGroup();
        ExecutorService executor = Executors.newFixedThreadPool(2);
        for (KafkaConsumer<UUID, String> consumer : consumerGroup) {
            executor.execute(() -> consume(consumer));
        }
        Thread.sleep(60000);
        for (KafkaConsumer<UUID, String> consumer : consumerGroup) {
            consumer.wakeup();
        }
        executor.shutdown();
    }
    private static Borrowing mapRecordToBorrowing(ConsumerRecord<UUID, String> record) {
        JSONObject json = new JSONObject(record.value());
        System.out.println(json);
        System.out.println("siemanotoborrowing");

        MongoUniqueId id = new MongoUniqueId(UUID.fromString(json.getString("borrowingId")));
        ZonedDateTime beginDate = Instant.ofEpochMilli(json.getLong("beginDate")).atZone(ZoneId.systemDefault());
        ZonedDateTime endDate = null;
        try {
            endDate = Instant.ofEpochMilli(json.getLong("endDate")).atZone(ZoneId.systemDefault());
        } catch (Exception ignored) {

        }
        Client client = mapJsonToClient(json.getJSONObject("client"));
        Literature literature = mapJsonToLiterature(json.getJSONObject("literature"));
        return new Borrowing(id, beginDate, endDate, client, literature);

    }

    private static Literature mapJsonToLiterature(JSONObject jsonLiterature) {
        MongoUniqueId id = new MongoUniqueId(UUID.fromString(jsonLiterature.getJSONObject("literatureId").getString("id")));
        System.out.println(jsonLiterature);
        System.out.println("siemanotoliteratura");

        if (jsonLiterature.getString("type").equals("Book")) {
            return new Book(id, jsonLiterature.getString("name"), jsonLiterature.getString("genre"), jsonLiterature.getString("author"), jsonLiterature.getInt("weight"), jsonLiterature.getInt("tier"), jsonLiterature.getInt("isBorrowed"));
        }
        else if (jsonLiterature.getString("type").equals("Magazine")) {
            return new Magazine(id, jsonLiterature.getString("name"), jsonLiterature.getString("issue"), jsonLiterature.getInt("weight"), jsonLiterature.getInt("isBorrowed"));
        }
        else {
            throw new IllegalArgumentException("Unknown literature type");
        }
    }

    private static Client mapJsonToClient(JSONObject jsonClient) {
        System.out.println(jsonClient);
        System.out.println("siemanotoklient");
        MongoUniqueId id = new MongoUniqueId(UUID.fromString(jsonClient.getJSONObject("clientId").getString("id")));
        return new Client(id, jsonClient.getString("firstName"), jsonClient.getString("lastName"), jsonClient.getString("personalID"), jsonClient.getInt("maxWeight"), jsonClient.getInt("currentWeight"));

    }
}
