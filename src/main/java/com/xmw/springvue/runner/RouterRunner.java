package com.xmw.springvue.runner;

import com.xmw.springvue.config.GlobalUtil;
import com.xmw.util.base.JsJsonUtil;
import com.xmw.util.base.ListUtil;
import com.xmw.util.base.StringUtil;
import com.xmw.util.file.FileBaseUtil;
import com.xmw.util.file.PathBaseUtil;
import com.xmw.util.file.WatchUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: xmw
 * @date: 2018/7/22 12:15
 * @version: 1.0
 * Description:
 */
public class RouterRunner {

    private static final String indexFile = "index.vue";

    private static final String index = "index";

    private static final String childrenDir = "children";

    private volatile static Map<String,Object> config = null;

    public static void startWatch(){
        initConfig();
        writeRouter();
        WatchUtil.makeWatch(GlobalUtil.routerCustomPath,ListUtil.toList(RouterRunner::initConfig, RouterRunner::writeRouter),ListUtil.toList(GlobalUtil.routerPath), StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE,StandardWatchEventKinds.ENTRY_MODIFY);
        WatchUtil.makeWatch(GlobalUtil.viewPath,ListUtil.toList(RouterRunner::writeRouter), StandardWatchEventKinds.ENTRY_CREATE, StandardWatchEventKinds.ENTRY_DELETE);
    }

