package com.exemplorabbitmq.mq;

import com.exemplorabbitmq.mq.videoprocessor.VideoProcess;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException {
//
        // Constantes
        boolean recursive = true;

        List<String> validExtensions = Arrays.asList("mp4","avi");
        Path dir = Paths.get("/home/publico/imagens");


        //Busca por arquivos no diretorio base e retorna uma lista de Path
        SearchFiles searchFiles = new SearchFiles();
        List<Path> listFiles = searchFiles.getFiles(dir);

        //Itera sobre a lista e retorna os dados do arquivo.
        FileHandler fileHandler = new FileHandler();

        for (Path fileSource:listFiles) {

            Map<String, String> fileInfo = fileHandler.getFileInfo(fileSource);

            //Valida a extensão
            if(validExtensions.contains(fileInfo.get("extensao"))){
                System.out.println("Extensao valida");
                //REGISTRAR NA FILA PROCESSAR

                VideoProcess videoProcess = new VideoProcess(fileInfo);
                videoProcess.Teste();

            } else {
                System.out.println("Extensao invalida");
                //REGISTRAR NA FILA APAGAR
            }

        }

        //Inicia Watcher de arquivos do diretorio raiz.
//        WatchDir watchDir = new WatchDir(dir, recursive);
//        watchDir.start();


        //---------------------------------------------------------------------------
//        CustomMessage customMessage = new CustomMessage();
//        customMessage.setMessage(arquivosExistentes.toString());

//        MessagePublisher messagePublisher = new MessagePublisher();
//        messagePublisher.publishMessage(customMessage);

//        SpringApplication.run(SpringRabbitmqProducerApplication.class, args);


    }
}
