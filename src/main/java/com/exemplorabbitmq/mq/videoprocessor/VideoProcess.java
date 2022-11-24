package com.exemplorabbitmq.mq.videoprocessor;


import com.exemplorabbitmq.mq.filehandler.FileHandler;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.exemplorabbitmq.mq.videoprocessor.VideoHandler.getFutureFilesName;
import static com.exemplorabbitmq.mq.videoprocessor.VideoHandler.cutVideo;
import com.exemplorabbitmq.mq.filehandler.FileHandler;

public class VideoProcess {
    Map<String,String> videoInformation;

    public VideoProcess(Path filePath, Path sourcePathToSave) throws IOException, ParseException {

        FileHandler fileHandler = new FileHandler();

        long fileSize = Files.size(filePath);
        while(fileSize <= 0){
            System.out.println("Menor " + fileSize);
            fileSize = Files.size(filePath);
        }
        System.out.println("Maior " + fileSize);

        this.videoInformation = fileHandler.getFileInfo(filePath);

        String videoPath = videoInformation.get("path") + "\\" + videoInformation.get("file");
        List<HashMap<String, String>> listOfFutureFiles = getFutureFilesName(videoInformation);

        Path fullPathToSave = Paths.get(String.valueOf(sourcePathToSave), videoInformation.get("carro"),
                "camera"+videoInformation.get("camera"), videoInformation.get("data"));

        if (!Files.isDirectory(fullPathToSave)){
            Files.createDirectories(fullPathToSave);;
        }

        for (HashMap<String, String> fileInformation : listOfFutureFiles) {

            Path fileNamePath = Paths.get(String.valueOf(fullPathToSave), fileInformation.get("fileName"));

            cutVideo(videoPath, String.valueOf(fileNamePath),
                    fileInformation.get("startTime"), fileInformation.get("finalTime"));

        }

        //ADICIONAR VIDEO A FILA DE APAGAR

    }

}
