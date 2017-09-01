package com.ggbook.utils;

import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;
import net.sf.jmimemagic.Magic;
import org.apache.commons.io.output.FileWriterWithEncoding;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件工具类
 * 1、报文系统
 * 2、文件指纹
 * 3、读取文件
 */
public class FileUtil {

	/**分系统的报文根路径*/
	public static final String WSDL_PATH = System.getProperty("os.name").toLowerCase().startsWith("win")?PropKit.get("wsdlfile.window.path"):PropKit.get("wsdlfile.linux.path");

	/**
	 * 计算文件指纹的算法
	 */
	private static final String ALGORITHM = "SHA-1";
	
	/**
	 * 写省运政报文
	 * @param filename 文件名
	 * @param content 文件内容
	 * @return
	 */
	public static String writeHighwayWsdlfile(String filename, String content){
		String _busNo = new SimpleDateFormat("yyMMdd").format(new Date());
		return writeWsdlfile("highway", _busNo, filename, content);
	}

	/**
	 * 写查询报文
	 * @param filename 文件名
	 * @param content 文件内容
	 * @return
	 */
	public static String writeQueryWsdlfile(String filename, String content){
		String tradeNo = ToolUtil.generationRandom();
		return writeQueryWsdlfile(tradeNo, filename, content);
	}

	/**
	 * 写查询报文
	 * @param tradeNo 文件夹
	 * @param filename 文件名
	 * @param content 文件内容
	 * @return
	 */
	public static String writeQueryWsdlfile(String tradeNo, String filename, String content){
		return writeWsdlfile("query", tradeNo, filename, content);
	}
	
	/**
	 * 获取报文
	 * @param busNo 客运订单号
	 * @param filename 文件名
	 * @return
	 */
	public static File getWsdlFile(String busNo, String filename){
		String path = WSDL_PATH+"/trade/";
        if(busNo.startsWith("2")){
            path += busNo.substring(0,4)+"/"+busNo.substring(4,6)+"/"+busNo.substring(6,8)+"/"+busNo;
        }else {
            path += busNo.substring(0,2)+"/"+busNo.substring(2,4)+"/"+busNo.substring(4,6)+"/"+busNo;
        }
		File file = new File(path+"/"+filename);
		if(file.exists()){
			return file;
		}
		return null;
	}
	
	/**
	 * 获取报文内容
	 * @param busNo 客运订单号
	 * @param filename 文件名
	 * @return
	 */
	public static String getWsdlFileContent(String busNo, String filename){
		File file = getWsdlFile(busNo, filename);
		if(file != null){
			try {
				return FileUtil.readFileByInputStream(file.getAbsolutePath());
			} catch (IOException e) {
			}
		}
		return null;
	}
	
	/**
	 * 写报文
	 * @param busNo 客运订单号
	 * @param filename 文件名
	 * @param content 文件内容
	 * @return
	 */
	public static String writeWsdlfile(String busNo, String filename, String content){
		return writeWsdlfile("trade", busNo, filename, content);
	}

	/**
	 * 写报文
	 * @param type 报文类型【query：查班次报文，trade：业务报文】
	 * @param busNo 客运订单号
	 * @param filename 文件名
	 * @param content 报文内容
     * @return
     */
	private static String writeWsdlfile(String type, String busNo, String filename, String content){
		if(StringUtils.isBlank(busNo) || StringUtils.isBlank(filename) || StringUtils.isBlank(content)){
			return null;
		}

		int index = filename.lastIndexOf(".");
		String suffix = "";
		String name = filename;
		if(index>-1){
			suffix = filename.substring(index, filename.length());
			name = filename.substring(0, index).split("-")[0];
		}

		//是否可写开关
		Prop prop = PropKit.use("wsdlfile.properties");
		boolean write = true;
		if(prop!=null && prop.containsKey(name)){
			write = prop.getBoolean(name);
		}
		if(!write){
			return null;
		}

		String path = "";
        if(busNo.startsWith("2")){
            if("trade".equals(type)){
                path = WSDL_PATH+"/trade/"+busNo.substring(0,4)+"/"+busNo.substring(4,6)+"/"+busNo.substring(6,8)+"/"+busNo;
            }else{
                path = WSDL_PATH+"/"+type+"/"+busNo.substring(0,4)+"/"+busNo.substring(4,6)+"/"+busNo.substring(6,8);
            }
        }else {
            if("trade".equals(type)){
                path = WSDL_PATH+"/trade/"+busNo.substring(0,2)+"/"+busNo.substring(2,4)+"/"+busNo.substring(4,6)+"/"+busNo;
            }else{
                path = WSDL_PATH+"/"+type+"/"+busNo.substring(0,2)+"/"+busNo.substring(2,4)+"/"+busNo.substring(4,6);
            }
        }


		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}

