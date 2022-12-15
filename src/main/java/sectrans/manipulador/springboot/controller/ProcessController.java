package sectrans.manipulador.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sectrans.manipulador.springboot.constantes.QueueConstants;
import sectrans.manipulador.springboot.dto.ProcessConfig;
import sectrans.manipulador.springboot.service.RabbitMQService;

@RestController
@RequestMapping(value = "process")
public class ProcessController {

    @Autowired
    private RabbitMQService rabbitMQService;
    @PutMapping
    private ResponseEntity processVideo(@RequestBody ProcessConfig processConfig){

        this.rabbitMQService.enviaMensagem(QueueConstants.PROCESS_QUEUE, processConfig);
        return new ResponseEntity(HttpStatus.OK);

    }
}
