package com.maicard.captcha.dao;

import java.util.List;

import com.maicard.captcha.criteria.CaptchaCriteria;
import com.maicard.captcha.domain.Captcha;

public interface CaptchaDao {
	public int insert(Captcha captcha);

	public int update(Captcha captcha);

	public List<Captcha> list(CaptchaCriteria captchaCriteria);


	public Captcha get(CaptchaCriteria captchaCriteria);

	int delete(int captchaId);
}
