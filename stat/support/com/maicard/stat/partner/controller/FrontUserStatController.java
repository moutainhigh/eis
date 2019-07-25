package com.maicard.stat.partner.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.EisError;
import com.maicard.standard.SecurityStandard;
import com.maicard.stat.criteria.FrontUserStatCriteria;
import com.maicard.stat.domain.FrontUserStat;
import com.maicard.stat.service.FrontUserStatService;


@Controller
@RequestMapping("/frontUserStat")
public class FrontUserStatController extends BaseController {

	@Resource
	private FrontUserStatService frontUserStatService;
	@Resource
	private AuthorizeService authorizeService;
	@Resource
	private CertifyService certifyService;
	@Resource
	private PartnerService partnerService;

	private int rowsPerPage = 10;

	final private SimpleDateFormat sdf = new SimpleDateFormat(CommonStandard.defaultDateFormat);

	//按时间统计用户
	@RequestMapping(method = RequestMethod.GET)
	public String index(HttpServletRequest request, HttpServletResponse response, ModelMap map, FrontUserStatCriteria frontUserStatCriteria) throws Exception {
		
		final String view = "stat/frontUser/index";

		User partner  = certifyService.getLoginedUser(request, response, SecurityStandard.UserTypes.partner.getId());
		if(partner == null ){
			throw new UserNotFoundInRequestException();
		}
		int rows = ServletRequestUtils.getIntParameter(request, "rows", rowsPerPage);		
		int page = ServletRequestUtils.getIntParameter(request, "page", 1);

		//查询开始时间
		if(frontUserStatCriteria.getQueryBeginTime() == null){
			//如果没设置起始时间，设置为一周前
			frontUserStatCriteria.setQueryBeginTime(DateUtils.truncate(DateUtils.addDays(new Date(), -7),Calendar.DAY_OF_MONTH));
		}

		long ownerId = (long)map.get("ownerId");

		if(ownerId < 1){
			logger.error("系统会话中没有ownerId数据");
			map.put("message", new EisMessage(EisError.systemDataError.getId(),"系统异常","请尝试访问其他页面或返回首页"));
			return CommonStandard.frontMessageView;		
		}

		boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
		logger.debug("当前合作伙伴[" + partner.getUuid() + "/" + partner.getUsername() + "]" + (isPlatformGenericPartner ? "是" : "不是") + "一般性合作伙伴");
		if(!isPlatformGenericPartner){
			partnerService.setSubPartner(frontUserStatCriteria, partner);
		}
		
		List<User> inviterList  = new ArrayList<User>();
		UserCriteria userCriteria = new UserCriteria(ownerId);
		if(!isPlatformGenericPartner){
			partnerService.applyMoreDynmicData(partner);
			inviterList = partner.getProgeny();
		} else {
			inviterList = partnerService.list(userCriteria);
		}
		if(inviterList != null && inviterList.size() > 0){
			map.put("inviterList", inviterList);
		}

		logger.info("前端用户统计查询起止时间:" + sdf.format(frontUserStatCriteria.getQueryBeginTime()) + " 到 " + (frontUserStatCriteria.getQueryEndTime() == null ? "空" : sdf.format(frontUserStatCriteria.getQueryEndTime())));
		int totalRows = frontUserStatService.count(frontUserStatCriteria);
		if(totalRows < 1){
			logger.debug("当前返回的数据数量是0");
			return view;
		}
		map.put("total", totalRows);

		Paging paging = new Paging(rows);
		paging.setCurrentPage(page);
		frontUserStatCriteria.setPaging(paging);
		map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));		
		List<FrontUserStat> frontUserStatList = frontUserStatService.listOnPage(frontUserStatCriteria);
		if(frontUserStatList != null && frontUserStatList.size() > 0){
			for(FrontUserStat frontUserStat : frontUserStatList){
				if(frontUserStat.getInviter() > 0){
					User inviterPartner = partnerService.select(frontUserStat.getInviter());
					if(inviterPartner != null){
						frontUserStat.setExtraValue("inviterName", (inviterPartner.getNickName() == null ? inviterPartner.getUsername() : inviterPartner.getNickName()));
					} else {
						frontUserStat.setExtraValue("inviterName", "未知");
					}
				} else {
					frontUserStat.setExtraValue("inviterName", "所有");

				}
			}
		}
		
		map.put("rows",frontUserStatList);
		List<User> childrenPartner = new ArrayList<User>();
		//partnerService.listAllChildren(childrenPartner, currentUuid);
		map.put("childrenPartner",  childrenPartner);
		return view;
	}


}
