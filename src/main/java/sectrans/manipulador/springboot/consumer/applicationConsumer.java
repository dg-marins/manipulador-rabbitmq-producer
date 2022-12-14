package sectrans.manipulador.springboot.consumer;

import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class applicationConsumer {

    @RabbitListener(queues = RabbitmqConstantes.FILA_PROCESS)
    private void consumoProcessVideo(ProcessDto processDto) throws IOException, ParseException {
        log.info("Consumido Fila PROCESS: {}", processDto.sourceFilePath);
        VideoProcess.process(processDto.sourceFilePath, processDto.sourcePathToSave);

    }

    @RabbitListener(queues = RabbitmqConstantes.FILA_ERASE)
    private void consumoEraseVideo(EraseDto eraseDto){
        log.info("Consumido Fila ERASE: {}", eraseDto.pathToRemove);
        FileHandler.eraseFile(eraseDto.pathToRemove);
    }
}
