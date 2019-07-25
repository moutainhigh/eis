package com.maicard.wpt.partner.controller;

import com.maicard.annotation.AllowJsonOutput;
import com.maicard.billing.service.ShareConfigService;
import com.maicard.common.base.BaseController;
import com.maicard.common.criteria.CacheCriteria;
import com.maicard.common.criteria.DataDefineCriteria;
import com.maicard.common.domain.Attribute;
import com.maicard.common.domain.DataDefine;
import com.maicard.common.domain.EisMessage;
import com.maicard.common.service.ApplicationContextService;
import com.maicard.common.service.CacheService;
import com.maicard.common.service.ConfigService;
import com.maicard.common.service.DataDefineService;
import com.maicard.common.util.NumericUtils;
import com.maicard.exception.UserNotFoundInRequestException;
import com.maicard.flow.criteria.WorkflowInstanceCriteria;
import com.maicard.flow.domain.WorkflowInstance;
import com.maicard.flow.service.WorkflowInstanceService;
import com.maicard.mb.service.MessageService;
import com.maicard.product.service.BusinessProcessor;
import com.maicard.product.service.ProductService;
import com.maicard.security.criteria.RoleCriteria;
import com.maicard.security.domain.Role;
import com.maicard.security.domain.User;
import com.maicard.security.domain.UserData;
import com.maicard.security.service.*;
import com.maicard.standard.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.ServletRequestUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static com.maicard.standard.CommonStandard.partnerMessageView;

/**
 * @author: iron
 * @description: 商户管理控制器
 * @date : created in  2017/12/12 16:46.
 */
@Controller
@RequestMapping("/merchant")
public class MerchantManageController extends BaseController {
    @Resource
    private ApplicationContextService applicationContextService;
    @Resource
    private AuthorizeService authorizeService;
    @Resource
    private com.maicard.product.service.ProductService ProductService;
    @Resource
    private MessageService messageService;
    @Resource
    private CertifyService certifyService;
    @Resource
    private ConfigService configService;
    @Resource
    private DataDefineService dataDefineService;
    @Resource
    private PartnerService partnerService;
    @Resource
    private ShareConfigService shareConfigService;
    @Resource
    private PartnerRoleService partnerRoleService;
    @Resource
    private PartnerRoleRelationService partnerRoleRelationService;
    @Resource
    private CacheService cacheService;
    @Resource
    private UserDataService userDataService;
    @Resource
    private WorkflowInstanceService workflowInstanceService;

    /**
     * 商户注册
     *
     * @param request
     * @param response
     * @param map
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/join", method = RequestMethod.GET)
    public String getCreate(HttpServletRequest request, HttpServletResponse response, ModelMap map) throws Exception {
        User partner = certifyService.getLoginedUser(request, response, SecurityStandard.UserTypes.partner.getId());
        long ownerId = NumericUtils.parseLong(map.get("ownerId"));
        boolean validateResult = merchantInfoValidate(partner, ownerId, map);
        if (!validateResult) {
            return CommonStandard.partnerMessageView;
        }
        boolean isPlatformGenericPartner = authorizeService.isPlatformGenericPartner(partner);
        		/*角色列表*/
        RoleCriteria roleCriteria = new RoleCriteria();
        roleCriteria.setOwnerId(partner.getOwnerId());
        roleCriteria.setRoleLevel(2);
        if (partner.getUserExtraTypeId()>0) {
            roleCriteria.setRoleLevel(partner.getUserExtraTypeId());
        }
        List<Role> partnerRoleList = partnerRoleService.list(roleCriteria);

        DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
        dataDefineCriteria.setObjectType(ObjectType.user.toString());
        dataDefineCriteria.setObjectId(SecurityStandard.UserTypes.partner.getId());
        List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
        if (dataDefineList == null || dataDefineList.size() < 1) {
            logger.warn("合作伙伴类型没有可选自定义字段");
        } else {
            List<DataDefine> dataDefineList2 = new ArrayList<DataDefine>(dataDefineList.size());
            for(DataDefine dataDefine : dataDefineList){
                if(dataDefine.getInputLevel() == null || dataDefine.getInputLevel().equals(InputLevel.partner.name())){
                    dataDefineList2.add(dataDefine);
                }
                if(dataDefine.getInputLevel() == null || (dataDefine.getInputLevel().equals(InputLevel.platform.name()) && isPlatformGenericPartner)){
                    dataDefineList2.add(dataDefine);
                }
            }
            map.put("configMap", dataDefineList2);
            dataDefineList = null;
        }

        map.put("statusCodeList", SecurityStandard.UserStatus.values());
        map.put("userExtraTypeList", SecurityStandard.UserExtraType.values());
        map.put("partner", new User());
        map.put("partnerRoleList", partnerRoleList);
        return "common/merchantManage/regist";
    }

    @RequestMapping(value="/plus", method=RequestMethod.POST)
    @AllowJsonOutput
    public String addMerchant(HttpServletRequest request, HttpServletResponse response, ModelMap map, User child) throws Exception {
        final String view = CommonStandard.partnerMessageView;
        ////////////////////////标准流程 ///////////////////////
        User partner  = certifyService.getLoginedUser(request, response, SecurityStandard.UserTypes.partner.getId());
        String username = ServletRequestUtils.getStringParameter(request, "username");
        logger.debug(username+"********************************************************************************");
        long ownerId = NumericUtils.parseLong(map.get("ownerId"));
        boolean validateResult = merchantInfoValidate(partner, ownerId, map);
        if (!validateResult) {
            return CommonStandard.partnerMessageView;
        }
        child.setOwnerId(ownerId);
        child.setUserTypeId(SecurityStandard.UserTypes.partner.getId());
        child.setUserExtraTypeId(2);
        child.setParentUuid(partner.getUuid());
        child.setInviter(partner.getUuid());
        String[] type1 = request.getParameterValues("roleId");
        List<Role> relatedRoleList = new ArrayList<Role>();

        if (type1!=null){
            for (int i = 0; i < type1.length; i++) {
                Role role=new Role();
                role.setRoleId(Integer.parseInt(type1[i]));
                relatedRoleList.add(role);
                logger.debug(type1[i]+"****************************************************************************");
            }
            child.setRelatedRoleList(relatedRoleList);
        }else{
            partnerRoleRelationService.deleteByUuid(child.getUuid());
        }
        logger.info("创建新商户时，尝试获取业务流实例,对象类型代码:"	+ ObjectType.merchant.toString() + ", 操作代码:" + Operate.create.name() + "].");
        WorkflowInstanceCriteria workflowInstanceCriteria = new WorkflowInstanceCriteria(0, ObjectType.merchant.toString(), SecurityStandard.UserTypes.partner.getId(), Operate.create.name(), partner);
        WorkflowInstance workflowInstance = workflowInstanceService.getInstance(workflowInstanceCriteria);
        Map<String, Attribute> validAttributeMap = new HashMap<String, Attribute>();
        int postProcess = 0;
        if (workflowInstance == null) {
            logger.debug("找不到创建新产品的工作流实例，或不需要使用工作流");

        } else {
            if (workflowInstance.getRouteList() == null || workflowInstance.getRouteList().size() < 1) {
                logger.error("针对对象[" + ObjectType.merchant.toString()	+ "]的工作流，其工作步骤为空");
                map.put("message", new EisMessage(EisError.workflowInstanceNotFound.getId(),"工作流数据异常"						));
                return partnerMessageView;
            }
            logger.debug("创建新商户使用" +  workflowInstance.getWorkflowInstanceId() + "#工作流实例，当前步骤:" + workflowInstance.getCurrentStep());

            map.put("workflowInstanceId", workflowInstance.getWorkflowInstanceId());
            validAttributeMap = workflowInstanceService.getValidInputAttribute(child, workflowInstance,null);
            if(workflowInstance.getCurrentRoute() != null){
                String postProcessStr = workflowInstance.getCurrentRoute().getPostProcess();
                if(NumericUtils.isNumeric(postProcessStr)){
                    postProcess = NumericUtils.parseInt(postProcessStr);
                    logger.debug("当前操作模式postProcess=" + postProcess);

                } else {
                    logger.error("当前工作流[" + workflowInstance.getWorkflowInstanceId() + "]没有当前步骤");
                }

            }
            if(validAttributeMap != null && validAttributeMap.containsKey("processClass")){
                //查看系统中是否有业务处理器，如果有，则放入
                String[] processorNames = applicationContextService.getBeanNamesForType(BusinessProcessor.class);
                logger.debug("当前系统中存在的业务处理器数量是:" + (processorNames == null ? "空" : processorNames.length ) );
                if(processorNames != null && processorNames.length > 0){
                    validAttributeMap.get("processClass").setValidValue(processorNames);
                    validAttributeMap.get("processClass").setUseMessagePrefix("ProcessClass");
                }

            }
            map.put("validAttributeMap",validAttributeMap);
        }
//        DataDefineCriteria dataDefineCriteria = new DataDefineCriteria();
//        dataDefineCriteria.setObjectType(ObjectType.user.toString());
//        dataDefineCriteria.setObjectId(child.getUserTypeId());
//        List<DataDefine> dataDefineList = dataDefineService.list(dataDefineCriteria);
//        if (dataDefineList == null || dataDefineList.size() == 0) {
//            logger.info("当前账户类型[" + child.getUserTypeId() + "]没有自定义字段.");
//        } else {
//            logger.debug("当前账户类型有[" + dataDefineList.size() + "]个自定义数据规范");
//
//            for (DataDefine dataDefine : dataDefineList) {
//                logger.debug("尝试获取数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]定义的数据");
//                String dataStr = ServletRequestUtils.getStringParameter(request,"data." + dataDefine.getDataCode());
//                if (dataStr == null || dataStr.equals("")) {
//                    logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]没有提交数据");
//                    continue;
//                }
//                logger.debug("数据规范[" + dataDefine.getDataCode() + "/" + dataDefine.getDataDefineId() + "]提交的数据是[" + dataStr + "]");
//                UserData userData = new UserData();
//                userData.setDataDefineId(dataDefine.getDataDefineId());
//                userData.setDataCode(dataDefine.getDataCode());
//                userData.setCurrentStatus(BasicStatus.normal.getId());
//                userData.setDataValue(dataStr);
//                if (child.getUserConfigMap() == null) {
//                    child.setUserConfigMap(new HashMap<String, UserData>());
//                }
//                logger.debug("尝试插入自定义账户数据[" + userData.getDataCode() + "/" + userData.getDataDefineId() + "]，数据内容:[" + userData.getDataValue() + "]");
//                child.getUserConfigMap().put(userData.getDataCode(), userData);
//            }
//        }
        child.setRelatedRoleList(relatedRoleList);
        long headUuid = ServletRequestUtils.getLongParameter(request, "headUuid", 0);
        child.setHeadUuid(headUuid);
//        processPartnerData(request, child);
        logger.info("请求写入新的Partner[" + child.getUsername() + "],parentUuid=[" + child.getParentUuid() + "]");
        child.setCreateTime(new Date());
        EisMessage message = null;

        logger.debug(child.toString()+"****************************************************************************");
        logger.debug(child.getNickName()+child.getUsername()+"++++++++++++++"+child.getUserPassword()+"++++++++++++++"+child.getAuthKey()+"++++++++++++++");
        if(partnerService.insert(child) == 1 ){
            cacheService.delete(new CacheCriteria(CommonStandard.cacheNameUser, "Partner#"+partner.getUuid()));
            message = new EisMessage(OperateResult.success.getId(),"操作完成");
        } else {
            message = new EisMessage(OperateResult.failed.getId(),"操作失败");
        }

        map.put("message", message);
        return view;
    }

    /**
     * 业务标准流程验证
     *
     * @param partner
     * @param ownerId
     * @param map
     * @return
     */
    private boolean merchantInfoValidate(User partner, long ownerId, ModelMap map) {
        if (partner == null) {
            throw new UserNotFoundInRequestException();
        }
        if (ownerId < 1) {
            logger.error("系统会话中没有ownerId数据");
            map.put("message", new EisMessage(EisError.systemDataError.getId(), "系统异常", "请尝试访问其他页面或返回首页"));
            return false;
        }
        if (partner.getOwnerId() != ownerId) {
            logger.error("用户[" + partner.getUuid() + "]的ownerId[" + partner.getOwnerId() + "]与系统的[" + ownerId + "]不匹配");
            map.put("message", new EisMessage(EisError.ownerNotMatch.getId(), "您尚未登录，请先登录"));
            return false;
        }
        return true;
    }


    private void processPartnerData(HttpServletRequest request, User partner){
        //先从系统中获取当前partner的所有配置
        User _oldPartner = partnerService.select(partner.getUuid());
        if(_oldPartner == null){
            logger.error("在系统中找不到需要处理的Partner[" + partner + "]");
            return;
        }
        HashMap<String, UserData>userConfigMap = new HashMap<String, UserData>();
        if(_oldPartner.getUserConfigMap() == null){
            logger.debug("将要处理的Partner[" + partner + "]没有已存在的用户数据");
        } else {
            userConfigMap = _oldPartner.getUserConfigMap();
        }
        //尝试从请求中获取所有自定义数据的值
        DataDefineCriteria userDataDefinePolicyCriteria = new DataDefineCriteria();
        userDataDefinePolicyCriteria.setObjectId(SecurityStandard.UserTypes.partner.getId());
        userDataDefinePolicyCriteria.setObjectExtraId(partner.getUserExtraTypeId());
        List<DataDefine> userDataDefinePolicyList = dataDefineService.list(userDataDefinePolicyCriteria);
        if(userDataDefinePolicyList != null){
            for(DataDefine userDataDefinePolicy: userDataDefinePolicyList){
                if(userDataDefinePolicy.getInputLevel() == null){
                    logger.debug("数据[" + userDataDefinePolicy.getDataCode() + "]的输入级别为空，跳过数据.");
                    continue;
                }
                if(StringUtils.isBlank(userDataDefinePolicy.getInputLevel()) || userDataDefinePolicy.getInputLevel().equals(InputLevel.platform.name()) || userDataDefinePolicy.getInputLevel().equals(InputLevel.user.name())){
                    String data = request.getParameter(userDataDefinePolicy.getDataCode());
                    logger.debug("尝试获取数据规范[" + userDataDefinePolicy.getDataCode() + "]的数据:" + data);
                    if(StringUtils.isBlank(data)){
                        continue;
                    }
                    UserData ud = new UserData();
                    ud.setDataDefineId(userDataDefinePolicy.getDataDefineId());
                    ud.setDataValue(data);
                    ud.setDataCode(userDataDefinePolicy.getDataCode());
                    userConfigMap.put(ud.getDataCode(), ud);
                } else {
                    logger.debug("数据[" + userDataDefinePolicy.getDataCode() + "]的输入级别不是用户或平台级别输入，跳过.");
                    continue;
                }
            }
        }
        partner.setUserConfigMap(userConfigMap);
        logger.debug("更新的合作伙伴[" + partner.getUserTypeId() + "/" + partner.getUserExtraTypeId() + "]，该用户类型有" + (userDataDefinePolicyList == null ? 0 : userDataDefinePolicyList.size()) + "条数据规范，从请求中得到了[" + (partner.getUserConfigMap() == null ? 0 : partner.getUserConfigMap().size()) + "]条数据");
    }

}
