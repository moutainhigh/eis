package com.maicard.captcha.service;

import com.maicard.captcha.criteria.CaptchaCriteria;
import com.maicard.captcha.domain.Captcha;

public interface Generator {
	public Captcha generate(CaptchaCriteria patchcaCriteria);
}
