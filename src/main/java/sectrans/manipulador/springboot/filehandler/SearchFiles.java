package sectrans.manipulador.springboot.filehandler;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SearchFiles {

    public List<Path> getFiles(Path directory) throws IOException {

        List<Path> pathsToProcess = new ArrayList<>();

        Files.walkFileTree(directory, Collections.emptySet(), 10, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                File f = new File(String.valueOf(file));
                if (f.isFile())
                    pathsToProcess.add(file);
                return FileVisitResult.CONTINUE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                System.err.println("Visit File Error: " + exc);
                return FileVisitResult.CONTINUE;
            }
        });

        return pathsToProcess;
    }

}
