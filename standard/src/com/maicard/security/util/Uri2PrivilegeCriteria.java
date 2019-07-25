package com.maicard.security.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.maicard.common.util.NumericUtils;
import com.maicard.security.criteria.PrivilegeCriteria;
import com.maicard.standard.Operate;

/**
 * 把请求URI转换为权限查询条件
 *
 *
 * @author NetSnake
 * @date 2015年12月26日
 *
 */
public class Uri2PrivilegeCriteria {
	
	protected static final Logger logger = LoggerFactory.getLogger(Uri2PrivilegeCriteria.class);
	
	

	public static PrivilegeCriteria convert(HttpServletRequest request){
		PrivilegeCriteria privilegeCriteria = new PrivilegeCriteria();
		String uri = request.getRequestURI();
		String[] requestPath = uri.split("/");
		if(requestPath == null || requestPath.length  < 2){
			logger.warn("当前请求的URI为空或长度不足:" + uri);
			return null;
		}
		//过滤掉可能的文件后缀
		String objectTypeCode = requestPath[1].replaceAll("\\.\\w*$", "");
		/*
		 * 如果请求URI只有两部分
		 * 那么最后一部分就是操作对象的代码objectTypeCode
		 * HTTP请求方式GET/POST就是操作代码privilegeCode
		 * GET就是list，POST就是create
		 */
		if(requestPath.length == 2){
			privilegeCriteria.setObjectTypeCode(objectTypeCode);
			if(request.getMethod().equals("POST")){
				privilegeCriteria.setOperateCode(Operate.create.getCode());
			} else {
				privilegeCriteria.setOperateCode(Operate.list.getCode());			
			}
			logger.debug("当前请求[" + uri + "]解析为:对对象" + privilegeCriteria.getObjectTypeCode() + "进行" + privilegeCriteria.getOperateCode() + "操作");
			return privilegeCriteria;
		}
		/*
		 * 如果URI请求有三部分
		 * 那么第2部分就是操作对象的代码objectTypeCode
		 * 第3部分只能是GET请求来获取页面
		 */
		if(requestPath.length == 3){
			privilegeCriteria.setObjectTypeCode(objectTypeCode);
			boolean isValidOperator = false;
			String opereateCode = requestPath[2].replaceAll("\\.\\w*", "");
			if(OperateUtils.getOperateEnum().contains(opereateCode)){
				isValidOperator = true;
			}
			/*for(String op : operateEnum){
				if(op.equals(opereateCode)){
					isValidOperator = true;
					break;
				}
			}*/
			if(!isValidOperator){
				logger.debug("操作码[" + opereateCode + "]不合法");
				return null;
			}
			privilegeCriteria.setOperateCode(opereateCode);
			logger.debug("当前请求[" + uri + "]解析为:对对象" + privilegeCriteria.getObjectTypeCode() + "进行" + privilegeCriteria.getOperateCode() + "操作");
			return privilegeCriteria;

		}
		/*
		 * URI请求有四个部分
		 * 第2部分：是操作对象的代码objectTypeCode
		 * 第3部分：是操作代码operateCode/privilegeCode
		 * 第4部分：二级操作码或操作对象的ID
		 */
		logger.info(requestPath[0]+"|"+requestPath[1]+"|"+requestPath[2]+"|"+requestPath[3]);
		privilegeCriteria.setObjectTypeCode(objectTypeCode);
		boolean isValidOperator = false;
		for(Operate op : Operate.values()){
			if(op.getCode().equals(requestPath[2])){
				isValidOperator = true;
			//	logger.info(isValidOperator+"有权限！！！");
				break;
			}
		}
		if(!isValidOperator){
			logger.warn("错误的操作码:" + requestPath[2]);
			return null;
		}
		privilegeCriteria.setOperateCode(requestPath[2]);
		String targetObject = requestPath[3].replaceAll("\\.\\w*", "");
		if(NumericUtils.isNumeric(targetObject)){
			//最后一部分是请求的一个对象的ID
			privilegeCriteria.setObjectId(targetObject);
			logger.debug("当前请求[" + uri + "]解析为:对对象" + privilegeCriteria.getObjectTypeCode() + "#" + privilegeCriteria.getObjectId() + "进行" + privilegeCriteria.getOperateCode() + "操作");
		} else {
			//最后一部分呢时请求对象的某个属性
			privilegeCriteria.setObjectId("self");
			privilegeCriteria.setObjectAttribute(targetObject);	
			logger.debug("当前请求[" + uri + "]解析为:对对象" + privilegeCriteria.getObjectTypeCode() + "的属性" + privilegeCriteria.getObjectAttribute() + "进行" + privilegeCriteria.getOperateCode() + "操作");
		}
		/*
		String objectId = "";
		try{
			objectId =requestPath[3].replaceAll("\\.\\w*", "");
		}catch(Exception e){
			e.printStackTrace();
		}
		if(objectId ==""){
			return null;
		}
		privilegeCriteria.setObjectId(objectId);*/
		return privilegeCriteria;
	}

}
