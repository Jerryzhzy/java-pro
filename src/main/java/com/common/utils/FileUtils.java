package com.common.utils;


import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.parser.PdfReaderContentParser;
import com.itextpdf.text.pdf.parser.SimpleTextExtractionStrategy;
import com.itextpdf.text.pdf.parser.TextExtractionStrategy;
import com.mortennobel.imagescaling.ResampleOp;
import net.sf.json.JSONObject;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.POIXMLTextExtractor;
import org.apache.poi.hslf.extractor.PowerPointExtractor;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.openxml4j.exceptions.OpenXML4JException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.xslf.extractor.XSLFPowerPointExtractor;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.xmlbeans.XmlException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;
import java.text.*;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * 
 * @author ziyu.zhang
 * @source IBM
 * 
 */
@SuppressWarnings("rawtypes")
public class FileUtils {
	
	public static String dirSplit = "\\";//linux windows

    /**
     * 处理图片并保存  一张原图和一张缩小后的图片  小图可用于手机端 
     * @param upload             大图对象 
     * @param uploadFileName     图片原名 
     * @param webPath            工程部署的绝对地址 
     * @param filePath           图片目录 
     * @param type               1原文件未存储，同时做原文件存储和压缩处理  2表示 原文件已经存储，只做缩略处理 
     * @return   为一字符数字，0位置 为原图片位置，1位置为压缩后图片位置，2位置为压缩后图片高度，3位置为压缩后图片宽度，4位置为压缩后图片大小 
     */  
   public static String[] uploadImages(File upload, String uploadFileName,String webPath,String filePath,String type) {  
  
        StringTokenizer tokenizer = new StringTokenizer(uploadFileName, ".");  
        String ext="";  
        while(tokenizer.hasMoreTokens()){  
            ext = tokenizer.nextToken();  
        }  
        
        String filename = "";
        //文件存储前
        if("1".equals(type)){
        	//大图的名字  
            filename = getNewName(uploadFileName);
            //保存大图  
    	    if(!saveFile(upload,webPath,filePath,filename,uploadFileName)){
    	        return null;  
    	    }  
        }else{
        	filename = uploadFileName;  
        }
              
        String afterFileName = getNewName(uploadFileName);
        //小图的名字  
        String smallname =   afterFileName;
        String smallPath = webPath + filePath + smallname;   
        // 产生一张新的截图  
        int width = 150;
        int height = 150;
        try {
			width = ConfigUtil.getIntValue("image.width");
			height = ConfigUtil.getIntValue("image.height");
		} catch (Exception e) {
        	e.printStackTrace();
		}
        
        String[] fileinfo =  resizeImage(upload, smallPath,width,height,ext);  
        if(null == fileinfo){  
            return new String[]{"/" + filePath + filename,"/" + filePath + filename,  
                     null,null,null};  
        }else{  
            return new String[]{"/" + filePath + filename,"/" + filePath + smallname,  
                                 fileinfo[0],fileinfo[1],fileinfo[2]};  
        }  
    }
   
