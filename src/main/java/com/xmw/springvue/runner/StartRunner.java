package com.xmw.springvue.runner;

import com.xmw.springvue.config.GlobalUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author: xmw
 * @date: 2018/7/17 17:59
 * @version: 1.0
 * Description:
 */
@Component
public class StartRunner implements CommandLineRunner {
    @Value("${spring.profiles.active}")
    private String profile;


    private static Process runner = null;
    @Override
    public void run(String... args) {
        try {
            if("dev".equals(profile)){
                if(Files.exists(GlobalUtil.vuePath)){
                    if(!nodeIsRun()){
                        run(GlobalUtil.vuePath,"yarn serve --open");
                    }
                    RouterRunner.startWatch();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void run(Path path, String cmd){
        try {
            if(runner == null){
                runner = Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd.exe","/k",cmd},null ,path.toFile());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void cmd(Path path,String cmd){
        try {
            Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd.exe","/c",cmd},null ,path.toFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void destroy(){
        if(runner != null){
            runner.destroy();
            runner = null;
        }
    }

    private static boolean nodeIsRun(){
        BufferedReader br=null;
        boolean result = false;
        try {
            // /c运行结束后关闭  /k 不关闭
            Process p=Runtime.getRuntime().exec(new String[]{"cmd","/c","tasklist | findstr \"node.exe\""},null ,null);
            br=new BufferedReader(new InputStreamReader(p.getInputStream(), Charset.forName("GBK")));
            String line=null;
            while((line=br.readLine())!=null){
                result = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }  finally{
            if(br!=null){
                try{
                    br.close();
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void main(String[] args) throws IOException {
    }
}
