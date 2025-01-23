package kafka;

import mappers.MongoUniqueId;
import objects.Borrowing;
import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.KafkaFuture;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.errors.TopicExistsException;
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

        try (Admin admin = Admin.create(properties)) {
            // Check if the topic already exists
            ListTopicsResult topics = admin.listTopics();
            if (topics.names().get().contains(topicName)) {
                System.out.println("Topic already exists: " + topicName);
                return;
            }

            NewTopic newTopic = new NewTopic(topicName, 3, (short) 3);
            CreateTopicsResult result = admin.createTopics(List.of(newTopic));
            result.all().get();

        } catch (ExecutionException e) {
            throw new RuntimeException("Failed to create topic: " + topicName, e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restore the interrupted status
            throw new RuntimeException("Interrupted while creating topic: " + topicName, e);
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
//        kafkaProducer.initTransactions();
    }
    public static void sendBorrowing(Borrowing borrowing) {
        initProducer();
        kafkaProducer.initTransactions();
        try {
            kafkaProducer.beginTransaction();
            JSONObject jsonBorrowing = borToJson(borrowing);
            ProducerRecord<UUID, String> record = new ProducerRecord<>(topicName, borrowing.getBorrowingId().getId(), jsonBorrowing.toString());
            Future<RecordMetadata> sent = kafkaProducer.send(record);
            RecordMetadata metadata = sent.get();
            System.out.println(metadata.toString());
            kafkaProducer.commitTransaction();
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        } catch (ProducerFencedException e) {
            kafkaProducer.close();
        } catch (KafkaException e) {
            kafkaProducer.abortTransaction();
        }
    }
    private static JSONObject borToJson(Borrowing borrowing) {
        JSONObject jsonBorrowing = new JSONObject();
        jsonBorrowing.put("borrowingId", borrowing.getBorrowingId());
        jsonBorrowing.put("beginDate", borrowing.getBeginDate().toInstant().toEpochMilli());
        if (borrowing.getEndDate() != null) {
            jsonBorrowing.put("endDate", borrowing.getEndDate());
        }
        jsonBorrowing.put("client", new JSONObject(borrowing.getClient()));
        JSONObject jsonLiterature = new JSONObject(borrowing.getLiterature());
        jsonLiterature.put("type", borrowing.getLiterature().getClass().getSimpleName());
        jsonBorrowing.put("literature", jsonLiterature);
        jsonBorrowing.put("shopName", "firstShop");
        return jsonBorrowing;
    }
}


