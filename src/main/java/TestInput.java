import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestInput {

    public static void main(String[] args) throws Exception {

        // Get input data from file
        Path inputFile = getInputPath(args);
        Path outputFile = getOutputPath(inputFile);
        // Scanner scanner = new Scanner(file);
        print(inputFile);
        print(outputFile);
        List<Path> filesInFolder = getFiles();
        for (Path file : filesInFolder) {
            System.out.println(file);
        }
    }

    public static List<Path> getFiles() {
        Path inputPath = Paths.get("input/se2007_task7_tokens");
        List<Path> filesInFolder = new ArrayList<>();
        try {
			// Files.walk(inputPath)
            //      .filter(Files::isRegularFile)
            //      .forEach(System.out::println);
            filesInFolder = Files.walk(inputPath)
                                 .filter(Files::isRegularFile)
                                 .collect(Collectors.toList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
        }
        return filesInFolder;
    }

    public static void print(Path file) throws Exception {
        System.out.println(file.getFileName().toString());
        System.out.println(file.toString());
        // System.out.println(file.getParent().getFileName().toString());
    }

    public static Path getInputPath(String[] args) {
        String fileName = "se2007_task7_tokens_1.tsv";
        if (args.length != 0) {
            fileName = args[0];
        }
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path file = currentPath.resolve("input")
                               .resolve(fileName);
        return file;
    }

    public static Path getOutputPath(Path inputFile) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        String outputFileName = "out_" + inputFile.getFileName().toString();
        Path outputPath = currentPath.resolve("output")
                                     .resolve(inputFile.getParent().getFileName());
        Path file = outputPath.resolve(outputFileName);
        // Create outputPath if does not exist
        if (Files.notExists(outputPath)) {
            try {
                Files.createDirectory(outputPath);
            } catch (java.io.IOException e) {
                System.out.println("createDirectory failed:" + e);
            }
        }
        return file;
    }
}
