package sectrans.manipulador.springboot.videoprocessor;


import org.springframework.context.annotation.Bean;
import sectrans.manipulador.springboot.filehandler.FileHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static sectrans.manipulador.springboot.videoprocessor.VideoHandler.getFutureFilesName;
import static sectrans.manipulador.springboot.videoprocessor.VideoHandler.cutVideo;

public class VideoProcess {
    Map<String,String> videoInformation;

    public VideoProcess(Path sourceFilePath, Path sourcePathToSave) throws IOException, ParseException {

        FileHandler fileHandler = new FileHandler();
        this.videoInformation = fileHandler.getFileInfo(sourceFilePath);

        //Não trata o vídeo enquanto não tenha finalizado o descarregamento.
        long fileSize = Files.size(sourceFilePath);
        while(fileSize <= 0){
            fileSize = Files.size(sourceFilePath);
        }

        String sourceVideoPath = videoInformation.get("path") + "\\" + videoInformation.get("file");
        List<HashMap<String, String>> listOfFutureFiles = getFutureFilesName(videoInformation);

        Path fullPathToSave = Paths.get(String.valueOf(sourcePathToSave), videoInformation.get("carro"),
                "camera"+videoInformation.get("camera"), videoInformation.get("data"));

        if (!Files.isDirectory(fullPathToSave)){
            Files.createDirectories(fullPathToSave);;
        }

        for (HashMap<String, String> fileInformation : listOfFutureFiles) {

            Path fileNamePath = Paths.get(String.valueOf(fullPathToSave), fileInformation.get("fileName"));
            File file = new File(fileNamePath.toUri());

            if(!file.exists()){
                cutVideo(sourceVideoPath, String.valueOf(fileNamePath),
                        fileInformation.get("startTime"), fileInformation.get("finalTime"));
            }

        }

        //ADICIONAR SOURCE VIDEO A FILA DE APAGAR

    }

}
