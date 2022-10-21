package com.exemplorabbitmq.mq.videoprocessor;


import java.util.Map;

public class VideoProcess {
    Map<String,String> videoInformation;

    public VideoProcess(Map<String,String> informations){
        this.videoInformation = informations;

//        VideoHandler videoHandler = new VideoHandler();

    }

    public void Teste(){
        System.out.println(videoInformation);
    }
}
