package sectrans.manipulador.springboot.videoprocessor;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.DateUtils;
import sectrans.manipulador.springboot.dto.VideoDto;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class VideoHandler {

    private static String[] getVideoDuration(String videoPath) throws IOException {

        String[] command = {"cmd.exe", "/c", "ffprobe", videoPath,
                "-show_entries", "format=duration", "-of", "compact=p=0:nk=1", "-v", "0", "-sexagesimal"};

        Process process = Runtime.getRuntime().exec(command);
        InputStream inputStream = process.getInputStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String dataRead = bufferedReader.readLine();
        String hourMinSec = dataRead.split("\\.")[0];

        return hourMinSec.split(":");
    }

    public static List<HashMap<String, String>> getFutureFilesName(
            VideoDto videoInformation) throws IOException, ParseException {


        String[] videoTime = getVideoDuration(
                videoInformation.path + "\\" + videoInformation.file);

        int hour = Integer.parseInt(videoTime[0]);
        int minutes = (hour*60) + Integer.parseInt(videoTime[1]);
        int seconds = Integer.parseInt(videoTime[2]);

        Date videoDate = new SimpleDateFormat("yyyy-MM-ddHHmmss").parse(
                videoInformation.data + videoInformation.hora);

        DateFormat fileNameFormat = new SimpleDateFormat("yyyyMMddHHmmss");

        List <HashMap<String,String>> futureFilesName = new ArrayList<HashMap<String,String>>();;

        Date hourInicial = new SimpleDateFormat("HH:mm:ss").parse("00:00:00");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

        for (int i=0; i < minutes; i++) {

            HashMap<String, String> dados = new HashMap<>();

            dados.put("fileName", fileNameFormat.format(videoDate) + "." + videoInformation.extension);
            dados.put("startTime", timeFormat.format(hourInicial));

            videoDate = DateUtils.addMinutes(videoDate, 1);

            hourInicial = DateUtils.addMinutes(hourInicial, 1);
            dados.put("finalTime", timeFormat.format(hourInicial) );

            futureFilesName.add(dados);
        }

        if(seconds > 0){
            HashMap<String, String> dados = new HashMap<>();

            dados.put("fileName", fileNameFormat.format(videoDate) + "." + videoInformation.extension);
            dados.put("startTime", timeFormat.format(hourInicial));

            hourInicial = DateUtils.addSeconds(hourInicial, seconds);
            dados.put("finalTime", timeFormat.format(hourInicial) );

            futureFilesName.add(dados);
        }

        return futureFilesName;
    };

    public static void cutVideo(
            String sourceFile, String newFileName, String startTime, String endTime) throws IOException {

        String[] command = {"ffmpeg", "-i", sourceFile,"-ss", startTime,"-to", endTime, "-c", "copy",
                newFileName};

        Runtime.getRuntime().exec(command);

        log.info("Fragmento Criado: {}", newFileName);

    }
}