   /** 
    * 接收File输出图片 
    * 以图片高为标准 按比例缩减图片 
    * @param file 
    *            原图片对象 
    * @param writePath 
    *            小图片存放的路径 
    * @param width 
    *            宽 
    * @param height 
    *            高 
    * @param format 
    *            图片格式 
    * @return 
    */  
   public static String[]  resizeImage(File file, String writePath,  
           Integer width, Integer height, String format) {  
         
       try {  
           BufferedImage inputBufImage = ImageIO.read(file);  
           inputBufImage.getType();  
             
           System.out.println("转前图片高度和宽度：" + inputBufImage.getHeight() + ":"  
                   + inputBufImage.getWidth());  
           if(height >=inputBufImage.getHeight() || width >= inputBufImage.getWidth()){  
                 
               return null;  
           }else{  
           //double dd = inputBufImage.getHeight() / height;  
           //width = (int) (inputBufImage.getWidth() / dd);  
           //height = (int) (inputBufImage.getHeight() / dd);  
           // 转换  
           ResampleOp resampleOp = new ResampleOp(width, height);  
           BufferedImage rescaledTomato = resampleOp.filter(inputBufImage,null);  
           File fil = new File(writePath);  
           ImageIO.write(rescaledTomato, format,fil);  
           System.out.println("转后图片高度和宽度：" + rescaledTomato.getHeight() + ":"  
                   + rescaledTomato.getWidth());  
           return new String[]{rescaledTomato.getHeight()+"",rescaledTomato.getWidth()+"",fil.length()+""};  
           }  
       } catch (IOException e) {  
           e.printStackTrace();  
           return null;  
       }  
 
   }  
    public static String getNewName(){
    	SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000);
		return newFileName;
    }
	public static String getNewName(String fileName){
		String fileExt = fileName.substring(
				fileName.lastIndexOf(".") + 1).toLowerCase();
		String newFileName = fileName.substring(0,fileName.lastIndexOf(".")).replace(" ","")+
				new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) +
				"_" + new Random().nextInt(1000) + "." + fileExt;

		return newFileName;
	}
	
    
	/**
	 * save file accroding to physical directory infomation
	 * 
	 * @param istream
	 *            input stream of destination file
	 * @return
	 */

	public static boolean SaveFileByPhysicalDir(String physicalPath,
			InputStream istream) {
		
		boolean flag = false;
		try {
			OutputStream os = new FileOutputStream(physicalPath);
			int readBytes = 0;
			byte buffer[] = new byte[8192];
			while ((readBytes = istream.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, readBytes);
			}
			os.close();
			flag = true;
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
		
			e.printStackTrace();
		}
		return flag;
	}
	
	public static boolean CreateDirectory(String dir){
		File f = new File(dir);
		if (!f.exists()) {
			f.mkdirs();
		}
		return true;
	}
	
	
	public static void saveAsFileOutputStream(String physicalPath,String content) {
		  File file = new File(physicalPath);
		  boolean b = file.getParentFile().isDirectory();
		  if(!b){
			  File tem = new File(file.getParent());
			  // tem.getParentFile().setWritable(true);
			  tem.mkdirs();// 创建目录
		  }
		  //Log.info(file.getParent()+";"+file.getParentFile().isDirectory());
		  FileOutputStream foutput =null;
		  try {
			  foutput = new FileOutputStream(physicalPath);
			  
			  foutput.write(content.getBytes("UTF-8"));
			  //foutput.write(content.getBytes());
		  }catch(IOException ex) {
			  ex.printStackTrace();
			  throw new RuntimeException(ex);
		  }finally{
			  try {
				  foutput.flush();
				  foutput.close();
			  }catch(IOException ex){
				  ex.printStackTrace();
				  throw new RuntimeException(ex);
			  }
		  }
		  	//Log.info("文件保存成功:"+ physicalPath);
		 }
	private static final int BUFFER_SIZE = 16 * 1024;
	
	
	/**
	 * 将文件保存到制定位置，路径不存在自动创建
	 * 
	 * @param file
	 *            要保存的文件
	 * @param webPath
	 *            工程部署的绝对路径
	 * @param filePath
	 *            文件夹的相对路径
	 * @param filename
	 *            文件名
	 * @return
	 */
	public static boolean saveFile(File file, String webPath, String filePath,
			String filename,String yfilename) {

		String delPath = webPath + filePath + "/" + yfilename;
		if (new File(webPath + filePath).exists()) {
			webPath = webPath + filePath + "/" + filename;
			File dstFile = new File(webPath);
			if (copy(file, dstFile)) {
				deleteFile(delPath);
				return true;
			}

		} else {
			if (new File(webPath + filePath).mkdirs()) {
				webPath = webPath + filePath + "/" + filename;
				File dstFile = new File(webPath);
				if (copy(file, dstFile)) {
					deleteFile(delPath);
					return true;
				}

			}

		}
		return false;

	}
	
	/**
	 * @Title:  文件上传存储服务器 （Base64字符串式 ，并缩略） 
	 * @Description: (文件上传存储 （Base64字符串式 ，并缩略）)
	 * @author: ziyu.zhang
	 * @date:   2015年11月26日 下午3:03:27 
	 * @param request HttpServletRequest
	 * @param fileStr base64 图片压缩字符串
	 * @param pack    存储文件夹名 见 config.properties配置
	 * @return 
	 * @return: String  
	 * @throws:
	 * Why & What is modified: <修改原因描述>
	 */
	@SuppressWarnings({ "deprecation", "rawtypes", "unused" })
	public static Map saveFile(HttpServletRequest request,String fileStr,String pack){
		Map<String,String> map = new HashMap<>();
		String result = "上传文件失败";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 根据服务器的文件保存地址找到项目部署的绝对地址
        String webPath =  request.getRealPath("/");  
        
        String filePath = pack+sdf.format(new Date())+"/"; 
		//文件保存目录路径
		String savePath = webPath + pack;
		//文件保存目录URL
		String saveUrl = request.getContextPath() + "/"+pack+sdf.format(new Date())+"/";
		
	    //如果目录不存在则创建
		if (!(new File(savePath).exists())) {			
			if (!(new File(savePath).mkdirs())) {
				result = "上传目录不存在";
				map.put("error", result);return map;
			}
		} 
		//检查目录写权限
		if (!(new File(savePath)).canWrite()) {
			result = "上传目录没有写权限";
			map.put("error", result);return map;
		}	
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		String newFileName = df.format(new Date()) + "_" + new Random().nextInt(1000) + ".png";
		//文件存储路径
		String path = getfileUrl(webPath, filePath, newFileName);
		if(path==null){
			result = "创建路径失败";
			map.put("error", result);return map;
		}
		//保存文件
		boolean isok = Base64ImageUtils.GenerateImage(fileStr, path);
		if(isok){
			//存储成功后 进行缩略图处理
			File file = new File(path);
			String[] str = uploadImages(file,newFileName,webPath,filePath,"2");
			
			JSONObject obj = new JSONObject();
			obj.put("url", str[0]);//原图
			obj.put("thumbnailUrl", str[1]);//缩略图
			obj.put("fileSize", file.length()+"");
			obj.put("fileName", newFileName);
			result = obj.toString();
			map.put("success", result);return map;
		}
		map.put("error", result);return map;
		
	}
	/**
	 * @Title:  文件上传存储服务器 （Http附件请求式）
	 * @Description: (文件上传 存储（Http请求式）)
	 * @author: ziyu.zhang
	 * @date:   2015年12月15日 下午1:45:58 
	 * @param request
	 * @param multipartRequest
	 * @param response
	 * @param pack          存储文件夹名 见 config.properties配置
	 * @param uploadfile    页面file签名 （如；页面表单file标签name）
	 * @return 
	 * @return: Map  
	 * @throws:
	 * Why & What is modified: <修改原因描述>
	 */
	
	public static Map saveFile(HttpServletRequest request,MultipartHttpServletRequest multipartRequest,
							   HttpServletResponse response,String pack,String uploadfile){
		//定义允许上传的文件扩展名
		String[] fileTypes = new String[] { "exe" };
		return uploadFile(request, multipartRequest, response, pack,uploadfile, fileTypes,null);
	}
	/**
	 * 
	 * @Description: TODO 消息公告中的图片上传
	 * @param request
	 * @param multipartRequest
	 * @param response
	 * @param pack
	 * @param uploadfile
	 * @return   
	 * @return Map  
	 * @throws
	 * @author xiaopeng.ma
	 * @date 2016年9月18日 下午4:30:49
	 */
	public static Map ckeditorUpload(HttpServletRequest request,MultipartHttpServletRequest multipartRequest,
			HttpServletResponse response,String pack,String uploadfile){
		//定义允许上传的文件扩展名
		String[] fileTypes = new String[] { "exe"};
		String fileTypesAllow = "jpg|jpeg|bmp|gif|png";
		return uploadFile(request, multipartRequest, response, pack,uploadfile, fileTypes,fileTypesAllow);
	}
	

	//上传文件
	private static Map uploadFile(HttpServletRequest request,
			MultipartHttpServletRequest multipartRequest,
			HttpServletResponse response, String pack, String uploadfile,
			String[] fileTypes, String fileTypesAllow) {
		Map<String,String> map = new HashMap<>();
		String result = "上传文件失败";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        // 根据服务器的文件保存地址找到项目部署的绝对地址
		String webPath =  request.getRealPath("/");
        String filePath = pack+sdf.format(new Date())+"/";
		//文件保存目录路径
		String savePath = webPath + filePath;
		//文件保存目录URL
		//String saveUrl = request.getContextPath() + "/"+pack+sdf.format(new Date())+"/";
		//最大文件大小
		long maxSize = 10000000;
		response.setContentType("text/html; charset=UTF-8");
		if (!ServletFileUpload.isMultipartContent(request)) {	
			result = "请选择文件";
			map.put("status", "error");
			map.put("message", result);
			return map;
			 
		}
		//如果目录不存在则创建
		if (!(new File(savePath).exists())) {			
			if (!(new File(savePath).mkdirs())) {
				result = "上传目录不存在";
				map.put("status", "error");
				map.put("message", result);
				return map;
			}
		} 
	
		//检查目录写权限
		if (!(new File(savePath)).canWrite()) {
			result = "上传目录没有写权限";
			map.put("status", "error");
			map.put("message", result);
			return map;
		}	
		
		MultipartFile file = multipartRequest.getFile(uploadfile);
		String filename = file.getOriginalFilename();  

		File source = new File(webPath+filePath+"/"+filename);//文件
		try {
			file.transferTo(source);
		} catch (IllegalStateException e1) {
			result = e1.getMessage();
			map.put("status", "error");
			map.put("message", result);
			return map;
		} catch (IOException e1) {
			result = e1.getMessage();
			map.put("status", "error");
			map.put("message", result);
			return map;
		}   
		
		String fileName = source.getName();
		if (source.isFile()) {	
			
			long fileSize = source.length();	
			
			//检查文件大小
			if (fileSize > maxSize) {
				result = "上传文件大小超过限制";
				map.put("status", "error");
				map.put("message", result);
				return map;
			}
			//检查扩展名
			String fileExt = fileName.substring(
					fileName.lastIndexOf(".") + 1).toLowerCase();
			//非空时，检查不允许的扩展名
			if (fileTypes!=null&&Arrays.<String> asList(fileTypes).contains(fileExt)) {
				result = "上传文件扩展名是不允许的扩展名";
				map.put("status", "error");
				map.put("message", result);
				return map;
			}
			//非空时，检查允许的扩展名
			if (fileTypesAllow!=null&&!Arrays.<String> asList(fileTypesAllow.split("\\|")).contains(fileExt)) {
				result = "格式错误，仅支持"+fileTypesAllow+"格式";
				map.put("status", "error");
				map.put("message", result);
				return map;
			}
			String newFileName = getNewName(fileName);
			//保存图片到硬盘
			FileUtils.saveFile(source, webPath, filePath, newFileName,filename);
		    result = filePath + newFileName;
		    map.put("url", result);
		    map.put("fileSize", fileSize+"");
		    map.put("fileName", newFileName);
		    map.put("status", "success");
		    map.put("message", "上传成功！");
			return map;
		}
		map.put("status", "error");
		map.put("message", result);
		return map;
	}
	public static List<Map<String,String>> saveFiles(HttpServletRequest request,MultipartHttpServletRequest multipartRequest, HttpServletResponse response,String pack,String uploadfile){
		List<Map<String,String>> maps = new ArrayList<>();
		Map<String,String> map = null;
		String result = "上传文件失败";
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		// 根据服务器的文件保存地址找到项目部署的绝对地址
		String webPath = request.getServletContext().getRealPath("/");
		String filePath = pack+sdf.format(new Date())+"/"; 
		//文件保存目录路径
		String savePath = webPath + pack;
		//文件保存目录URL
		//String saveUrl = request.getContextPath() + "/"+pack+sdf.format(new Date())+"/";
		//定义允许上传的文件扩展名
		//String[] fileTypes = new String[] { "gif", "jpg", "jpeg", "png", "bmp", "mp3", "MP3", "WAV", "wav", "WMA", "amr","zip" };
		String[] fileTypes = new String[] { "exe" };
		//最大文件大小
		long maxSize = 1000000000;
		response.setContentType("text/html; charset=UTF-8");
		if (!ServletFileUpload.isMultipartContent(request)) {	
			result = "请选择文件";
			map = new HashMap<>();
			map.put("status", "error");
			map.put("message", result);
			maps.add(map);
			return maps;
			
		}
		//如果目录不存在则创建
		if (!(new File(savePath).exists())) {			
			if (!(new File(savePath).mkdirs())) {
				result = "上传目录不存在";
				map = new HashMap<>();
				map.put("status", "error");
				map.put("message", result);
				maps.add(map);
				return maps;
			}
		} 
		
		//检查目录写权限
		if (!(new File(savePath)).canWrite()) {
			result = "上传目录没有写权限";
			map = new HashMap<>();
			map.put("status", "error");
			map.put("message", result);
			maps.add(map);
			return maps;
		}	
		
	    List<MultipartFile> files = multipartRequest.getFiles(uploadfile);
		for (MultipartFile file : files) {
			map = new HashMap<>();
	    	String filename = file.getOriginalFilename();  
	    	String contentType = file.getContentType();

	    	File source = new File(filename.toString());//文件
	    	try {
	    		file.transferTo(source);
	    	} catch (IllegalStateException e1) {
	    		result = e1.getMessage();
				map = new HashMap<>();
				map.put("status", "error");
				map.put("message", result);
				maps.add(map);
				return maps;
	    	} catch (IOException e1) {
	    		result = e1.getMessage();
				map = new HashMap<>();
				map.put("status", "error");
				map.put("message", result);
				maps.add(map);
				return maps;
	    	}   
	    	
	    	String fileName = source.getName();
	    	if (source.isFile()) {	
	    		
	    		long fileSize = source.length();	
	    		
	    		//检查文件大小
	    		if (fileSize > maxSize) {
	    			result = "上传文件大小超过限制";
					map = new HashMap<>();
					map.put("status", "error");
					map.put("message", result);
					maps.add(map);
					return maps;
	    		}
	    		//检查扩展名
	    		String fileExt = fileName.substring(
	    				fileName.lastIndexOf(".") + 1).toLowerCase();
	    		if (Arrays.<String> asList(fileTypes).contains(fileExt)) {
	    			result = "上传文件扩展名是不允许的扩展名";
					map = new HashMap<>();
					map.put("status", "error");
					map.put("message", result);
					maps.add(map);
					return maps;
	    		}
				String newFileName = getNewName(fileName);
	    		//保存图片到硬盘
	    		FileUtils.saveFile(source, webPath, filePath, newFileName,filename);
	    		result = filePath + newFileName;
	    		//map.put("success"+files.indexOf(file), result);
	    		map.put("contentType", contentType);
				map.put("url", result);
				map.put("fileSize", fileSize+"");
				map.put("fileName", newFileName);
				map.put("status", "success");
				map.put("message", "上传成功！");
				maps.add(map);
	    	}else{
				map = new HashMap<>();
				map.put("status", "error");
				map.put("message", result);
				maps.add(map);
				return maps;
	    	}
		}
		return maps;
	}

	/**
	 *
	 * @param file
	 * @param webPath
	 * @param filePath
     * @return
     */
	public static String saveFile(String newFileName,MultipartFile file,String webPath,String filePath){
		String results = "";
		String filename = file.getOriginalFilename();
		String pathurl = webPath + filePath + "/" + filename;


		//如果目录不存在则创建
		if (!(new File(webPath + filePath).exists())) {
			if (!(new File(webPath + filePath).mkdirs())) {
				return "error";
			}
		}
		//检查目录写权限
		if (!(new File(webPath + filePath)).canWrite()) {
			return "error";
		}

		File source = new File(pathurl);//文件
		try {
			file.transferTo(source);
		} catch (IllegalStateException e1) {
			e1.printStackTrace();
			//
		} catch (IOException e1) {
			e1.printStackTrace();
			//
		}
		long maxSize = 1000000000;
		String fileName = source.getName();
		if (source.isFile()) {
			long fileSize = source.length();
			//检查文件大小
			if (fileSize > maxSize) {
				results = "size";
				return results;
			}
			//检查扩展名
			String fileExt = fileName.substring(
					fileName.lastIndexOf(".") + 1).toLowerCase();
			if ("exe".equals(fileExt)) {
				results = "fileext";
				return results;
			}
			//保存图片到硬盘
			boolean isok = FileUtils.saveFile(source, webPath, filePath,newFileName,filename);
			if (isok) {
				results = "ok";
				return results;
			}else{
				results = "error";
				return results;
			}
		}

		return "error";
	}


	/**
	 * 获取文件路径 不存在就创建
	 * 
	 * @param
	 * @param webPath
	 *            工程部署的绝对路径
	 * @param filePath
	 *            文件夹的相对路径
	 * @param filename
	 *            文件名
	 * @return
	 */
	public static String getfileUrl(String webPath, String filePath,
			String filename) {

		if (new File(webPath + filePath).exists()) {
			webPath = webPath + filePath + "/" + filename;
			return webPath;
		} else {
			if (new File(webPath + filePath).mkdirs()) {
				webPath = webPath + filePath + "/" + filename;
				return webPath;
			}

		}
		return null;
	}
	
	
	/**
	 * 把源文件对象复制成目标文件对象
	 * 
	 * @param src
	 *            源文件
	 * @param dst
	 *            目标文件
	 * @return
	 */
	public static boolean copy(File src, File dst) {
		boolean result = false;
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst),
					BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			int len = 0;
			while ((len = in.read(buffer)) > 0) {
				out.write(buffer, 0, len);
			}
			result = true;
		} catch (Exception e) {
			e.printStackTrace();
			result = false;
		} finally {
			if (null != in) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (null != out) {
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return result;
	}
	
	 /**
     * COPY文件
     * @param srcFile String
     * @param desFile String
     * @return boolean
     */
    public boolean copyToFile(String srcFile, String desFile) {
        File scrfile = new File(srcFile);
        if (scrfile.isFile() == true) {
            int length;
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(scrfile);
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            File desfile = new File(desFile);

            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(desfile, false);
            }
            catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }
            desfile = null;
            length = (int) scrfile.length();
            byte[] b = new byte[length];
            try {
                fis.read(b);
                fis.close();
                fos.write(b);
                fos.close();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            scrfile = null;
            return false;
        }
        scrfile = null;
        return true;
    }

    /**
     * COPY文件夹
     * @param sourceDir String
     * @param destDir String
     * @return boolean
     */
    public boolean copyDir(String sourceDir, String destDir) {
        File sourceFile = new File(sourceDir);
        String tempSource;
        String tempDest;
        String fileName;
        File[] files = sourceFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            fileName = files[i].getName();
            tempSource = sourceDir + "/" + fileName;
            tempDest = destDir + "/" + fileName;
            if (files[i].isFile()) {
                copyToFile(tempSource, tempDest);
            } else {
                copyDir(tempSource, tempDest);
            }
        }
        sourceFile = null;
        return true;
    }

    /**
     * 删除指定目录及其中的所有内容。
     * @param dir 要删除的目录
     * @return 删除成功时返回true，否则返回false。
     */
    public static boolean deleteDirectory(File dir) {
        File[] entries = dir.listFiles();
        int sz = entries.length;
        for (int i = 0; i < sz; i++) {
            if (entries[i].isDirectory()) {
                if (!deleteDirectory(entries[i])) {
                    return false;
                }
            } else {
                if (!entries[i].delete()) {
                    return false;
                }
            }
        }
        if (!dir.delete()) {
            return false;
        }
        return true;
    }
    
    
    
    /**
     * File exist check
     *
     * @param sFileName File Name
     * @return boolean true - exist<br>
     *                 false - not exist
     */
    public static boolean checkExist(String sFileName) {
       boolean result = false;
        try {
        	File f = new File(sFileName);
        	//if (f.exists() && f.isFile() && f.canRead()) {
		   if (f.exists() && f.isFile()) {
			   result = true;
		   } else {
			   result = false;
		   }
	    } catch (Exception e) {
	        result = false;
        }
        /* return */
        return result;
    }
   
    /**
     * Get File Size
     *
     * @param sFileName File Name
     * @return long File's size(byte) when File not exist return -1
     */
    public static long getSize(String sFileName) {
       
        long lSize = 0;
       
        try {
   File f = new File(sFileName);
           
            //exist
            if (f.exists()) {
    if (f.isFile() && f.canRead()) {
     lSize = f.length();
    } else {
     lSize = -1;
    }
            //not exist
            } else {
    lSize = 0;
            }
        } catch (Exception e) {
   lSize = -1;
        }
       
        /* return */
        return lSize;
    }
   
 /**
  * File Delete
  *
  * @param sFileName File Nmae
  * @return boolean true - Delete Success<br>
  *                 false - Delete Fail
  */
    public static boolean deleteFromName(String sFileName) {
  
	  boolean bReturn = true;
	  
	  try {
	   File oFile = new File(sFileName);
	   
	   //exist
	   if (oFile.exists()) {
	    //Delete File
	       boolean bResult = oFile.delete();
	       //Delete Fail
	       if (bResult == false) {
	    	   bReturn = false;
	       }
	   
	   //not exist
	   } else {
	    
	   }
	   
	  } catch (Exception e) {
	   bReturn = false;
	  }
	  //return
	  return bReturn;
    }
   
 /**
  * File Unzip
  *
  * @param sToPath  Unzip Directory path
  * @param sZipFile Unzip File Name
  */
 @SuppressWarnings("rawtypes")
