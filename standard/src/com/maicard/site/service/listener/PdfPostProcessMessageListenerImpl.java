package com.maicard.site.service.listener;



import javax.annotation.Resource;
import javax.jms.*;

import org.springframework.stereotype.Service;

import com.maicard.common.base.BaseService;
import com.maicard.site.service.DocumentService;
import com.maicard.standard.Operate;

/**
 * PDF文档后期处理器，监听管理消息总线[com.maicard.mb.ManagementTopic]
 * 等待新增PDF文档的消息，并调用对应服务将该文档转换为SWF和JPG
 * @author NetSnake
 * @date 2012-9-15
 */

@Service
public class PdfPostProcessMessageListenerImpl extends BaseService implements MessageListener {

	@Resource
	private DocumentService documentService;

	@Override
	public void onMessage(Message message) {
		logger.info("PDF文档后期处理器从管理订购消息总线接收到消息");
		try{
			if(message instanceof ObjectMessage){
				ObjectMessage objectMessage = (ObjectMessage)message;
				int operate = 0;
				try{
					operate = objectMessage.getIntProperty("operate");
				}catch(Exception e){}
				if(operate == Operate.postProcess.getId()){
					documentService.postProcess(objectMessage);
					
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		
		//TODO

	}





}
