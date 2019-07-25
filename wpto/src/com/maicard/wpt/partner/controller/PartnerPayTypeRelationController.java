package com.maicard.wpt.partner.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMethod;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.common.base.BaseController;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ConfigService;
import com.maicard.common.util.NumericUtils;
import com.maicard.common.util.Paging;
import com.maicard.common.util.PagingUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.mb.service.MessageService;
import com.maicard.money.criteria.PayTypeCriteria;
import com.maicard.money.domain.PayType;
import com.maicard.money.service.PayTypeService;
import com.maicard.security.criteria.UserCriteria;
import com.maicard.security.criteria.UserRelationCriteria;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserRelation;
import com.maicard.security.service.AuthorizeService;
import com.maicard.security.service.CertifyService;
import com.maicard.security.service.PartnerService;
import com.maicard.security.service.UserRelationService;
import com.maicard.standard.CommonStandard;
import com.maicard.standard.DataName;
import com.maicard.standard.EisError;
import com.maicard.standard.ObjectType;
import com.maicard.standard.OperateResult;
import com.maicard.standard.SecurityStandard.UserTypes;

/**
 * 允许商户开通哪些支付方式
 *
 * @author hailong
 * @date 2017-6-26
 */
@Controller
@RequestMapping("/partnerPayTypeRelation")
public class PartnerPayTypeRelationController extends BaseController {

    @Resource
    private CertifyService certifyService;
    @Resource
    private AuthorizeService authorizeService;
    @Resource
    private PartnerService partnerService;
    @Resource
    private PayTypeService payTypeService;
    @Resource
    private UserRelationService userRelationService;
    @Resource
    private MessageService messageService;
    @Resource
    private ConfigService configService;

    private int rowsPerPage = 10;

    @PostConstruct
    public void init() {
        rowsPerPage = configService.getIntValue(
                DataName.partnerRowsPerPage.toString(), 0);
        if (rowsPerPage < 1) {
            rowsPerPage = CommonStandard.DEFAULT_PARTNER_ROWS_PER_PAGE;
        }
    }

    @RequestMapping(method = RequestMethod.GET)
    public String list(HttpServletRequest request,
                       HttpServletResponse response, ModelMap map, UserCriteria partnerCriteria) throws Exception {

        final String view = "common/partner/paylist";
        ////////////////////////标准流程 ///////////////////////
        User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
        if (partner == null) {
            throw new UserNotFoundInRequestException();
        }

        long ownerId = NumericUtils.parseLong(map.get("ownerId"));

        if (ownerId < 1) {
            logger.error("系统会话中没有ownerId数据");
            map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
            return CommonStandard.partnerMessageView;
        }

        if (partner.getOwnerId() != ownerId) {
            logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
            map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
            return CommonStandard.partnerMessageView;
        }
        boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
        //////////////////////// 结束标准流程 ///////////////////////
        //EisMessage message = null;
        if (!isPlatformGenericPartner) {
            partnerService.setSubPartner(partnerCriteria, partner);
            return view;
        }
        int rows = ServletRequestUtils.getIntParameter(request, "rows",
                rowsPerPage);
        int page = ServletRequestUtils.getIntParameter(request, "page", 1);
        int totalRows = partnerService.count(partnerCriteria);
        map.put("total", totalRows);
        Paging paging = new Paging(rows);
        partnerCriteria.setPaging(paging);
        partnerCriteria.getPaging().setCurrentPage(page);
        logger.info("一共  " + totalRows + " 行数据，每页显示 " + rows + " 行数据，当前是第 "
                + page + " 页。");
        List<User> partnerList = partnerService.listOnPage(partnerCriteria);
        List<HashMap<String, Object>> maps = new ArrayList<>();

        for (User user : partnerList) {
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("username", user.getUsername());
            hashMap.put("uuid", user.getUuid());
            UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
            userRelationCriteria.setOwnerId(ownerId);
            userRelationCriteria.setRelationType(ObjectType.pay.name());
            userRelationCriteria.setObjectType(ObjectType.pay.name());
            userRelationCriteria.setUuid(user.getUuid());
            List<UserRelation> list = userRelationService.list(userRelationCriteria);
            logger.debug("用户关联支付方式的集合长度:" + list.size() + "+++++++++++++++++++++++++++++++++++++++++++++++++++++");
            if (list != null && list.size() > 0) {
                List<PayType> payTypes = new ArrayList<>();
                for (UserRelation userRelation : list) {
                    PayType payType = payTypeService.select((int) userRelation.getObjectId());
                    payTypes.add(payType);
                }
                logger.debug("支付方式的集合长度:" + payTypes.size() + "+++++++++++++++++++++++++++++++++++++++++++++++++++++");
                hashMap.put("payType", payTypes);
            }
            maps.add(hashMap);
        }
        map.put("rows", maps);
        //计算并放入分页
        map.put("contentPaging", PagingUtils.generateContentPaging(totalRows, rows, page));
        return view;
    }


