package sectrans.manipulador.springboot.videoprocessor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sectrans.manipulador.springboot.dto.VideoDto;
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

@Component
@Service
@Slf4j
public class VideoProcess {

    public static void process(String sourceFilePath, String sourcePathToSave) throws IOException, ParseException {

        FileHandler fileHandler = new FileHandler();
//        Map<String,String> videoInformation = fileHandler.getFileInfo(Path.of(sourceFilePath));
        VideoDto videoInformation = fileHandler.getFileInfo(Path.of(sourceFilePath));

        String sourceVideoPath = String.format("%s%s%s",videoInformation.path, "/", videoInformation.file);
        List<HashMap<String, String>> listOfFutureFiles = getFutureFilesName(videoInformation);

        Path fullPathToSave = Paths.get(String.valueOf(sourcePathToSave), videoInformation.car,
                "camera"+videoInformation.camera, videoInformation.data);

        if (!Files.isDirectory(fullPathToSave)){
            Files.createDirectories(fullPathToSave);;
        }

        //Desmembra o vídeo original
        for (HashMap<String, String> fileInformation : listOfFutureFiles) {

            Path fileNamePath = Paths.get(String.valueOf(fullPathToSave), fileInformation.get("fileName"));
            File file = new File(fileNamePath.toUri());

            //Se o arquivo não existir, ele irá criar.
            if(!file.exists()){
                cutVideo(sourceVideoPath, String.valueOf(fileNamePath),
                        fileInformation.get("startTime"), fileInformation.get("finalTime"));
            }
            else {
                log.info("Fragmento existente: {}", fileInformation.get("fileName"));
            }

        }

        //Enviado video original para a fila apagar.
//        EraseDto eraseDto = new EraseDto();
//
//        eraseDto.pathToRemove = String.valueOf(sourceFilePath);
//        RabbitMQService rabbitMQService = new RabbitMQService();
//        rabbitMQService.enviaMensagem(RabbitmqConstantes.FILA_ERASE, eraseDto);

    }

}
