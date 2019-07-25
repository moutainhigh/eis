package com.maicard.site.service.impl;

import java.io.*;


/**
 * 将PDF文件转换为SWF文件
 * @author NetSnake
 *
 */

public class Pdf2SwfConvertServiceImpl {
	public static String SEPARATOR = System.getProperty("file.separator");
	public static String OS = System.getProperty("os.name");
	public static String basePath;
	public static String pdf_incoming_path;
	public static String pdf_processed_path;
	public static String swf_path;
	public static long SLEEP_TIME = 5000;
	public static String COVERT_COMMAND;
	static {
		if(OS.startsWith("Windows")){
			COVERT_COMMAND = "D:/pdf2swf.exe";
		} else {
			COVERT_COMMAND = "/usr/local/bin/pdf2swf";			
		}
		
	}

	public static void main(String[] argv){
		if(argv.length != 1){
			System.err.println("Usage:\njava " + Pdf2SwfConvertServiceImpl.class.getName() + " base_process_dir(like /var/tomcats/tomcat1/webapps/exhibit/data)\nIf you want it run as daemon on linux, use\nnohup java " + Pdf2SwfConvertServiceImpl.class.getName() + " base_process_dir &");
			//basePath = "D:/NetSnake/Source/CFIEX/exhibit-2.1-cfiex/WebContent/data";
			return;
		}
		File covert = new File(COVERT_COMMAND);
		if(!covert.exists()){
			System.out.println("Covert tool[" + COVERT_COMMAND + "] not exist!");
			return;
		}
		if(!covert.canExecute()){
			System.out.println("Covert tool[" + COVERT_COMMAND + "] not executable!");
			return;
		}
		basePath = argv[0];
		pdf_incoming_path = basePath + SEPARATOR + "pdf_incoming";
		pdf_processed_path = basePath + SEPARATOR + "pdf_processed";
		swf_path = basePath;
		System.out.println("Starting checking coverting at path:" + pdf_incoming_path +", processed file put to:" + pdf_processed_path + ", swf file put to:" + swf_path + ", sleep each " + SLEEP_TIME + "ms, Running on " + OS);
		while(true){
			File incoming_dir = new File(pdf_incoming_path);
			File[] newPdf = incoming_dir.listFiles();
			System.out.println("Found " + newPdf.length + " files in dir.");
			for(int i=0;i<newPdf.length;i++){
				if(newPdf[i].isFile() && newPdf[i].getName().endsWith("pdf")){
					//开始处理PDF文件
					pdf2swf(newPdf[i]);
				}
			}
			try{
			Thread.sleep(SLEEP_TIME);
			}catch(Exception e){}
		}
	}

	private static boolean pdf2swf(File source){
		String sourceFileName = source.getAbsolutePath();
		String destFileName = swf_path + SEPARATOR + source.getName().replaceAll("\\.pdf$", "");
		String destFilePattern = destFileName + SEPARATOR;
		File processedFile = new File(pdf_processed_path + SEPARATOR + source.getName());
		File destDir = new File(destFileName);
		
		File file = new File(pdf_processed_path + SEPARATOR + source.getName());
		if(file.exists()&&file.isFile()){
			file.delete();
		}
		

		long  etaTime =  source.length() / 1024 / 1024 * 30;
		System.out.println("Starting coverting source file:" + sourceFileName + " to " + destFilePattern + ", eta:" + etaTime +"s.");
		if(!destDir.exists()){
			if(!destDir.mkdir()){
				System.out.println("Create directory[" + destDir.getAbsolutePath() + "] failed!");
				return false;
			}
		}
		
//		String name  = source.getName().substring(source.getName().lastIndexOf("_")+1);
//		int maxPage = Integer.valueOf(name.substring(0,name.lastIndexOf(".")));
//		int upPage = 1;
//		int lastPage = 5;
//		int i = 1;
		String command = null;
//		while(true ){
//			while(lastPage > maxPage){
//				lastPage = lastPage -1;
//			}
//			if((maxPage -lastPage ) < 5){
//				lastPage = maxPage;
//			}
			 command = COVERT_COMMAND + " " + sourceFileName + " -o " + destFilePattern + "%" +".swf" + " -T 9";
		      System.out.println("Run command:" + command);
				try{
					long startTime = System.currentTimeMillis();
					Process p = Runtime.getRuntime().exec(command);
					BufferedReader br = null;
					br = new BufferedReader(new InputStreamReader(p.getInputStream(), "utf-8"));
					String line = "";
					while((line = br.readLine()) != null){
						if(!line.trim().equals(""))
						System.out.println(line);
					}
			
					
//					p.waitFor();
//					p.destroy();
//					int rt = p.exitValue();
					long processTime = System.currentTimeMillis() - startTime;
					System.out.println("Cover completed, return:" +", use time:" + processTime / 1000 + "s.");
					//Thread.sleep(etaTime * 1000);
				}catch(Exception e){}
//			 if(lastPage == maxPage){
//				break;
//			}
//			upPage = upPage +5;
//			lastPage = lastPage + 5;
//			++i;
//		
//		}
		
		//如果对应目录下已经生成了对应的SWF，那么就将此PDF挪到pdf_processed目录
		if(!destDir.exists()){
			//文件转换未成功
			System.out.println("Covert file[" + sourceFileName + "] failed: directory not exist!");
			return false;
		}
		int createdFileCount = destDir.list().length;
		System.out.println(createdFileCount  + " swf files created");
		if(createdFileCount < 1){
			//文件转换未成功
			System.out.println("Covert file[" + sourceFileName + "] failed: dest dir[" + destDir.getAbsolutePath() + " sub-file count:" + createdFileCount);
			return false;
		}
		System.out.println("Move file[" + sourceFileName + "] to [" + processedFile.getAbsolutePath() + "].");
		
		if(source.renameTo(processedFile)){
			System.out.println("Move success, covert process compelete.");
		} else {
			System.out.println("Move failed.");
			
		}
		return true;
	}
	


}
