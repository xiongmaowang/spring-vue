package com.xmw.springvue.runner;

import com.xmw.springvue.config.GlobalUtil;
import com.xmw.util.CmdUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.file.Files;

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

    @Override
    public void run(String... args) {
        try {
            if("dev".equals(profile)){
                if(Files.exists(GlobalUtil.vuePath)){
                    if(!nodeIsRun()){
                        CmdUtil.run(GlobalUtil.vuePath, "start","cmd.exe","/k","yarn serve --open");
                    }
                    RouterRunner.startWatch();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static boolean nodeIsRun(){
        BufferedReader br=null;
        boolean result = false;
        try {
            // /c运行结束后关闭  /k 不关闭
            Process p = CmdUtil.run(null,"tasklist | findstr \"node.exe\"");
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
