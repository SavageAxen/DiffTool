import com.fasterxml.jackson.databind.ObjectMapper;

final class PropertyUpdate implements ChangeType {
    String property;
    String previous;
    String current;

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
        return "{\"property\": \"" + property + "\", \"previous\": \"" + previous + "\", \"current\": \"" + current
                + "\"}";
    }
}