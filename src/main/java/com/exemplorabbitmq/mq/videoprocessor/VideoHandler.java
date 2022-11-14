package com.exemplorabbitmq.mq.videoprocessor;

import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class VideoHandler {

    private static String[] getVideoDuration(String videoPath) throws IOException {

        String[] command = {"cmd.exe", "/c", "ffprobe", videoPath,
                "-show_entries", "format=duration", "-of", "compact=p=0:nk=1", "-v", "0", "-sexagesimal"};

        Process process = Runtime.getRuntime().exec(command);
        InputStream inputStream = process.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String dataRead = bufferedReader.readLine();
        String hourMinSec = dataRead.split("\\.")[0];
        String[] time = hourMinSec.split(":");

        return time;
    }

    public static List<HashMap<String, String>> getFutureFilesName(Map<String, String> videoInformation) throws IOException, ParseException {


        String[] videoTime = getVideoDuration(
                videoInformation.get("path") + "\\" + videoInformation.get("file"));

        int hour = Integer.parseInt(videoTime[0]);
        int minutes = (hour*60) + Integer.parseInt(videoTime[1]);
        int seconds = Integer.parseInt(videoTime[2]);

        Date videoDate = new SimpleDateFormat("yyyy-MM-ddHHmmss").parse(
                videoInformation.get("data") + videoInformation.get("hora"));

        DateFormat fileNameFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        Date hourInicial = new SimpleDateFormat("HH:mm:ss").parse("00:00:00");

        List <HashMap<String,String>> futureFilesName = new ArrayList<HashMap<String,String>>();;

        for (int i=0; i < minutes; i++) {

            HashMap<String, String> dados = new HashMap<>();

            dados.put("fileName", fileNameFormat.format(videoDate) + "." + videoInformation.get("extensao"));

            videoDate = DateUtils.addMinutes(videoDate, 1);

        }

        if(seconds > 0){
            HashMap<String, String> dados = new HashMap<>();

            dados.put("fileName", fileNameFormat.format(videoDate) + "." + videoInformation.get("extensao"));
        }

        return futureFilesName;
    };
}
