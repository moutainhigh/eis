package com.maicard.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SystemUtils {

	protected static final Logger logger = LoggerFactory.getLogger(SystemUtils.class);

	/**
	 * 执行系统命令<br>
	 * 如果createTempFileForCmdContent为true，那么cmd是一组shell命令的内容<br>
	 * 需要创建一个临时脚本并执行该脚本<br>
	 * 执行完成后会删除该临时脚本<br>
	 * 如果为false，则直接执行指定的cmd
	 * @param cmd
	 * @param createTempFileForCmdContent
	 * @return
	 */
	public static String execCommand(String cmd, boolean createTempFileForCmdContent){

		String fileName = null;
		if(createTempFileForCmdContent){
			fileName = FileUtils.getTempDirectoryPath() + File.separator + UUID.randomUUID().toString() + ".sh";

			File tempFile = new File(fileName);
			try {
				FileUtils.write(tempFile, cmd);
				tempFile.setExecutable(true);
			} catch (IOException e1) {
				e1.printStackTrace();
				logger.error("无法执行安装，因为无法写入程序文件:" + fileName);
				return null;
			} 
		} else {
			fileName = cmd;
		}
		String exec = "/bin/sh -c \"" + fileName + "\"";
		logger.debug("执行系统命令:" + exec + ",请求调用命令的内容是:" + cmd + ",是否生成临时命令文件:" + createTempFileForCmdContent);
		StringBuffer sb = new StringBuffer();
		try {
			Process p = Runtime.getRuntime().exec(exec);
			InputStream stderr = p.getInputStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			while ( (line = br.readLine()) != null){
				sb.append(line).append("\n");
			}
			int exitVal = p.waitFor();
			logger.debug("外部命令结果:" + exitVal + "，命令输出:" + sb.toString());
		} catch (Exception e) {
			logger.error("无法调用外部命令[" + exec + "]:" + e.getMessage());
			e.printStackTrace();
		}
		if(createTempFileForCmdContent){
			try {
				FileUtils.forceDelete(new File(fileName));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();

	
	}

}
