# DiffTool

## Audit System Core sample application

The DiffTool class works by comparing the fields of an JSON Object, the current and previous versions of an objects using reflection. If a field is a List, the method toMap is used to convert the list into a map where the keys are the values of the field. The method then compares the keys of the maps to determine which items were added or removed from the list. If an item is present in both lists, the method calls itself recursively to compare the fields of the inner object.

If a field is not a List, the method simply compares the values of the field in the previous and current objects.  
If the values are not equal, a `UpdatedProperty` object is created to track the change.  
The method returns a list of `ChangeType` objects that represent all of the changes that were detected between the previous and current objects.  
The `ChangeType` interface has two implementations: `UpdatedProperty` and `ListUpdate`. `UpdatedProperty` create the updated info and the is put into the `ListUpdate`.

## How to run project
I pick Jackson to make the deserialization for the recibed objects so, to run this project you need Jakson jar files to be referenced
[jackson-core-2.9.9.jar](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-core/2.9.9/jackson-core-2.9.9.jar) and [jackson-databind-2.9.9.3.jar](https://repo1.maven.org/maven2/com/fasterxml/jackson/core/jackson-databind/2.9.9.3/jackson-databind-2.9.9.3.jar) or later

Only download the files, add the jar dependencies and run it on VSCode or Eclypse.
It is a very simple project so, no need for build tools like Maven or Graddle.
I added some "simple" JSON Objects files to test it. This projects compare one JSON Object to other JSON object, so if you try to run it, each JSON file must contain *ONLY ONE* object.


## How I put it together

I took me arround 16 to 20 hours of work, mainly because I try to put together a class and try to fit the data to it.

That was no the correct solution, so I have to go back and start again. It was also fun because sometimes I got scambled in the recursive functions.

Also, never before I needed to deserialize a JSON Object and work with the data directly, usually JSON Objects are deserialized in to data class objects.

Learned a bit about Jackson







