package sectrans.manipulador.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import sectrans.manipulador.springboot.filehandler.SearchFiles;
import sectrans.manipulador.springboot.videoprocessor.VideoProcess;
import sectrans.manipulador.springboot.watcher.WatchDir;
import org.apache.commons.io.FilenameUtils;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
@EnableScheduling
public class SectransManipuladorApplication {

    public static void main(String[] args) {

        SpringApplication.run(SectransManipuladorApplication.class, args);

    }

    @Scheduled(cron = "*/5 * * * * *")
    public void scheduling() {
        System.out.println("Chamando metodo a cada 5 seg");
    }
}