public static void releaseZip(String sToPath, String sZipFile) throws Exception {
  
  if (null == sToPath ||("").equals(sToPath.trim())) {
   File objZipFile = new File(sZipFile);
   sToPath = objZipFile.getParent();
  }
  ZipFile zfile = new ZipFile(sZipFile);
  Enumeration zList = zfile.entries();
  ZipEntry ze = null;
  byte[] buf = new byte[1024];
  while (zList.hasMoreElements()) {

   ze = (ZipEntry) zList.nextElement();
   if (ze.isDirectory()) {
    continue;
   }

   OutputStream os =
    new BufferedOutputStream(
     new FileOutputStream(
      getRealFileName(sToPath, ze.getName())));
   InputStream is = new BufferedInputStream(zfile.getInputStream(ze));
   int readLen = 0;
   while ((readLen = is.read(buf, 0, 1024)) != -1) {
    os.write(buf, 0, readLen);
   }
   is.close();
   os.close();
  }
  zfile.close();
 }
 
 /**
  * getRealFileName
  *
  * @param  baseDir   Root Directory
  * @param  absFileName  absolute Directory File Name
  * @return java.io.File     Return file
  */
 @SuppressWarnings({ "rawtypes", "unchecked" })
private static File getRealFileName(String baseDir, String absFileName) throws Exception {
  
  File ret = null;

  List dirs = new ArrayList();
  StringTokenizer st = new StringTokenizer(absFileName, System.getProperty("file.separator"));
  while (st.hasMoreTokens()) {
   dirs.add(st.nextToken());
  }

  ret = new File(baseDir);
  if (dirs.size() > 1) {
   for (int i = 0; i < dirs.size() - 1; i++) {
    ret = new File(ret, (String) dirs.get(i));
   }
  }
  if (!ret.exists()) {
   ret.mkdirs();
  }
  ret = new File(ret, (String) dirs.get(dirs.size() - 1));
  return ret;
 }

 /**
  * copyFile
  *
  * @param  srcFile   Source File
  * @param  targetFile   Target file
  */
 @SuppressWarnings("resource")
