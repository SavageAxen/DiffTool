import com.fasterxml.jackson.databind.ObjectMapper;

final class PropertyAdded implements ChangeType {
    String property;
    String added;
    String removed;

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
        return "{\"property\": \"" + property + "\", \"added\": \"" + added + "\", \"removed\": \"" + removed
                + "\"}";
    }
}