		try {
			File file = new File(path+"/"+filename);
			//重复报文
			if(file.exists()){
				filename = filename.replaceFirst(suffix+"$", "") + "-" + new SimpleDateFormat("HHmmss.S").format(new Date()) + suffix;
				file = new File(path+"/"+filename);
			}

			file.createNewFile();
			FileOutputStream out = new FileOutputStream(file);
			out.write(content.getBytes("utf-8"));
			out.flush();
			out.close();
			return file.getAbsolutePath();
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * 追加写文件
	 * @param filePath 文件名（带路径）
	 * @param content 文件内容
	 */
	public static String appendFile(String filePath, String content) throws Exception{
		File file = new File(filePath);
		if(!file.exists()){
			File base = file.getParentFile();
			if(!base.exists()){
				base.mkdirs();
			}
			file.createNewFile();
		}

		FileWriterWithEncoding writer = null;
		try {
			// 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
			writer = new FileWriterWithEncoding(filePath, "utf-8", true);
			writer.write(content);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(writer != null){
					writer.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return file.getAbsolutePath();
	}
	
    /**
     * 写文件
     * @param basePath 文件根路径（子路径默认 年/月/日）
     * @param filename 文件名
     * @param content 文件内容
     */
    public static String writeFile(String basePath, String filename, String content){
        File base = new File(basePath);
        File path = new File(base.getAbsolutePath()+String.format("/%1$tY/%1$tm/%1$td/", new Date()));
        if(!path.exists()){
            path.mkdirs();
        }
        try {
        	File file = new File(path.getAbsolutePath()+File.separator+filename);
        	if(file.exists()){
        		String[] tmps = filename.split("\\.");
        		String suffix = tmps.length>1 ? "."+tmps[tmps.length-1] : "";
        		filename = filename.replaceFirst(suffix+"$", "") + "-" + new SimpleDateFormat("HHmmss.S").format(new Date()) + suffix;
        		file = new File(path.getAbsolutePath()+File.separator+filename);
        	}
        	file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(content.getBytes("utf-8"));
            out.flush();
            out.close();
            return file.getAbsolutePath();
		} catch (Exception e) {
			return null;
		}
    }
    
    /**
     * 写文件
     * @param filePath 文件绝对路径
     * @param content 文件内容
     * @throws IOException 
     */
    public static String writeFile(String filePath, String content) throws IOException{
    	File file = new File(filePath);
        if(!file.exists()){
            File base = file.getParentFile();
            if(!base.exists()){
                base.mkdirs();
            }
            file.createNewFile();
        }
        
        try {
            file.createNewFile();
            FileOutputStream out = new FileOutputStream(file);
            out.write(content.getBytes("utf-8"));
            out.flush();
            out.close();
            return file.getAbsolutePath();
		} catch (Exception e) {
			return null;
		}
    }
    
	/**
	 * 按行读文件
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static List<String> readFileByLine(String path) throws IOException{
		List<String> list = new ArrayList<String>();
        FileReader reader = new FileReader(path);
        BufferedReader br = new BufferedReader(reader);
        String line = null;
       
        while((line = br.readLine()) != null) {
        	list.add(line);
        }
       
        br.close();
        reader.close();
        return list;
	}
	
	/**
	 * 按行读文件
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readFile(String path) throws IOException{
		FileReader reader = new FileReader(path);
		BufferedReader br = new BufferedReader(reader);
		String line = null;
		StringBuffer result = new StringBuffer();
        while((line = br.readLine()) != null) {
        	result.append(line);
        }
        br.close();
        reader.close();
        return result.toString();
	}
	
	/**
	 * 读文件，解决部分中文乱码
	 * @param path
	 * @return
	 * @throws IOException
	 */
	public static String readFileByInputStream(String path) throws IOException{
		InputStream in = new FileInputStream(path);
		InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
		BufferedReader reader = new BufferedReader(inReader);
		String line = null;
		StringBuffer sb = new StringBuffer(); 
		while ((line = reader.readLine()) != null) {
			sb.append(line).append("\n");
		}
		reader.close();
		inReader.close();
		return sb.toString();
	}
	
	/**
	 * 格式化输出xml
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static String formatXML(String xml){
		try {
			SAXReader reader = new SAXReader();
			InputStream ins = new ByteArrayInputStream(xml.getBytes());
			Document doc = reader.read(new BufferedReader(new InputStreamReader(ins, System.getProperty("file.encoding"))));
	        StringWriter out = null;
	        OutputFormat formate = OutputFormat.createPrettyPrint();
	        out = new StringWriter();
	        XMLWriter writer = new XMLWriter(out,formate);
	        writer.write(doc);
	        out.close();
	        writer.close();
	        return out.toString();
		} catch (Exception e) {
			return xml;
		}
    }

	/**
	 * 计算文件指纹
	 * @param file 上传的文件
	 * @return null-出错
	 */
	public static String getFileCode(UploadFile file){
		return getFileCode(file, FileUtil.ALGORITHM);
	}

	public static String getFileCode(UploadFile file, String algorithm){
		if(file == null){
			return null;
		}

		String code = null;
		try {
			code = getFileCode(new FileInputStream(file.getFile()), algorithm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return code;
	}

	/**
	 * 计算文件指纹
	 * @param file 上传的文件
	 * @return null-出错
	 */
	public static String getFileCode(File file){
		return getFileCode(file, FileUtil.ALGORITHM);
	}

	public static String getFileCode(File file, String algorithm){
		if(file == null){
			return null;
		}

		String code = null;
		try {
			code = getFileCode(new FileInputStream(file), algorithm);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return code;
	}

	public static String getFileCode(InputStream in) {
		return getFileCode(in, FileUtil.ALGORITHM);
	}

	public static String getFileCode(InputStream in, String algorithm) {
		if(in == null){
			return null;
		}

		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance(algorithm);
			byte[] buffer = new byte[1024 * 1024 * 10];
			int len = 0;

			while ((len = in.read(buffer)) > 0) {
				messageDigest.update(buffer, 0, len);
			}
		} catch (Exception e) {
			return null;
		} finally {
			if(in != null){
				try {
					in.close();
				} catch (IOException e) {
					return null;
				}
			}
		}
		return messageDigest == null ? null : byte2hex(messageDigest.digest());
	}

	/**
	 * 将二进制数组转换成16进制（大写）
	 * @param array
	 * @return
	 */
	public static String byte2hex(byte[] array) {
		if (array == null || array.length == 0) {
			return null;
		}

		StringBuffer stringBuffer = new StringBuffer();
		for (byte num : array) {
			String temp = Integer.toHexString(num & 0xFF);
			if (temp.length() < 2) {
				stringBuffer.append(0);
			}
			stringBuffer.append(temp);
		}
		return stringBuffer.toString().toUpperCase();
	}

	/**
	 * 复制文件
	 * @param oldPath
	 * @param newPath
     */
	public static synchronized void copy(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if(oldfile.exists()){
				File parent = new File(newPath).getParentFile();
				if(!parent.exists()){
					parent.mkdirs();
				}
				InputStream in = new FileInputStream(oldPath);
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				while ( (byteread = in.read(buffer)) != -1) {
					bytesum += byteread;
					fs.write(buffer, 0, byteread);
				}
				in.close();
			}
		}
		catch (Exception e) {}
	}

	/**
	 * 写xls的导出文件
	 * @param file
	 * @param book
	 * @return
	 */
	public static File writeXLS(File file, XSSFWorkbook book){
		try {
			FileOutputStream fileOutputStreane = new FileOutputStream(file);
			book.write(fileOutputStreane);
			fileOutputStreane.flush();
			fileOutputStreane.close();
			file.deleteOnExit();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return file;
	}

	/**
	 * 根据配置文件转换excel头的参数key
	 * @param sheet
	 * @return
     */
	public static Map<Integer, String> getHeadKey(XSSFSheet sheet){
		XSSFRow row = sheet.getRow(0);
		short num = row.getLastCellNum();
		Prop prop = PropKit.use("depot/xlsx.txt");
		Map<Integer, String> keys = new HashMap<>();
		for(short i=0; i<num; i++){
			XSSFCell cell = row.getCell(i);
			int index = cell.getColumnIndex();
			String key = cell.getStringCellValue();
			keys.put(index, prop.get(key));
		}
		return keys;
	}

	/**
	 * 获取文件的ContentType
	 * @param path
	 * @return
	 */
	public static String getFileContentType(String path) {
		if(StringUtils.isBlank(path)){
			return null;
		}
		return FileUtil.getFileContentType(new File(path));
	}

	/**
	 * 获取文件的ContentType
	 * @param file
	 * @return
	 */
	public static String getFileContentType(File file) {
		try {
			return Magic.getMagicMatch(file, false).getMimeType();
		} catch (Exception e) {
			//e.printStackTrace();
		}
		return "";
	}


	public static void downloadFile(String remoteFilePath, String localFilePath) {
		URL urlfile = null;
		HttpURLConnection httpUrl = null;
		BufferedInputStream bis = null;
		BufferedOutputStream bos = null;
		File f = new File(localFilePath);
		try {
			urlfile = new URL(remoteFilePath);
			httpUrl = (HttpURLConnection)urlfile.openConnection();
			httpUrl.connect();
			bis = new BufferedInputStream(httpUrl.getInputStream());
			bos = new BufferedOutputStream(new FileOutputStream(f));
			int len = 2048;
			byte[] b = new byte[len];
			while ((len = bis.read(b)) != -1)
			{
				bos.write(b, 0, len);
			}
			bos.flush();
			bis.close();
			httpUrl.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				bis.close();
				bos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
