package com.maicard.mb.service;


/**
 * 定义用户有新消息时的动作
 * 
 * 由用户配置数据userConfig中的配置来决定如何处理收到的短消息和订阅消息
 * short_message_notify_site = true 则在站内提示用户有短消息[默认]
 * short_message_notify_sms = true 则向用户手机发送短消息提醒
 * short_message_notify_mail = true 则向用户邮箱发送消息提醒
 * 
 * subscribeMessageNotify???与上述一致，但针对订阅消息
 * 具体由哪个程序去处理，由名字为messageNotifyService???的bean决定
 * 例如：
 * config中的
 * message_notify_sms_bean决定了短消息的短信提示处理者
 * message_notify_mail_bean决定了短消息的邮件提示处理者
 * 不存在站内提示处理者，因为它必须由ajax访问指定的URL
 * 
 * @author NetSnake
 * @date 2012-10-1
 */
public interface MessageNotifyService {
	void send(com.maicard.common.domain.EisMessage eisMessage) throws Exception;

}
