package com.exemplorabbitmq.mq;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {

    public Map<String, String> getFileInfo(Path filePath){

        String direct = filePath.toString();
        String[] parts = direct.split("Video");
        String[] partSplit = parts[1].split("/");
        String car = partSplit[1];

        String filename = filePath.getFileName().toString();
        String[] cutSting = filename.split("-",5);
        String camera = String.valueOf(cutSting[4].charAt(5));
        String date = cutSting[1];
        String hour = cutSting[2];
        String extension = cutSting[4].split("\\.")[1];

        Map<String, String> object = new HashMap<>();
        object.put("camera", camera);
        object.put("data", date);
        object.put("hora", hour);
        object.put("extensao", extension);
        object.put("carro", car);
        object.put("path", String.valueOf(filePath));

        return object;
    }

}
