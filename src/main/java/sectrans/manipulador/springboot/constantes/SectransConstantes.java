package sectrans.manipulador.springboot.constantes;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class SectransConstantes {
    public static final List<String> VALID_EXTENSIONS = Arrays.asList("mp4","avi");
    public static final Path SOURCE_DIRECTORY = Paths.get("C:/VIDEO");
    public static final Path PATH_TO_SAVE = Paths.get("C:/Users/Douglas/Desktop/home/publico/imagens");
    public static final boolean REMOVE_FILES = false;
}
