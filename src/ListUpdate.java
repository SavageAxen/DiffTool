import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;

final class ListUpdate implements ChangeType {
    String property;
    List<String> added;
    List<String> removed;

    public String toJson() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            return mapper.writeValueAsString(this);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String toString() {
        return "{\"property\": \"" + property + "\", \"added\": " + added.toString() + ", \"removed\": "
                + removed.toString() + "}";
    }
}