package cn.itsource.ffmpeg;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: java0412-ymcc
 * @BelongsPackage: cn.itsource.ffmpeg
 * @Author: Director
 * @CreateTime: 2022-09-05  18:13
 * @Description: TODO
 * @Version: 1.0
 */
public class TestFfmpeg {

    @Test
    public void testVideoBuilder() {
        // processBuilder：可以帮我们在java代码中执行黑窗口命令
        ProcessBuilder processBuilder = new ProcessBuilder();
        List<String> commond = new ArrayList<>();
        // 本地ffmpeg的安装目录
        commond.add("D:\\itEnvironment\\ffmpeg\\bin\\ffmpeg");
        commond.add("-i");
        // 要上传的文件
        commond.add("C:\\Users\\blank\\Desktop\\test\\测试视频.mp4");
        commond.add("-hls_time");
        commond.add("10");
        commond.add("-hls_list_size");
        commond.add("0");
        commond.add("-hls_segment_filename");
        // 转码之后的ts文件目录，每个ts文件就是一个视频小切片
        commond.add("C:\\Users\\blank\\Desktop\\test\\habor%05d.ts");
        // 转码之后的m3u8文件目录，m3u8文件记录了该视频的所有ts文件信息
        commond.add("C:\\Users\\blank\\Desktop\\test\\habor.m3u8");
        processBuilder.command(commond);
        //将标准输入流和错误输入流合并，通过标准输入流读取信息
        processBuilder.redirectErrorStream(true);
        try {
            //启动进程
            Process start = processBuilder.start();
            //获取输入流
            InputStream inputStream = start.getInputStream();
            //转成字符输入流
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "gbk");
            int len = -1;
            char[] c = new char[1024];
            StringBuffer outputString = new StringBuffer();
            //读取进程输入流中的内容
            while ((len = inputStreamReader.read(c)) != -1) {
                String s = new String(c, 0, len);
                outputString.append(s);
                System.out.print(s);
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
