package sectrans.manipulador.springboot;

import org.apache.commons.io.FilenameUtils;
import sectrans.manipulador.springboot.filehandler.SearchFiles;
import sectrans.manipulador.springboot.videoprocessor.VideoProcess;
import sectrans.manipulador.springboot.watcher.WatchDir;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, ParseException {

        // Constantes (Irão para arquivo de configuração!)
        List<String> validExtensions = Arrays.asList("mp4","avi");
        Path sourceDirectory = Paths.get("C:\\VIDEO");
        Path pathToSave = Paths.get("C:\\Users\\Douglas\\Desktop\\home\\publico\\imagens");

        //Módulo1
        //Busca por arquivos no diretorio base e retorna uma lista de Path
        SearchFiles searchFiles = new SearchFiles();
        List<Path> listExistingFiles = searchFiles.getFiles(sourceDirectory);

        //Itera sobre a lista e retorna os dados do arquivo.

        for (Path existingFile:listExistingFiles) {

            //Valida a extensão
            if(validExtensions.contains(FilenameUtils.getExtension(String.valueOf(existingFile)))) {

                //REGISTRAR NA FILA PROCESSAR
                new VideoProcess(existingFile, pathToSave);

            } else {
                //REGISTRAR NA FILA APAGAR
                System.out.println("Extensao invalida");
            }
        }

        //Inicia Watcher de arquivos do diretorio raiz.
        WatchDir watchDir = new WatchDir(sourceDirectory, validExtensions, pathToSave);
        watchDir.start();

    }
}