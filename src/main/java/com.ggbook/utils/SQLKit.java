package com.ggbook.utils;

import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.Db;

import java.io.*;

/**
 * Created by Administrator on 2017/4/19.
 */
public class SQLKit {

    public static boolean importSQL(File cfile, String origin, String table) {
        try {
            String jdbcUrl = PropKit.get("jdbcUrl");
            String[] jdbcStr = jdbcUrl.split("\\/");
            String[] hostPortArr = jdbcStr[2].split(":");
            String[] databasesArr = jdbcStr[3].split("\\?");
            String host = hostPortArr[0];
            String port = hostPortArr[1];
            String databases = databasesArr[0];

            String path = cfile.getAbsolutePath();
            // 解密文本
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(cfile), "UTF-8"));
            String line = null;
            StringBuffer textStr = new StringBuffer();
            while((line = in.readLine())!=null) {
                textStr.append(line);
            }
            String aesText = AESKit.dncode(PropKit.get("export.data.secret"), textStr.toString()); // 文本解密
            int index = aesText.indexOf("INSERT INTO");
            if(index == -1) {
                return false;
            }
            aesText = aesText.substring(index).replace(origin, table);
            PrintStream ps = null;
            try {
                ps = new PrintStream(new FileOutputStream(cfile));
                ps.println(aesText);// 往文件里写入字符串
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } finally {
                in.close();
                ps.close();
            }

            // 导入sql数据
            StringBuffer buff = new StringBuffer(PathKit.getWebRootPath()+"/plugin"); // 插件目录
            Runtime runtime = Runtime.getRuntime();
            Process process;
            //设置上传路径
            String os = System.getProperty("os.name");
            if(os.toLowerCase().startsWith("win")){
                process = runtime.exec("cmd /c mysql -h "+host+" -P"+port+" -u"+PropKit.get("user")+" -p"+PropKit.get("password")+" --default-character-set=utf8 "+databases+"<"+path, null, new File(buff.toString()));
            }else{
                process = runtime.exec(new String[] { "/bin/sh","-c", "mysql -h "+host+" -P"+port+" -u"+PropKit.get("user")+" -p"+PropKit.get("password")+" --default-character-set=utf8 "+databases+"<"+path});
            }
            process.waitFor();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean importSQL(File cfile) {
        try {
            String jdbcUrl = PropKit.get("jdbcUrl");
            String[] jdbcStr = jdbcUrl.split("\\/");
            String[] hostPortArr = jdbcStr[2].split(":");
            String[] databasesArr = jdbcStr[3].split("\\?");
            String host = hostPortArr[0];
            String port = hostPortArr[1];
            String databases = databasesArr[0];


            String path = cfile.getAbsolutePath();
            // 导入sql数据
            StringBuffer buff = new StringBuffer(PathKit.getWebRootPath()+"/plugin"); // 插件目录
            Runtime runtime = Runtime.getRuntime();
            Process process;
            //设置上传路径
            String os = System.getProperty("os.name");
            if(os.toLowerCase().startsWith("win")){
                process = runtime.exec("cmd /c mysql -h "+host+" -P"+port+" -u"+PropKit.get("user")+" -p"+PropKit.get("password")+" --default-character-set=utf8 "+databases+"<"+path, null, new File(buff.toString()));
            }else{
                process = runtime.exec(new String[] { "/bin/sh","-c", "mysql -h "+host+" -P"+port+" -u"+PropKit.get("user")+" -p"+PropKit.get("password")+" --default-character-set=utf8 "+databases+"<"+path});
            }
            process.waitFor();

            cfile.delete();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void updateSQL() {
        try {
            File file = new File(PathKit.getWebRootPath() + "/update.sql");
            if (!file.exists()) {
                return;
            }
            String sqls = FileUtil.readFile(file.getAbsolutePath());
            for (String sql : sqls.split(";")) {
                Db.update(sql);
            }
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
