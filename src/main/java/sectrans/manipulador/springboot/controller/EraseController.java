package sectrans.manipulador.springboot.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import sectrans.manipulador.springboot.constantes.QueueConstants;
import sectrans.manipulador.springboot.dto.EraseDto;
import sectrans.manipulador.springboot.service.RabbitMQService;

@RestController
@RequestMapping(value = "erase")
public class EraseController {

    @Autowired
    private RabbitMQService rabbitMQService;
    @PutMapping
    private ResponseEntity eraseFile(@RequestBody EraseDto eraseDto){

        this.rabbitMQService.enviaMensagem(QueueConstants.DELETION_QUEUE, eraseDto);
        System.out.println(eraseDto.pathToRemove);
        return new ResponseEntity(HttpStatus.OK);

    }
}
