package com.exemplorabbitmq.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException {
//
        // Constantes
        boolean recursive = true;

        Path dir = Paths.get("/home/publico/imagens");


        //Busca por arquivos no diretorio base e retorna uma lista de Path
        SearchFiles searchFiles = new SearchFiles();
        List<Path> arquivosExistentes = searchFiles.getFiles(dir);

        //Para cada arquivo existente na lista, retorna os dados do arquivo.
        FileHandler fileHandler = new FileHandler();

        for (Path fileSource:arquivosExistentes) {

            Map<String, String> fileInfo = fileHandler.getFileInfo(fileSource);
            System.out.println(fileInfo.get("carro"));
            System.out.println(fileInfo);
            //REGISTRAR NA FILA

        }

        //Inicia Watcher de arquivos do diretorio raiz.
        WatchDir watchDir = new WatchDir(dir, recursive);
        watchDir.start();


        //---------------------------------------------------------------------------
//        CustomMessage customMessage = new CustomMessage();
//        customMessage.setMessage(arquivosExistentes.toString());

//        MessagePublisher messagePublisher = new MessagePublisher();
//        messagePublisher.publishMessage(customMessage);

//        SpringApplication.run(SpringRabbitmqProducerApplication.class, args);


    }
}