    @RequestMapping(value = "/get" + "/{uuid}")
    public String getUserRelation(HttpServletRequest request,
                                  HttpServletResponse response, ModelMap map,
                                  @PathVariable("uuid") long uuid) throws Exception {
        final String view = "common/partner/payment";
        ////////////////////////标准流程 ///////////////////////
        User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
        if (partner == null) {
            throw new UserNotFoundInRequestException();
        }

        long ownerId = NumericUtils.parseLong(map.get("ownerId"));

        if (ownerId < 1) {
            logger.error("系统会话中没有ownerId数据");
            map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
            return CommonStandard.partnerMessageView;
        }

        if (partner.getOwnerId() != ownerId) {
            logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
            map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
            return CommonStandard.partnerMessageView;
        }
        boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
        //////////////////////// 结束标准流程 ///////////////////////
        if (!isPlatformGenericPartner) {
            //不是内部用户，检查是不是访问自己或下级账户
            if (partner.getUuid() == uuid || partnerService.isValidSubUser(partner.getUuid(), uuid)) {
                //用户合法访问
            } else {
                map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
                return CommonStandard.partnerMessageView;
            }
        }

        //查询出所有的支付方式
        PayTypeCriteria payTypeCriteria = new PayTypeCriteria();
        payTypeCriteria.setOwnerId(ownerId);
        List<PayType> payTypeList = payTypeService.list(payTypeCriteria);
        List<PayType> clonePayTypeList = new ArrayList<PayType>();
        clonePayTypeList.addAll(payTypeList);
        if (payTypeList != null && payTypeList.size() > 0) {
            for (PayType payType : clonePayTypeList) {
                //查询用户的关联
                UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
                userRelationCriteria.setObjectId(payType.getPayTypeId());
                userRelationCriteria.setUuid(uuid);
                userRelationCriteria.setOwnerId(ownerId);
                List<UserRelation> list = userRelationService.list(userRelationCriteria);
                logger.debug(payType.getPayTypeId() + "**************************************************************");
                if (list == null || list.size() < 1) {

                    payType.setCurrentStatus(0);
                }
                logger.debug(list.size() + "+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");
            }

        }
        map.put("payType", payTypeList);
        User user = partnerService.select(uuid);
        map.put("partner", user);
        return view;
    }

    /**
     * 修改支付方式的状态
     */
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @AllowJsonOutput
    public String update(HttpServletRequest request, HttpServletResponse response,
                         ModelMap map, @ModelAttribute("partner") User child, String payTypeId, String currentStatus) {
        final String view = CommonStandard.partnerMessageView;
        long ownerId = 0;
        try {
            ownerId = (long) map.get("ownerId");
        } catch (Exception e) {
            logger.error(ExceptionUtils.getFullStackTrace(e));
        }
        if (ownerId < 1) {
            logger.error("系统会话中没有ownerId数据");
            return view;
        }

        User partner = certifyService.getLoginedUser(request, response, UserTypes.partner.getId());
        boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
        if (!isPlatformGenericPartner) {
            //不是内部用户，检查是不是访问自己或下级账户
            if (partner.getUuid() == child.getUuid() || partnerService.isValidSubUser(partner.getUuid(), child.getUuid())) {
                //用户合法访问
            } else {
                map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
                return CommonStandard.partnerMessageView;
            }
        }

        //根据状态码  更新用户选择的付款方式
        if (StringUtils.isNotBlank(currentStatus)) {
            if (currentStatus.equals("100001")) {
                UserRelation userRelation = new UserRelation();
                userRelation.setUuid(child.getUuid());
                userRelation.setObjectId(Integer.parseInt(payTypeId));
                userRelation.setOwnerId(ownerId);
                userRelation.setRelationType("pay");
                userRelation.setObjectType(ObjectType.pay.name());
                userRelation.setCurrentStatus(100001);
                //将用户选择的付款方式添加到关联关系中
                int rs = userRelationService.insert(userRelation);
                if (rs == 1) {
                    messageService.sendJmsDataSyncMessage(null, "userRelationService", "insert", userRelation);
                }
            } else {
                UserRelationCriteria userRelationCriteria = new UserRelationCriteria();
                userRelationCriteria.setObjectId(Integer.parseInt(payTypeId));
                userRelationCriteria.setUuid(child.getUuid());
                userRelationCriteria.setOwnerId(ownerId);
                userRelationCriteria.setRelationType("pay");
                userRelationCriteria.setObjectType(ObjectType.pay.name());
                //删除这一条关联记录
                userRelationService.delete(userRelationCriteria);
            }
        }

        map.put("message", new EisMessage(OperateResult.success.id, "更新成功"));
        return view;
    }
}
