import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;


public class DiffTool {
    private static ObjectMapper mapper = new ObjectMapper();
    private static boolean prevId = false;
    private static boolean currId = false;

    public static List<ChangeType> diff(String previous, String current) throws Exception {

        JsonNode prevNode = mapper.readTree(previous);
        JsonNode currNode = mapper.readTree(current);

        return diff(prevNode, currNode, "");
    }

    private static List<ChangeType> diff(JsonNode prevNode, JsonNode currNode, String currentProperty) throws Exception {

        //As I dont have a data specification, I'll asume a JSON string
        List<ChangeType> changes = new ArrayList<>();
        Iterator<String> fieldNames = currNode.fieldNames();
        
        //Search for fields in the string
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            
            String newProperty = currentProperty.isEmpty() ? fieldName : currentProperty + "." + fieldName;
            if (prevNode.has(fieldName)) {

                JsonNode prevField = prevNode.get(fieldName);
                JsonNode currField = currNode.get(fieldName);

                //compare the properties
                if (prevField.isObject() && currField.isObject()) {

                    //recursive call with the current properties, as they have the same structure
                    changes.addAll(diff(prevField, currField, newProperty));
                    
                } else if (!prevField.equals(currField)) {

                    //Current fields are different so we add to changes.  
                    PropertyUpdate update = new PropertyUpdate();
                    //update.property = "Updated: " + newProperty;        //Uncomment to identify updated FieldNames
                    update.property = newProperty;
                    update.previous = prevField.asText();
                    update.current = currField.asText();
                    changes.add(update);
                }
            } else {
                
                // Handle new fields in the current object
                PropertyUpdate update = new PropertyUpdate();
                //update.property = "New field: " + newProperty;        //Uncomment to identify new FieldNames
                update.property = newProperty;
                update.previous = null;
                update.current = currNode.get(fieldName).asText();
                changes.add(update);
            }
        }
        // Handle fields removed in the current object
        fieldNames = prevNode.fieldNames();
        while (fieldNames.hasNext()) {
            String fieldName = fieldNames.next();
            if (!currNode.has(fieldName)) {
                PropertyUpdate update = new PropertyUpdate();

                //Uncomment to identify deleted FieldNames
                //update.property = currentProperty.isEmpty() ? "Created: " + fieldName : currentProperty + "." + fieldName;
                update.property = currentProperty.isEmpty() ? fieldName : currentProperty + "." + fieldName;
                update.previous = prevNode.get(fieldName).asText();
                update.current = null;
                changes.add(update);
            }
        }


        
        //As we don't know the structure of the data, and we cannot know if there will be a ID field at the beginning or at the end
        // of the data, I'll let the data be proccessed ant then check if there is an ID field.
        //Also is checked here to avoid conflits in using the 2 nodes and 2 iterators
        fieldNames = currNode.fieldNames();
        while (fieldNames.hasNext()){
            String fieldName = fieldNames.next();
            currId = fieldName == "id"? true: currId;
        }
        fieldNames = prevNode.fieldNames();
        while (fieldNames.hasNext()){
            String fieldName = fieldNames.next();
            prevId = fieldName == "id"? true: prevId;
        }


        try {
            if (!(currId && prevId)){
                throw new Exception("The system lacks the information to determine what has changed");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<>();
        };
        
        return changes;
    }
        
}
