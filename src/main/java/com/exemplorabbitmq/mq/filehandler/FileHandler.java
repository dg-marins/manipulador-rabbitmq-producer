package com.exemplorabbitmq.mq.filehandler;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class FileHandler {
    public Map<String, String> getFileInfo(Path source){

        String filePath = source.toString();
        String[] filePathSplit = filePath.toString().split("VIDEO")[1].split("\\\\");

        String filename = source.getFileName().toString();
        String[] cutSting = filename.split("-",5);
        String camera = String.valueOf(cutSting[4].charAt(5));
        String hour = cutSting[2];
        String extension = cutSting[4].split("\\.")[1];
        String car = filePathSplit[1];
        String date = filePathSplit[2];


        Map<String, String> object = new HashMap<>();
        object.put("camera", camera);
        object.put("data", date);
        object.put("hora", hour);
        object.put("extensao", extension);
        object.put("carro", car);
        object.put("path", source.toFile().getParent());
        object.put("file", filename);

        return object;
    }


}