    public static boolean initConfig(){
        try {
            byte[] bytes = Files.readAllBytes(GlobalUtil.routerCustomPath);
            config = JsJsonUtil.mapper.readValue(bytes,Map.class);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
    public static boolean writeRouter(){
        try {
            if(config == null || config.get("auto") == null || (Boolean) config.get("auto")){
                FileBaseUtil.write(GlobalUtil.routerPath, generateRouter(GlobalUtil.viewPath));
                return true;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String generateRouter(Path targetPath) {
        StringBuilder sb = new StringBuilder();
        List<Router> routers = new ArrayList<>();
        List<Map<String,Object>> list = new ArrayList<>();
        try {
            getAllRouter(targetPath, targetPath, routers, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sb.append("//本文件由系统自动生成如果需要手动修改路由 请在routerCustom文件修改\n");
        appendImport(routers, sb);
        sb.append("export default");
        routersToList(routers,list);
        sb.append(JsJsonUtil.objToStr(list));
        return sb.toString();
    }
    private static void appendImport(List<Router> routers,StringBuilder sb){
        if(routers != null){
            for (Router router : routers) {
                if(router.getAsync()){
                    sb.append(String.format("const %s = () => import('@/views/%s') \n",router.getComponent(),router.relativePath));
                }else{
                    sb.append(String.format("import %s from '@/views/%s'\n",router.getComponent(),router.relativePath));
                }
                appendImport(router.getChildren(), sb);
            }
        }
    }
    private static void routersToList(List<Router> routers,List<Map<String,Object>> list){
        if(routers != null){
            for (Router router : routers) {
                Map<String, Object> map = new LinkedHashMap<>();
                map.put("name",router.getName());
                map.put("path",router.getPathStr());
                map.put("component",new JsJsonUtil.JsObj(router.getComponent()));
                if(router.getChildren() != null){
                    List<Map<String,Object>> list1 = new ArrayList<>();
                    routersToList(router.getChildren(),list1);
                    map.put("children",list1);
                }
                list.add(map);
            }
        }
    }
    private static void getAllFile(Path path, List<Path>list) throws IOException {
        if(!Files.isDirectory(path)){
            list.add(path);
        }else{
            for (Path p : Files.list(path).collect(Collectors.toList())) {
                getAllFile(p, list);
            }
        }
    }
    private static List<Path> getAllFile(Path path) throws IOException {
        List<Path> list = new ArrayList<>();
        getAllFile(path, list);
        return list;
    }
    /**
     * @author xmw
     * @date 2018/7/21 18:44
     ** @param path
     * @param list
     * @return void
     * @description : Router分为有children的和没children的,
     * 有children的文件夹下 必然又有一个 index.vue 和一个children文件夹.
     */
    private static void getAllRouter(Path targetPath, Path path, List<Router> list, Router parent) throws IOException {
        if(!Files.isDirectory(path)){
            Router init = Router.init(targetPath, path, null, parent);
            if(!init.getDelete()) {
                list.add(init);
            }
        }else{
            Path indexPath = null;
            Path childrenPath = null;
            List<Path> paths = Files.list(path).collect(Collectors.toList());
            for (Path p : paths) {
                if(!Files.isDirectory(p) && indexFile.equalsIgnoreCase(p.getFileName().toString())){
                    indexPath = p;
                    continue;
                }
                if(Files.isDirectory(p) && childrenDir.equalsIgnoreCase(p.getFileName().toString())){
                    childrenPath = p;
                }
            }
            if(indexPath != null && childrenPath != null){
                List<Router> children = new ArrayList<>();
                Router init = Router.init(targetPath, indexPath, children, parent);
                if(!init.getDelete()){
                    list.add(init);
                }
                getAllRouter(targetPath, childrenPath, children, init);
            }else{
                for (Path p : paths) {
                    getAllRouter(targetPath, p, list, parent);
                }
            }
        }
    }


    private static String getFileName(Path path){
        String fileName = path.getFileName().toString();
        String[] split = fileName.split("\\.");
        if(split.length>0){
            fileName = split[0];
        }
        return StringUtil.toDownStart(fileName);
    }

    private static String getObjName(Path path, Path targetPath){
        Path relativize = targetPath.relativize(path);
        if(relativize.getParent() == null){
            return getFileName(path);
        }
        return PathBaseUtil.join(relativize.getParent(),"_") + "_"+getFileName(path);
    }


    public static class Router{
        private Path path;
        private Router parent;
        private List<Router> children;
        private String relativePath;
        private String name;
        private String pathStr;
        private String component;
        private boolean delete = false;
        private boolean async = false;

        public Router(Path path, Router parent, List<Router> children) {
            this.path = path;
            this.parent = parent;
            this.children = children;
        }
        /**
         * @author xmw
         * @date 2018/7/30 10:48
         ** @param targetPath viewpath
         * @param path
         * @param children
         * @param parent
         * @return com.xmw.runner.RouterRunner.Router
         * @description :
         */
        public static Router init(Path targetPath, Path path, List<Router> children, Router parent){
            Router router = new Router(path, parent,children);
            String fileName = getFileName(path);
            //如果文件名称是index.vue 那么久只要那个根路径
            String annoName = index.equalsIgnoreCase(fileName)?"":fileName;
            //相对于 views文件的路径
            String relativePath = PathBaseUtil.join(targetPath.relativize(path.getParent()),"/");
            relativePath = StringUtil.isNotBlank(relativePath)?relativePath+"/":relativePath;
            //相对于父类的路径
            String parentRelativePath = relativePath;
            if(router.getParent() != null){
                parentRelativePath = PathBaseUtil.join(router.getParent().getPath().getParent().resolve(childrenDir).relativize(path.getParent()),"/");
                parentRelativePath = StringUtil.isNotBlank(parentRelativePath)?parentRelativePath+"/":parentRelativePath;
            }
            router.setRelativePath(relativePath + path.getFileName().toString());
            router.setName(relativePath + fileName);
            router.setPathStr("/"+parentRelativePath + fileName);
            router.setComponent(getObjName(path, targetPath));
            //将配置文件导入
            try {
                if(RouterRunner.config != null){
                    List routes = (List)RouterRunner.config.get("routes");
                    if(routes != null){
                        String name = router.getName();
                        Optional<Object> findRouter = routes.stream().filter(r -> (name.equals(((Map) r).get("name")))).findFirst();
                        if(findRouter.isPresent()){
                            Map<String, Object> findMap = (Map)findRouter.get();
                            if(findMap.get("path") != null){
                                router.setPathStr((String)findMap.get("path"));
                            }
                            if(findMap.get("async") != null){
                                router.setAsync((Boolean)findMap.get("async"));
                            }
                            if(findMap.get("delete") != null){
                                router.setDelete((Boolean)findMap.get("delete"));
                            }
                        }
                    }
                }
            }catch (Exception e){
                e.printStackTrace();
            }
            return router;
        }
        public Path getPath() {
            return path;
        }

        public void setPath(Path path) {
            this.path = path;
        }

        public List<Router> getChildren() {
            return children;
        }

        public void setChildren(List<Router> children) {
            this.children = children;
        }

        public Router getParent() {
            return parent;
        }

        public void setParent(Router parent) {
            this.parent = parent;
        }

        public String getName() {
            return name;
        }

        public boolean getDelete() {
            return delete;
        }

        public void setDelete(boolean delete) {
            this.delete = delete;
        }

        public boolean getAsync() {
            return async;
        }

        public void setAsync(boolean async) {
            this.async = async;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPathStr() {
            return pathStr;
        }

        public void setPathStr(String pathStr) {
            this.pathStr = pathStr;
        }

        public String getComponent() {
            return component;
        }

        public void setComponent(String component) {
            this.component = component;
        }

        public String getRelativePath() {
            return relativePath;
        }

        public void setRelativePath(String relativePath) {
            this.relativePath = relativePath;
        }
    }
    public static void main(String[] args) throws IOException {
        //System.out.println(GlobalUtil.projectPath.startsWith(Paths.get("D:\\项目\\AppCallbackServer")));
/*        StarterRunner starterRunner = new StarterRunner();
        System.out.println(starterRunner.generateRouter(GlobalUtil.projectPath.resolve("vue").resolve("src").resolve("views")));
        String a = "sbsbaaawww";
        System.out.println(a.replaceFirst("a", ""));*/
/*        initConfig();
        System.out.println(generateRouter(GlobalUtil.viewPath));*/
        initConfig();
        generateRouter(GlobalUtil.viewPath);
    }
}
