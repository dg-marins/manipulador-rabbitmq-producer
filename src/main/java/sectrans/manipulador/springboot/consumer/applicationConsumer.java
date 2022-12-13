package sectrans.manipulador.springboot.consumer;

import org.apache.tomcat.jni.File;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import sectrans.manipulador.springboot.constantes.RabbitmqConstantes;
import sectrans.manipulador.springboot.dto.EraseDto;
import sectrans.manipulador.springboot.dto.ProcessDto;
import sectrans.manipulador.springboot.filehandler.FileHandler;
import sectrans.manipulador.springboot.videoprocessor.VideoProcess;

import java.io.IOException;
import java.text.ParseException;

@Component
public class applicationConsumer {

    @RabbitListener(queues = RabbitmqConstantes.FILA_PROCESS)
    private void consumoProcessVideo(ProcessDto processDto) throws IOException, ParseException {
        System.out.println("----------------CONSUMIDO PROCESS -----------------");
        System.out.println(processDto.sourceFilePath);
        System.out.println(processDto.sourcePathToSave);
        System.out.println("------------------------------------------");

        VideoProcess.process(processDto.sourceFilePath, processDto.sourcePathToSave);

    }

    @RabbitListener(queues = RabbitmqConstantes.FILA_ERASE)
    private void consumoEraseVideo(EraseDto eraseDto){
        System.out.println("----------------CONSUMIDO ERASE -----------------");
        System.out.println(eraseDto.pathToRemove);
        System.out.println("------------------------------------------");

        FileHandler.eraseFile(eraseDto.pathToRemove);
    }
}