static public void copyFile(String srcFile , String targetFile) throws IOException
  {
  
   FileInputStream reader = new FileInputStream(srcFile);
   FileOutputStream writer = new FileOutputStream(targetFile);

   byte[] buffer = new byte [4096];
   int len;

   try
   {
    reader = new FileInputStream(srcFile);
    writer = new FileOutputStream(targetFile);
   
    while((len = reader.read(buffer)) > 0)
    {
     writer.write(buffer , 0 , len);
    }
   }
   catch(IOException e)
   {
    throw e;
   }
   finally
   {
    if (writer != null)writer.close();
    if (reader != null)reader.close();
   }
  }

 private static boolean flag = false; 
 private static File file = null;
 /** 
  * 删除单个文件 
  * @param   sPath    被删除文件的文件名 
  * @return 单个文件删除成功返回true，否则返回false 
  */  
 public static boolean deleteFile(String sPath) {  
 	flag = false;  
 	file = new File(sPath);  
     // 路径为文件且不为空则进行删除  
     if (file.isFile() && file.exists()) {  
         file.delete();  
         flag = true;  
     }  
     return flag;  
 }  
 
 
 /**
  * renameFile
  *
  * @param  srcFile   Source File
  * @param  targetFile   Target file
  */
 static public void renameFile(String srcFile , String targetFile) throws IOException
  {
   try {
    copyFile(srcFile,targetFile);
    deleteFile(srcFile);
   } catch(IOException e){
    throw e;
   }
  }


 public static void write(String tivoliMsg,String logFileName) {
  try{
   byte[]  bMsg = tivoliMsg.getBytes();
   FileOutputStream fOut = new FileOutputStream(logFileName, true);
   fOut.write(bMsg);
   fOut.close();
  } catch(IOException e){
   //throw the exception      
  }

 }
 /**
 * This method is used to log the messages with timestamp,error code and the method details
 */
 public static void writeLog(String logFile, String batchId, String exceptionInfo) {
  
  DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.DEFAULT, Locale.JAPANESE);
  
  Object args[] = {df.format(new Date()), batchId, exceptionInfo};
  
  String fmtMsg = MessageFormat.format("{0} : {1} : {2}", args);
  
  try {
   
   File logfile = new File(logFile);
   if(!logfile.exists()) {
    logfile.createNewFile();
   }
   
      FileWriter fw = new FileWriter(logFile, true);
      fw.write(fmtMsg);
      fw.write(System.getProperty("line.separator"));

      fw.flush();
      fw.close();

  } catch(Exception e) {
  }
 }
	/**
	 * 读取txt内容 支持格式：txt
	 * Created by ziyu.zhang on 2017/11/29 16:59
	 * @Param	文件路径
	 * @return  文件内容
	 */
	public static String readTextFile(String realPath) {
		try {
			File file = new File(realPath);
			if (!file.exists()){
				System.out.println("File not exist!");
				return null;
		  }
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(realPath),"UTF-8"));
			String temp = "";
			String txt="";
			while((temp = br.readLine()) != null){
                txt+=temp;
             }
			br.close();
			return txt;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 读取txt内容 支持格式：txt
	 * Created by ziyu.zhang on 2017/11/29 16:59
	 * @Param	文件路径
	 * @return  文件内容
	 */
	public static String readTextFile1(String fileName) {
		String encoding = "gb2312";
		File file = new File(fileName);
		Long filelength = file.length();
		byte[] filecontent = new byte[filelength.intValue()];
		try {
			FileInputStream in = new FileInputStream(file);
			in.read(filecontent);
			in.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			return new String(filecontent, encoding);
		} catch (UnsupportedEncodingException e) {
			System.err.println("The OS does not support " + encoding);
			e.printStackTrace();
			return null;
		}
	}
	/**
	 * 读取word内容 支持格式：doc;docx
	 * Created by ziyu.zhang on 2017/11/29 16:59
	 * @Param	文件路径
	 * @return  文件内容
	 */
	public static String readWordFile(String realPath) {
		String result = "";
		try {
			if(realPath.endsWith(".doc")){
				InputStream is = new FileInputStream(new File(realPath));
				WordExtractor ex = new WordExtractor(is);
				result = ex.getText();
			}
			if(realPath.endsWith(".docx")){
				OPCPackage opcPackage = POIXMLDocument.openPackage(realPath);
				POIXMLTextExtractor extractor = new XWPFWordExtractor(opcPackage);
				result = extractor.getText();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	/**
	 * 读取pdf内容 支持格式：pdf
	 * Created by ziyu.zhang on 2017/11/29 16:59
	 * @Param	文件路径
	 * @return  文件内容
	 */
	public static String readPdfFile(String fileName) {
		try {
			PdfReader reader = new PdfReader(fileName);
			PdfReaderContentParser parser = new PdfReaderContentParser(reader);
			StringBuffer buff = new StringBuffer();
			TextExtractionStrategy strategy;
			for (int i = 1; i <= reader.getNumberOfPages(); i++) {
                strategy = parser.processContent(i,
                        new SimpleTextExtractionStrategy());
                buff.append(strategy.getResultantText());
            }
			return buff.toString();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 读取Excel文件内容 直接抽取幻灯片的全部内容 支持格式：ppt；pptx
	 * Created by ziyu.zhang on 2017/11/29 16:59
	 * @Param	文件路径
	 * @return  文件内容
	 */

	public static String readPPTFile(String realPath){
		File file = new File(realPath);
		try{
			FileInputStream fin=new FileInputStream(file);
			if(realPath.endsWith(".ppt") || realPath.endsWith(".PPT")){
				String cont=readPPT1(fin);
				fin.close();
				return cont;
			}
			if(realPath.endsWith(".pptx") || realPath.endsWith(".PPTX")){
				String cont=readPPT2(realPath);
				if(null!=cont){
					fin.close();
					return cont;
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	//直接抽取幻灯片的全部内容2007
	public static String readPPT2(String file){
		try {
			return new XSLFPowerPointExtractor(POIXMLDocument.openPackage(file)).getText();
		} catch (XmlException e) {
			e.printStackTrace();
		} catch (OpenXML4JException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	//直接抽取幻灯片的全部内容2003
	public static String readPPT1(InputStream is) throws IOException{
		PowerPointExtractor extractor=new PowerPointExtractor(is);
		return extractor.getText();
	}

	/**
	 * 读取Excel文件内如 支持格式：xls；xlsx
	 * Created by ziyu.zhang on 2017/11/29 16:59
	 * @Param	文件路径
	 * @return  文件内容
	 */
	public static String readExcelFile(String realPath){
		try {
			if(realPath.endsWith("xls")){
                return readEXCEL(realPath);
            }else{
				return readEXCEL2007(realPath);
            }
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;

	}

	// 读取xls文件
	public static String readEXCEL(String file) throws IOException {
		StringBuilder content = new StringBuilder();
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));// 创建对Excel工作簿文件的引用
		for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
			if (null != workbook.getSheetAt(numSheets)) {
				HSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
				for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
						.getLastRowNum(); rowNumOfSheet++) {
					if (null != aSheet.getRow(rowNumOfSheet)) {
						HSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
						for (short cellNumOfRow = 0; cellNumOfRow <= aRow
								.getLastCellNum(); cellNumOfRow++) {
							if (null != aRow.getCell(cellNumOfRow)) {
								HSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
								if (convertCell(aCell).length() > 0) {
									content.append(convertCell(aCell));
								}
							}
							content.append("\n");
						}
					}
				}
			}
		}
		return content.toString();
	}


	// 读取xlsx文件
	public static String readEXCEL2007(String file) throws IOException {
		StringBuilder content = new StringBuilder();
		XSSFWorkbook workbook = new XSSFWorkbook(file);
		for (int numSheets = 0; numSheets < workbook.getNumberOfSheets(); numSheets++) {
			if (null != workbook.getSheetAt(numSheets)) {
				XSSFSheet aSheet = workbook.getSheetAt(numSheets);// 获得一个sheet
				for (int rowNumOfSheet = 0; rowNumOfSheet <= aSheet
						.getLastRowNum(); rowNumOfSheet++) {
					if (null != aSheet.getRow(rowNumOfSheet)) {
						XSSFRow aRow = aSheet.getRow(rowNumOfSheet); // 获得一个行
						for (short cellNumOfRow = 0; cellNumOfRow <= aRow
								.getLastCellNum(); cellNumOfRow++) {
							if (null != aRow.getCell(cellNumOfRow)) {
								XSSFCell aCell = aRow.getCell(cellNumOfRow);// 获得列值
								if (convertCell(aCell).length() > 0) {
									content.append(convertCell(aCell));
								}
							}
							content.append("\n");
						}
					}
				}
			}
		}
		return content.toString();
	}


	private static String convertCell(Cell cell) {
		NumberFormat formater = NumberFormat.getInstance();
		formater.setGroupingUsed(false);
		String cellValue = "";
		if (cell == null) {
			return cellValue;
		}


		switch (cell.getCellType()) {
			case HSSFCell.CELL_TYPE_NUMERIC:
				cellValue = formater.format(cell.getNumericCellValue());
				break;
			case HSSFCell.CELL_TYPE_STRING:
				cellValue = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BLANK:
				cellValue = cell.getStringCellValue();
				break;
			case HSSFCell.CELL_TYPE_BOOLEAN:
				cellValue = Boolean.valueOf(cell.getBooleanCellValue()).toString();
				break;
			case HSSFCell.CELL_TYPE_ERROR:
				cellValue = String.valueOf(cell.getErrorCellValue());
				break;
			default:
				cellValue = "";
		}
		return cellValue.trim();
	}




	//遍历文件及文件夹
	public static void getFiles(String filePath,Map<String,String> filemap,int type){
		File root = new File(filePath);
		File[] files = root.listFiles();
		if(null!=files && files.length>0){
			Arrays.sort(files, new FileSorts.CompratorByLastModified());
			for(File file:files){

				if(file.isDirectory()){ //文件夹
					filemap.put(file.getName(),getFileSize(file));
					getFiles(file.getAbsolutePath(),filemap,type);//递归调用
				}else{	//文件
					if(1==type){
						filemap.put(file.getName(),getFileSize(file));
					}
				}
			}
		}
	}

	/**
	 * 获取最后一个版本包名
	 * Created by ziyu.zhang on 2016/4/29 14:00
	 * @Param
	 * @return
	 */
	public static String getLastFileDirectory(String filePath){
		File root = new File(filePath);
		File[] files = root.listFiles();
		if(null!=files && files.length>0){
			Arrays.sort(files, new FileSorts.CompratorByLastModified());
			File file = files[files.length-1];
			if(file.isDirectory()) { //文件夹
				return file.getName();
			}
		}
		return null;
	}

	/**
	 * 验证上一个版本包是否为空,或是否包含指定后缀名的文件
	 * Created by ziyu.zhang on 2016/4/29 13:59
	 * @Param ext 后缀名数组
	 * @return  为空或不包含指定格式文件 返回false
	 */
	public static boolean valLastFileDirectory(String filePath,String[] ext){
		File root = new File(filePath);
		File[] files = root.listFiles();
		if(null!=files && files.length>0){
			Arrays.sort(files, new FileSorts.CompratorByLastModified());
			if(files.length>=1){
				File file = files[files.length-1];
				if(file.isDirectory()){ //最后一个文件夹
					String path = filePath +"/"+ file.getName();//最后一个文件夹路径
					File root_ = new File(path);
					File[] files_ = root_.listFiles();
					if(null==files_ || files_.length<=0){
						return false;
					}else{
						//遍历最后一个文件夹下的所有文件
						for(File file_:files_){
							String fileName = file_.getName();
							String fileExt = fileName.substring(
									fileName.lastIndexOf(".") + 1).toLowerCase();
							if(Arrays.asList(ext).contains(fileExt)){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 获取指定后缀名的 最后一个文件名
	 * Created by ziyu.zhang on 2016/4/29 13:12
	 * @Param 
	 * @return 
	 */
	public static String getLastFile(String filePath,String[] ext){
		File root = new File(filePath);
		File[] files = root.listFiles();
		if(null!=files && files.length>0){
			Arrays.sort(files, new FileSorts.CompratorByLastModified());
			for(int i=files.length;i > 0;i--){
				File file = files[i-1];
				String fileName = file.getName();
				String fileExt = fileName.substring(
						fileName.lastIndexOf(".") + 1).toLowerCase();
				if(Arrays.asList(ext).contains(fileExt)){
					return file.getName();//文件名
				}
			}
		}
		return null;
	}
	//格式化文件大小
	public static String getformatFileSize(long fileSize){
		DecimalFormat df = new DecimalFormat("##0.00");
		if(fileSize >= 1024 * 1024 * 1024){
			return df.format(fileSize/(1024*1024*1024*1.0)) + " G";
		}else  if(fileSize >= 1024 * 1024){
			return df.format(fileSize/(1024*1024*1.0)) + " M";
		}else if(fileSize >= 1024){
			return df.format(fileSize/(1024*1.0)) + " KB";
		}
		return fileSize + " B";
	}

	//获取文件大小
	public static String getFileSize(File f){
		FileChannel fc = null;
		try {
			if (f.exists() && f.isFile()){
				FileInputStream fis= new FileInputStream(f);
				fc= fis.getChannel();
				long fileSize = fc.size();
				return getformatFileSize(fileSize);

			}else{
				//logger.info("file doesn't exist or is not a file");
			}
		} catch (FileNotFoundException e) {
			//logger.error(e);
		} catch (IOException e) {
			//logger.error(e);
		} finally {
			if (null!=fc){
				try{
					fc.close();
				}catch(IOException e){
					//logger.error(e);
				}
			}
		}
		return "";
	}

	public static void main(String[] args) throws Exception{
		String filePath = "C:\\MySQL 技术分享.pptx";
		String strs = readPPTFile(filePath);
		System.out.println(strs);

	}
	
 
}
