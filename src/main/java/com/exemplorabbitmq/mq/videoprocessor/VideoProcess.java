package com.exemplorabbitmq.mq.videoprocessor;


import java.io.*;
import java.text.ParseException;
import java.util.Map;

import static com.exemplorabbitmq.mq.videoprocessor.VideoHandler.getFutureFilesName;

public class VideoProcess {
    Map<String,String> videoInformation;

    public VideoProcess(Map<String,String> informations) throws IOException, ParseException {
        this.videoInformation = informations;

        String videoPath = videoInformation.get("path") + "\\" + videoInformation.get("file");

        VideoHandler videoHandler = new VideoHandler();

        getFutureFilesName(videoInformation);

    }

}
