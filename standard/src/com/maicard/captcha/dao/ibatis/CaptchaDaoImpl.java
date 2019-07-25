package com.maicard.captcha.dao.ibatis;

import java.util.List;


import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import com.maicard.captcha.criteria.CaptchaCriteria;
import com.maicard.captcha.dao.CaptchaDao;
import com.maicard.captcha.domain.Captcha;
import com.maicard.common.base.BaseDao;

@Repository
public class CaptchaDaoImpl extends BaseDao implements CaptchaDao {

	/*@PostConstruct
	public void init(){
		try{
			getSqlSessionTemplate().update("Captcha.init");
		}catch(Exception e){
			logger.error("无法执行Captcha初始化语句:" + e.getMessage());
		}
	}*/


	@Override
	public int insert(Captcha captcha) {
		logger.info("尝试插入新的验证码.");
		try{
			return 	 getSqlSessionTemplate().insert("Captcha.insert", captcha);
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;

	}

	@Override
	public int update(Captcha captcha) {
		return getSqlSessionTemplate().update("Captcha.update", captcha);
	}

	@Override
	public int delete(int captchaId) {
		return getSqlSessionTemplate().delete("Captcha.delete", new Integer(captchaId));
	}

	@Override
	public List<Captcha> list(CaptchaCriteria captchaCriteria) {
		Assert.notNull(captchaCriteria, "captchaCriteria must not be null");		
		return getSqlSessionTemplate().selectList("Captcha.list", captchaCriteria);	}

	@Override
	public Captcha get(CaptchaCriteria captchaCriteria) {
		List<Captcha> captchaList = getSqlSessionTemplate().selectList("Captcha.list", captchaCriteria);	
		if(captchaList == null || captchaList.size() < 1){
			return null;
		}
		return captchaList.get(0);
	}

}
