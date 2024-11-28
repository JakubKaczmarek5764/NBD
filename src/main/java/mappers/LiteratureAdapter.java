package mappers;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbConfig;
import jakarta.json.bind.adapter.JsonbAdapter;
import objects.Literature;

public class LiteratureAdapter implements JsonbAdapter<Literature, JsonObject> {
    private final Jsonb jsonb = JsonbBuilder.create(new JsonbConfig().withAdapters(new MongoUniqueIdAdapter()));

    @Override
    public JsonObject adaptToJson(Literature literature) {
        JsonObject json = jsonb.fromJson(jsonb.toJson(literature), JsonObject.class);

        return Json.createObjectBuilder(json)
                .add("_clazz", literature.getClass().getSimpleName())
                .build();
    }

    @Override
    public Literature adaptFromJson(JsonObject json) {
        String clazz = json.getString("_clazz");
        try {
            Class<?> type = Class.forName("objects." + clazz); // Replace with your package name
            Literature jsonObj = (Literature) jsonb.fromJson(json.toString(), type);
            return jsonObj;
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unknown class: " + clazz, e);
        }
    }
}
