package com.xmw.springvue.config;


import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class GlobalUtil {
    //项目地址 如果修改了ide 的 output path, 需要修改
    public static Path projectPath ;
    static {
        try {
            projectPath = Paths.get(GlobalUtil.class.getClassLoader().getResource("").toURI()).getParent().getParent();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    public final static Path projectJavaPath = projectPath.resolve(Paths.get("src","main","java"));
    public final static Path projectResourcePath = projectPath.resolve(Paths.get("src","main","resources"));
    //vue
    public static final Path vuePath = GlobalUtil.projectPath.resolve("vue");
    public static final Path viewPath = vuePath.resolve("src").resolve("views");
    public static final Path routerPath = vuePath.resolve("src").resolve("build").resolve("auto").resolve("routerAuto.js");
    public static final Path routerCustomPath = vuePath.resolve("src").resolve("build").resolve("auto").resolve("routerConfig.json");

}
