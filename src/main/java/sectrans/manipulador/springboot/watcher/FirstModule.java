package sectrans.manipulador.springboot.watcher;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import sectrans.manipulador.springboot.constantes.RabbitmqConstantes;
import sectrans.manipulador.springboot.dto.EraseDto;
import sectrans.manipulador.springboot.dto.ProcessDto;
import sectrans.manipulador.springboot.filehandler.SearchFiles;
import sectrans.manipulador.springboot.service.RabbitMQService;
import sectrans.manipulador.springboot.constantes.SectransConstantes;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

@Component
@Service
@EnableScheduling
@Slf4j
public class FirstModule {

    @Autowired
    private RabbitMQService rabbitMQService;
    public final List<String> processedFiles = new ArrayList<>();
    @Autowired
    @Scheduled(cron = "*/30 * * * * *")
    public void process() throws IOException {

        //Módulo1
        //Busca por arquivos no diretorio base e retorna uma lista de Path
        SearchFiles searchFiles = new SearchFiles();
        List<Path> listExistingFiles = searchFiles.getFiles(SectransConstantes.SOURCE_DIRECTORY);

        //Itera sobre a lista e retorna os dados do arquivo.
        for (Path existingFile:listExistingFiles) {

            //Filtra arquivos manipulados anteriormente.
            if(processedFiles.contains(String.valueOf(existingFile))){
                log.info("Arquivo já manipulado: {}", existingFile);
                continue;
            }

            //Valida a extensão
            if(SectransConstantes.VALID_EXTENSIONS.contains(FilenameUtils.getExtension(String.valueOf(existingFile)))) {

                //Envia arquivo válido para fila de processamento
                ProcessDto processDto = new ProcessDto();
                processDto.sourceFilePath = String.valueOf(existingFile);
                processDto.sourcePathToSave = String.valueOf(SectransConstantes.PATH_TO_SAVE);

                this.processedFiles.add(String.valueOf(existingFile));
                this.rabbitMQService.enviaMensagem(RabbitmqConstantes.FILA_PROCESS, processDto);

            }
            //Envia arquivo inválido para fila de apagar.
            else {
                EraseDto eraseDto = new EraseDto();

                eraseDto.pathToRemove = String.valueOf(existingFile);
                this.rabbitMQService.enviaMensagem(RabbitmqConstantes.FILA_ERASE, eraseDto);
            }
        }
    }
}