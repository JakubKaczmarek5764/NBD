package kafka;

import mappers.MongoUniqueId;
import objects.Borrowing;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.common.serialization.UUIDSerializer;
import org.json.JSONObject;

import java.util.List;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Producer {
    static KafkaProducer<UUID, String> kafkaProducer;
    static String topicName;
    public static void createTopic(String topic) {
        topicName = topic;
        Properties properties = new Properties();
        properties.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka2:9292, kafka3:9392");
        int partitionsNumber = 3;
        short replicationFactor = 3;
        try (Admin admin = Admin.create(properties)) {
            NewTopic newTopic = new NewTopic(topicName, partitionsNumber, (short) replicationFactor);
            CreateTopicsOptions options = new CreateTopicsOptions()
                    .timeoutMs(1000)
                    .validateOnly(false)
                    .retryOnQuotaViolation(true);
            CreateTopicsResult result = admin.createTopics(List.of(newTopic), options);
            KafkaFuture<Void> future = result.values().get(topicName);
            future.get();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
    public static void initProducer() {
        Properties properties = new Properties();
        properties.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka1:9192, kafka2:9292, kafka3:9392");
        properties.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, UUIDSerializer.class.getName());
        properties.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.put(ProducerConfig.CLIENT_ID_CONFIG, "local");
        properties.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "859676a3-08b8-4bd0-b82e-e6267d524ea5");

        kafkaProducer = new KafkaProducer<>(properties);
    }
    public static void sendBorrowing(Borrowing borrowing) {
        try {
            initProducer();
            JSONObject jsonBorrowing = borToJson(borrowing);
            ProducerRecord<UUID, String> record = new ProducerRecord<>(topicName, borrowing.getBorrowingId().getId(), jsonBorrowing.toString());
            Future<RecordMetadata> sent = kafkaProducer.send(record);
            RecordMetadata metadata = sent.get();
            System.out.println(metadata.toString());
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
    private static JSONObject borToJson(Borrowing borrowing) {
        JSONObject jsonBorrowing = new JSONObject();
        jsonBorrowing.put("borrowingId", borrowing.getBorrowingId());
        jsonBorrowing.put("beginDate", borrowing.getBeginDate().toInstant().toEpochMilli());
        if (borrowing.getEndDate() == null) {
            jsonBorrowing.put("endDate", 0);
        }
        jsonBorrowing.put("client", new JSONObject(borrowing.getClient()));
        JSONObject jsonLiterature = new JSONObject(borrowing.getLiterature());
        jsonLiterature.put("type", borrowing.getLiterature().getClass().getSimpleName());
        jsonBorrowing.put("literature", jsonLiterature);

        return jsonBorrowing;
    }
}


