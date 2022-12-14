package sectrans.manipulador.springboot.conections;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;
import sectrans.manipulador.springboot.constantes.QueueConstants;

import javax.annotation.PostConstruct;

@Component
public class RabbitMQConection {
    private static final String NAME_EXCHANGE = "amq.direct";
    private AmqpAdmin amqpAdmin;

    public RabbitMQConection(AmqpAdmin amqpAdmin){
        this.amqpAdmin = amqpAdmin;
    }

    private Queue fila(String nameFila){
        return new Queue(nameFila, true, false, false);
    }

    private DirectExchange chanceDirect(){
        return new DirectExchange(NAME_EXCHANGE);
    }

    private Binding relation(Queue fila, DirectExchange swap){
        return new Binding(fila.getName(), Binding.DestinationType.QUEUE, chanceDirect().getName(), fila.getName(), null);
    }

    @PostConstruct
    private void add(){
        Queue filaProcess = this.fila(QueueConstants.PROCESS_QUEUE);
        Queue filaErase = this.fila(QueueConstants.DELETION_QUEUE);

        DirectExchange exchange = this.chanceDirect();

        Binding linkProcess = this.relation(filaProcess, exchange);
        Binding linkErase = this.relation(filaErase, exchange);

        //Criando as filas RabbitMQ
        this.amqpAdmin.declareQueue(filaProcess);
        this.amqpAdmin.declareQueue(filaErase);

        this.amqpAdmin.declareExchange(exchange);

        this.amqpAdmin.declareBinding(linkProcess);
        this.amqpAdmin.declareBinding(linkErase);
    }

}
