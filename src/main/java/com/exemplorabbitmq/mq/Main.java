package com.exemplorabbitmq.mq;

import com.exemplorabbitmq.mq.filehandler.SearchFiles;
import com.exemplorabbitmq.mq.videoprocessor.VideoProcess;
import com.exemplorabbitmq.mq.watcher.WatchDir;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException, ParseException {

//        // Constantes
        boolean recursive = true;

        List<String> validExtensions = Arrays.asList("mp4","avi");
        Path dir = Paths.get("C:\\VIDEO");
        Path sourcePathToSave = Paths.get("C:\\Users\\Douglas\\Desktop\\home\\publico\\imagens");

        //Busca por arquivos no diretorio base e retorna uma lista de Path
        SearchFiles searchFiles = new SearchFiles();
        List<Path> listFiles = searchFiles.getFiles(dir);

        //Itera sobre a lista e retorna os dados do arquivo.

        for (Path fileSource:listFiles) {

            //Valida a extensão
            if(validExtensions.contains(FilenameUtils.getExtension(String.valueOf(fileSource)))) {

                //REGISTRAR NA FILA PROCESSAR
                new VideoProcess(fileSource, sourcePathToSave);

            } else {

                System.out.println("Extensao invalida");
                //REGISTRAR NA FILA APAGAR
            }

        }

        //Inicia Watcher de arquivos do diretorio raiz.
        WatchDir watchDir = new WatchDir(dir, recursive, validExtensions, sourcePathToSave);
        watchDir.start();

        //---------------------------------------------------------------------------

//        CustomMessage customMessage = new CustomMessage();
//        customMessage.setMessage(arquivosExistentes.toString());

//        MessagePublisher messagePublisher = new MessagePublisher();
//        messagePublisher.publishMessage(customMessage);

//        SpringApplication.run(SpringRabbitmqProducerApplication.class, args);


    }
}
