package sectrans.manipulador.springboot.consumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import sectrans.manipulador.springboot.constantes.QueueConstants;
import sectrans.manipulador.springboot.dto.EraseDto;
import sectrans.manipulador.springboot.dto.ProcessConfig;
import sectrans.manipulador.springboot.filehandler.FileHandler;
import sectrans.manipulador.springboot.videoprocessor.VideoManager;

import java.io.IOException;
import java.text.ParseException;

@Component
@Slf4j
@RequiredArgsConstructor
public class QueueListener {

    private final VideoManager manager;

    @RabbitListener(queues = QueueConstants.PROCESS_QUEUE)
    private void consumoProcessVideo(ProcessConfig processConfig) throws IOException, ParseException {
        log.info("Consumido Fila PROCESS: {}", processConfig.sourceFilePath);
        manager.split(processConfig.sourceFilePath, processConfig.sourcePathToSave);

    }

    @RabbitListener(queues = QueueConstants.DELETION_QUEUE)
    private void consumoEraseVideo(EraseDto eraseDto){
        log.info("Consumido Fila ERASE: {}", eraseDto.pathToRemove);
        FileHandler.eraseFile(eraseDto.pathToRemove);
    }
}
