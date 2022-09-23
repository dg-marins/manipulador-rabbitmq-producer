package com.exemplorabbitmq.mq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class Main {

    public static void main(String[] args) throws IOException {
//
        boolean recursive = true;

        Path dir = Paths.get("/home/publico/imagens");

        WatchDir watchDir = new WatchDir(dir, recursive);
        watchDir.start();

        SpringApplication.run(SpringRabbitmqProducerApplication.class, args);


    }
}
