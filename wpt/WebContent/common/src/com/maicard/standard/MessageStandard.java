package com.maicard.standard;

import java.io.Serializable;

public interface MessageStandard extends Serializable {
	
	public static enum MessageLevel{
		system("system","系统数据消息"),
		user("user","用户级别消息");

		private final String code;
		private final String name;
		private MessageLevel(String code, String name){
			this.code = code;
			this.name = name;
		}
		public String getCode() {
			return code;
		}

		public String getName() {
			return name;
		}
		@Override
		public String toString(){
			return this.code;
		}	
	}

	//消息状态
	public static enum MessageStatus{
		unknown(0),
		/**
		 * ,"新消息"
		 */
		unread(140001),
		
		/**
		 * ,"草稿"
		 */
		draft(140002),
		
		/**
		 * ,"已读"
		 */
		readed(140003),
		
		/**
		 * ,"已删除"
		 */
		deleted(140004),
		
		/**
		 * ,"已发送"
		 */
		sent(140005),
		
		/**
		 * ,"等待发送"
		 */
		queue(140006);

		public final int id;
		private MessageStatus(int id){
			this.id = id;
		}
		public MessageStatus findById(int id){
			for(MessageStatus value: MessageStatus.values()){
				if(value.id == id){
					return value;
				}
			}
			return unknown;
		}
	}


	//用户消息发送模式
	public static enum UserMessageSendMethod{
		site, email, sms;
	}


}
