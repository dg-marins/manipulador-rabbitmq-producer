package sectrans.manipulador.springboot.videoprocessor;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sectrans.manipulador.springboot.constantes.RabbitmqConstantes;
import sectrans.manipulador.springboot.dto.EraseDto;
import sectrans.manipulador.springboot.filehandler.FileHandler;
import sectrans.manipulador.springboot.service.RabbitMQService;

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
public class VideoProcess {

    public static void process(String sourceFilePath, String sourcePathToSave) throws IOException, ParseException {


        FileHandler fileHandler = new FileHandler();
        Map<String,String> videoInformation = fileHandler.getFileInfo(Path.of(sourceFilePath));

        //Não trata o vídeo enquanto não tenha finalizado o descarregamento.
        long fileSize = Files.size(Path.of(sourceFilePath));
        while(fileSize <= 0){
            fileSize = Files.size(Path.of(sourceFilePath));
        }

        String sourceVideoPath = String.format("%s%s%s",videoInformation.get("path"), "/", videoInformation.get("file"));
        List<HashMap<String, String>> listOfFutureFiles = getFutureFilesName(videoInformation);

        Path fullPathToSave = Paths.get(String.valueOf(sourcePathToSave), videoInformation.get("carro"),
                "camera"+videoInformation.get("camera"), videoInformation.get("data"));

        if (!Files.isDirectory(fullPathToSave)){
            Files.createDirectories(fullPathToSave);;
        }

        for (HashMap<String, String> fileInformation : listOfFutureFiles) {

            Path fileNamePath = Paths.get(String.valueOf(fullPathToSave), fileInformation.get("fileName"));
            File file = new File(fileNamePath.toUri());

            //Se o arquivo não existir, ele irá criar.
            if(!file.exists()){
                cutVideo(sourceVideoPath, String.valueOf(fileNamePath),
                        fileInformation.get("startTime"), fileInformation.get("finalTime"));
            }

        }

    }

}
