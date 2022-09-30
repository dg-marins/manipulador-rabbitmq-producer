package com.exemplorabbitmq.mq;

import java.util.HashMap;
import java.util.Map;

public class FileHandler {

    public Map<String, String> getFileInfo(String file){

        String[] cutSting = file.split("-",5);
        String camera = "camera" + cutSting[4].charAt(5);
        String date = cutSting[1];
        String hour = cutSting[2];
        String extension = cutSting[4].split("\\.")[1];

        Map<String, String> object = new HashMap<>();
        object.put("camera", camera);
        object.put("data", date);
        object.put("hora", hour);
        object.put("extensao", extension);

        return object;
    }

}
