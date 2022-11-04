package com.exemplorabbitmq.mq.videoprocessor;

import org.apache.commons.lang3.time.DateUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

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

    public static void getFutureFilesName(Map<String, String> videoInformation) throws IOException, ParseException {

        String[] videoTime = getVideoDuration(
                videoInformation.get("path") + "\\" + videoInformation.get("file"));

        int hour = Integer.parseInt(videoTime[0]);
        int minutes = (hour*60) + Integer.parseInt(videoTime[1]);
        int seconds = Integer.parseInt(videoTime[2]);

        String videoStartTime = videoInformation.get("data") + videoInformation.get("hora") ;

        Date date = new SimpleDateFormat("yyyy-MM-ddHHmmss").parse(videoStartTime);
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        for (int i=0; i < minutes; i++) {

            String fileName = dateFormat.format(date) + "." + videoInformation.get("extensao");
            System.out.println(fileName);

            date = DateUtils.addMinutes(date, 1);
        }

        System.out.println(date);


    };
}
