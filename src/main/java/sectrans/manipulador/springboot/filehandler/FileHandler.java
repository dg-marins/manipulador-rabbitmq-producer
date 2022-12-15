package sectrans.manipulador.springboot.filehandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.backoff.ThreadWaitSleeper;
import sectrans.manipulador.springboot.dto.VideoDto;

import java.io.File;
import java.nio.file.Path;

@Slf4j
public class FileHandler {
public VideoDto getFileInfo(Path source){

        String filePath = source.toString();
        String[] filePathSplit = filePath.split("VIDEO")[1].split("\\\\");

        String filename = source.getFileName().toString();
        String[] cutString = filename.split("-",5);
        String camera = String.valueOf(cutString[4].charAt(5));
        String hour = cutString[2];
        String extension = cutString[4].split("\\.")[1];
        String car = filePathSplit[1];
        String date = filePathSplit[2];

        VideoDto videoDto = new VideoDto();
        videoDto.camera = camera;
        videoDto.data = date;
        videoDto.hora = hour;
        videoDto.extension = extension;
        videoDto.car = car;
        videoDto.path = source.toFile().getParent();
        videoDto.file = filename;

        return videoDto;
    }


    public static void eraseFile(String pathToRemove) throws InterruptedException {

        File file = new File(pathToRemove);
        int x = 0;

        while(x <= 2) {

            if (file.delete()) {
                log.info("Arquivo deletado: {}", pathToRemove);
                return;
            }
            Thread.sleep(5000);
            x++;
        }

        log.info("Falha ao deletar arquivo: {}", pathToRemove);

    }

}
