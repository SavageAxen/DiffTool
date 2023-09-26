import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class MyListComparator {
        public static ArrayList<String> compareJsonArrays(String json1, String json2) throws Exception {

                // Create ObjectMapper instance
                ObjectMapper mapper = new ObjectMapper();
                List<String> list1;
                List<String> list2;
                List<String> baseListCopy;
                List<String> newListCopy;

                ArrayList<String> result = new ArrayList<>();

                // Handle null strings
                if (json1 == null || json1.isBlank()) {
                        list2 = mapper.readValue(json2, new TypeReference<List<String>>() {
                        });
                        baseListCopy = new ArrayList<>(list2);
                        result.add(baseListCopy.toString());
                        return result;
                }
                if (json2 == null || json2.isBlank()) {
                        list1 = mapper.readValue(json1, new TypeReference<List<String>>() {
                        });
                        newListCopy = new ArrayList<>(list1);
                        result.add(newListCopy.toString());
                        return result;
                }

                // Convert JSON string to List
                list1 = mapper.readValue(json1, new TypeReference<List<String>>() {});
                list2 = mapper.readValue(json2, new TypeReference<List<String>>() {});
                

                // Create copies of the lists for manipulation
                baseListCopy = new ArrayList<>(list1);
                newListCopy = new ArrayList<>(list2);

                // Remove all common elements from the copies
                baseListCopy.removeAll(list2);
                newListCopy.removeAll(list1);

                // Add the deleted and added items to an ArrayList
                result.add(newListCopy.toString());
                result.add(baseListCopy.toString());

                return result;
        }
}
