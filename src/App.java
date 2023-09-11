import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

        //Here I will take a file that contains a JSON Object, because there is no additiona especification
        //in the Code challenge provided, I created somewhat simple objects
        //As I dont have a object data specification, I'll just mark the datas as "added" or "deleted"
        //Added file5 to demostrate a different object



public class App {
    public static void main(String[] args) throws Exception {

        File file1 =new File("object1.json");
        File file2 =new File("object2.json");
        File file3 =new File("object3.json");
        File file4 =new File("object4.json");
        //File file5 =new File("object2.json");

        String filePath1 = "src/object1.json";
        String content1 = new String(Files.readAllBytes(Paths.get(filePath1)));
        String filePath2 = "src/object2.json";
        String content2 = new String(Files.readAllBytes(Paths.get(filePath2)));

        String filePath3 = "src/object3.json";
        String content3 = new String(Files.readAllBytes(Paths.get(filePath3)));
        String filePath4 = "src/object4.json";
        String content4 = new String(Files.readAllBytes(Paths.get(filePath4)));

        List<ChangeType> result = DiffTool.diff(content1, content2);    //Fail because object1 dont have an id
        System.out.print("\nResult: " + result.toString());
        List<ChangeType> result1 =DiffTool.diff(content2, content4);    //Shoud retur the list of differences and updates
        System.out.print("\nResult1: " + result1.toString());
        List<ChangeType> result2 =DiffTool.diff(content1, content3);    //Fail because object1 and object 3 don't have an id
        System.out.print("\nResult2: " +result2.toString());

    }
}