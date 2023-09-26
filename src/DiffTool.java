import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.JsonNode;

public class DiffTool {
    private static ObjectMapper mapper = new ObjectMapper();

    private static boolean prevHasId = false;
    private static boolean currHasId = false;

    public static List<ChangeType> diff(String previous, String current, boolean lookForId) throws Exception {

        JsonNode prevNode = mapper.readTree(previous);
        JsonNode currNode = mapper.readTree(current);
        prevHasId = false;
        currHasId = false;
        return diff(prevNode, currNode, "", lookForId);
    }

    private static List<ChangeType> diff(JsonNode prevNode, JsonNode currNode, String currentProperty,
            boolean lookForId)
            throws Exception {

        // As I dont have a data specification, I'll asume a JSON string
        List<ChangeType> changes = new ArrayList<>();
        Iterator<String> fieldNames = currNode.fieldNames();

        // Search for fields in the string
        while (fieldNames.hasNext()) {

            String fieldName = fieldNames.next();
            currHasId = fieldName == "id" ? true : currHasId;

            String newProperty = currentProperty.isEmpty() ? fieldName : currentProperty + "." + fieldName;
            if (prevNode.has(fieldName)) {
                prevHasId = fieldName == "id" ? true : prevHasId;
                JsonNode prevField = prevNode.get(fieldName);
                JsonNode currField = currNode.get(fieldName);
                // compare the properties
                if (prevField.isObject() && currField.isObject()) {

                    // recursive call with the current properties, as they have the same structure
                    changes.addAll(diff(prevField, currField, newProperty, true));

                } else if (prevField.isArray() && currField.isArray()) {

                    try {
                        ArrayList<String> change = MyListComparator.compareJsonArrays(prevField.toString(),
                                currField.toString());
                        // Current fields are lists, so we add the differences if any
                        PropertyAdded update = new PropertyAdded();
                        update.property = newProperty;
                        update.added = change.get(0);
                        update.removed = change.get(1);
                        changes.add(update);
                    } catch (Exception e) {

                        ObjectMapper localMapper = new ObjectMapper();
                        JsonNode rootNode = localMapper.readTree(prevNode.toString());
                        JsonNode prevObject = rootNode.get(fieldName);
                        ObjectNode previousNode = localMapper.createObjectNode();
                        previousNode.set(fieldName, prevObject);

                        rootNode = localMapper.readTree(currNode.toString());
                        JsonNode currObject = rootNode.get(fieldName);
                        ObjectNode currentNode = localMapper.createObjectNode();
                        currentNode.set(fieldName, currObject);

                        changes.addAll(diff(previousNode.toString(), currentNode.toString(), false));
                    }

                } else if (!prevField.equals(currField)) {

                    // Current fields are different so we add to changes.
                    PropertyUpdate update = new PropertyUpdate();
                    // update.property = "Updated: " + newProperty; //Uncomment to identify updated
                    update.property = newProperty;
                    update.previous = prevField.asText();
                    update.current = currField.asText();
                    changes.add(update);
                }
            } else {

                // Handle new fields in the current object
                PropertyAdded added = new PropertyAdded();
                // added.property = "New field: " + newProperty; //Uncomment to identify new
                // FieldNames
                added.property = newProperty;
                added.added = null;
                added.removed = currNode.get(fieldName).asText();
                changes.add(added);
            }
        }
        // Handle fields removed in the current object
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            if (!currNode.has(fieldName)) {
                PropertyUpdate update = new PropertyUpdate();

                // Uncomment to identify deleted FieldNames
                // update.property = currentProperty.isEmpty() ? "Created: " + fieldName :
                // currentProperty + "." + fieldName;
                update.property = currentProperty.isEmpty() ? fieldName : currentProperty + "." + fieldName;
                update.previous = prevNode.get(fieldName).asText();
                update.current = null;
                changes.add(update);
            }
        }

        // As we don't know the structure of the data, and we cannot know if there will
        // be a ID field at the beginning or at the end
        // of the data, I'll let the data be proccessed ann then check if there is an ID
        // field.

        if ((currHasId && prevHasId) && lookForId) {
        return changes;
        };
        throw new Exception("The system lacks the information to determine what has changed");
    }
}
