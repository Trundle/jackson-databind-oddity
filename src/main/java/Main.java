import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Map;

/**
 * Demonstrates an oddity / potential bug in Jackson 2.9.7: deserializing a value will call the annotated creator, but
 * deserializing it as a map key won't call the annotated creator. Output will be:
 *
 * <pre>
 * Creator was called
 * Constructor was called
 * ------
 * Constructor was called
 * </pre>
 */
public class Main {

    static class Key {

        private final String value;

        private Key(final String value) {
            this.value = value;
            System.out.println("Constructor was called");
        }

        @JsonCreator(mode = JsonCreator.Mode.DELEGATING)
        public static Key of(final String value) {
            System.out.println("Creator was called");
            return new Key(value);
        }
    }

    public static void main(final String... args) throws IOException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.readValue("\"some key\"", Key.class);
        System.out.println("------");
        mapper.readValue("{\"some key\": 42}", new TypeReference<Map<Key, Integer>>() {});
    }
}
