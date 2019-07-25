<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp"%>

<%@include file="/WEB-INF/jsp/common/include/header.jsp"%>
    <div class="container-fluid">
		<div class="row">
			<div class="fullScreenBg" style="display:none;">	
			</div>
			<div class="message" style="display:none;">
				<span class="text"></span>
				<div class="submit">确定</div>
			</div>
		<%@include file="/WEB-INF/jsp/common/include/menu.jsp"%>
        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">
			<h2 class="sub-header"><span>产品：[${productType.productTypeName}]</span></h2>
			<div class="table-responsive">	
				<form:form id="productTypeForm"  commandName="product" name="productForm" action="/product/create.shtml" enctype="multipart/form-data" onsubmit="return false">
					<input type="hidden" name="workflowInstanceId" value="${workflowInstanceId}"/>
					<input type="hidden" name="productCode"  value="${product.productCode}"  />
						<table class="table table-striped">
							<thead style="display:none;">
								<tr>
								  <th style="width:20%;text-align:right;"></th>
								  <th style="width:80%;text-align:left;"></th>		                  
								</tr>
							</thead>
							<tbody>
								<tr>
									<td style="width:20%;text-align:right;" class="productLeft">产品编码：</td>
									<td style="width:80%;text-align:left;" class="productRight clearfix">
										
										${product.productCode}
									</td>
								</tr>
							<c:forEach items="${validAttributeMap}" var="map">
								<tr>	
									<c:choose>
										<c:when test="${map.value.readonly=='true'}">
											<td style="width:20%;text-align:right;" class="productLeft"><spring:message code="DataName.${map.value.columnName}"  />：</td>
											<c:choose>
												<c:when test="${map.value.columnType == 'extra'}">
													<td style="width:80%;text-align:left;" class="productRight clearfix">${product.productDataMap[map.value.columnName].dataValue} <input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" value="${product.productDataMap[map.value.columnName].dataValue}" /></td>
												</c:when>
												<c:otherwise>
													<td style="width:80%;text-align:left;" class="productRight clearfix">${product[map.value.columnName]} <input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" value="${product[map.value.columnName]}" /></td>
												</c:otherwise>
											</c:choose>
										</c:when>	
										<c:when test="${map.value.hidden=='2'}">
											<!-- no this attribute: ${map.value.columnName} -->
										</c:when>	
										<c:when test="${map.value.hidden=='1'}">
											<c:choose>
												<c:when test="${map.value.columnType == 'extra'}">
													<input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" value="${product.productDataMap[map.value.columnName].dataValue}" />
												</c:when>
												<c:otherwise>
													<input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" value="${product[map.value.columnName]}" />
												</c:otherwise>
											</c:choose>
										</c:when>								
									
										<c:otherwise>			
											<td style="width:20%;text-align:right;" class="productLeft clearfix"><spring:message code="DataName.${map.value.columnName}"  />：</td>
											<c:choose>
												<c:when test="${map.value.inputMethod=='file'}"> 
													<td style="width:80%;text-align:left;" class="productRight clearfix">
													<c:choose>
														<c:when test="${map.value.columnType == 'extra'}">
															<c:choose>
																<c:when test="${map.value.columnName == 'deliveryPriceListFile'}">
																	<input type="file" name="${map.value.columnName}" id="${map.value.columnName}" />
																	${product.productDataMap[map.value.columnName].dataValue}
																</c:when>
																<c:otherwise>
																	<!--<input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" />产品小图-->
																	<div id="uploadready" class="marleft75 martop30"><!--productGallery0-->
																		<div class="box_input" id="box_input_${map.value.columnName}" style="margin-bottom: 10px;">
																			<a class="btn_addPic inputa" href="javascript:void(0);"><span>添加文件</span>
																			<input type="file" class="filePrew" name="${map.value.columnName}" id="${map.value.columnName}" value=""   data-name="${map.value.columnName}"/>
																			</a>
																		</div>


																		<div class="dropbox">
																			<ul class="pageContent" id="pageContent_${map.value.columnName}">
																			</ul>
																			<ul class="uploaded">
																				<c:if test="${!empty product.productDataMap[map.value.columnName].dataValue}">
																					<c:if test="${fn:contains(product.productDataMap[map.value.columnName].dataValue, '.')}">
																					<li>
																						<div class="showPic" id="showPic_${map.value.columnName}">
																							<img src="${product.productDataMap[map.value.columnName].dataValue}" alt="${product.productDataMap[map.value.columnName].dataValue}" border="0"/>
																						</div>
																						<span class="btn_delPic" id="btn_delPic_${map.value.columnName}" onclick="delPic('${map.value.columnName}','')">确认删除</span>
																					</li>
																					</c:if>
																				</c:if>
																			 </ul> 
																		</div>
																	</div>
																</c:otherwise>
															</c:choose>
														</c:when>
														<c:otherwise>
															${product[map.value.columnName]}
														</c:otherwise>
													</c:choose>
													</td>
												</c:when>
												<c:when test="${map.value.inputMethod=='gallery'}">
													<td style="width:80%;text-align:left;" class="productRight clearfix">
														<!--<input type="hidden" value="${map.value.columnName}" id="${map.value.columnName}">付费用户才能看得-->
														<div id="uploadready" class="marleft75 martop30"><!--productGallery0-->
															<div class="box_input" id="box_input_${map.value.columnName}" style="margin-bottom: 10px;">
																<a class="btn_addPic inputa" href="javascript:void(0);"><span>添加文件</span>
																<input type="file" class="filePrew" name="${map.value.columnName}" id="${map.value.columnName}" value=""  class="blue"   data-name="${map.value.columnName}"/>                  
																</a>
															</div>

															<div class="dropbox">
																<ul class="pageContent" id="pageContent_${map.value.columnName}">
																</ul>
																<ul class="uploaded" id='uploaded_productGallery'>
																	<c:if test="${!empty product.productDataMap[map.value.columnName].dataValue}">
																		<c:set value="${fn:split(product.productDataMap[map.value.columnName].dataValue, ',')}" var="img1" />
																		<c:forEach items="${img1}" var="img">
																			<li>
																				<div class="showPic_${map.value.columnName}" id="${img}">
																					<img src="/static/productFile/open/${img}" alt="/static/productFile/open/${img}" border="0"/>
																				</div>
																				<span class="btn_delPic" id="btn_delPic_${img}" onclick="delPic('${map.value.columnName}','${img}')">确认删除</span>
																			</li>
																		</c:forEach>
																	</c:if>
																 </ul> 
															</div>
														</div>
													</td>
												</c:when>
												<c:when test="${map.value.inputMethod=='textarea'}">
													<td style="width:80%;text-align:left;" class="productRight clearfix"><textarea rows='5' cols='100' name="${map.value.columnName}" id="${map.value.columnName}" placeholder="请输入<spring:message code='${map.value.useMessagePrefix}.${map.value.columnName}'  />"  ></textarea></td>
												</c:when>
												<c:when test="${map.value.inputMethod=='date'}">	
													<td style="width:80%;text-align:left;" class="productRight clearfix">							
													<c:choose>
														<c:when test="${map.value.columnType == 'extra'}">
															<input type="text" name="${map.value.columnName}"  id="${map.value.columnName}" value="${document.documentDataMap[map.value.columnName].dataValue}" />
															<img id="my_datetime" onclick="WdatePicker({el:'${map.value.columnName}',dateFmt:'yyyy-MM-dd HH:mm:ss'})" src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
														</c:when>
														<c:otherwise>
															<input type="text" name="${map.value.columnName}" id="${map.value.columnName}" value='<fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${document[map.value.columnName]}" type="both"/>' />
															<img onclick="WdatePicker({el:'${map.value.columnName}',dateFmt:'yyyy-MM-dd HH:mm:ss'})"  src="/theme/${theme}/images/datebox_arrow.png" width="18" height="20" align="absmiddle" style="cursor:pointer;">
														</c:otherwise>
													</c:choose>
													</td>
												</c:when>	
												<c:when test="${map.value.inputMethod=='select'}">
													<td style="width:80%;text-align:left;" class="productRight clearfix">
													<c:choose>
														<c:when test="${map.value.columnName == 'currentStatus'}">
															<select name="${map.value.columnName}" id="${map.value.columnName}">
															<c:forEach var="s" items="${map.value.validValue}">
																<option value="${s}"><!--${s}--> <spring:message code="Status.${s}" /></option>
															</c:forEach>
															</select>
														</c:when>
														<c:when test="${map.value.columnName == 'productTypeId'}">
															<select name="${map.value.columnName}" id="${map.value.columnName}">
															<c:forEach var="s" items="${map.value.validValue}">
																<option value="${s}"><!--${s}--> <spring:message code="ProductTypeId.${s}" /></option>
															</c:forEach>
														</select>
														</c:when>
														<c:otherwise>
															<select name="${map.value.columnName}" id="${map.value.columnName}">
															<c:forEach var="s" items="${map.value.validValue}">
																<option value="${s}"><!--${s}--> <spring:message code="${map.value.useMessagePrefix}.${s}" /></option>
															</c:forEach>
														</c:otherwise>
													</c:choose>
													</td>
												</c:when>
												<c:otherwise>
													<td style="width:80%;text-align:left;" class="productRight"><input type="text" name="${map.value.columnName}" id="${map.value.columnName}" placeholder="请输入<spring:message code='${map.value.useMessagePrefix}.${map.value.columnName}'  />" 
														<c:choose>
														<c:when test="${map.value.columnType == 'extra'}">
															value="${product.productDataMap[map.value.columnName].dataValue}"
														</c:when>
														<c:otherwise>
															value="${product[map.value.columnName]}"
														</c:otherwise>
														</c:choose>
													/></td>
												</c:otherwise>								
											</c:choose>
										</c:otherwise>
									</c:choose>
								</tr>  
								
							</c:forEach>	
								<!-- 下面是非动态、写死的输入 -->
							<tr>
			   					<td style="text-align:right;">
			   						图片列表展现形式：
			   					</td>
			   					<td style="text-align:left;padding-left:5%;">
			   						<select name="displayTypeId" id="displayTypeId">
			   							<option value="0"> -- 请选择 -- </option>
			   							<option value="176005">轮播图1</option>
			   						</select>
			   					</td>
			   				</tr>
							<%-- <c:if test="${postProcess > 0}"> --%>
								<tr>
									<td style="width:20%;text-align:right;">市场价格：</td>
									<td style="width:80%;text-align:left;">
										<input name="labelMoney" id="labelMoney" type="text" value="${product.labelMoney}"  />
									</td>							
								</tr>
								<tr >
									<td style="width:20%;text-align:right;">标准价格：</td>
									<td style="width:80%;text-align:left;">
										<span>${moneyName}：<input name="PRICE_PRICE_STANDARD.money" id="PRICE_PRICE_STANDARDmoney" type="text" value="${product.price.money}" style="margin-left:5px;margin-right:10px;width:80px;"/></span>
										<span>${coinName}：<input name="PRICE_PRICE_STANDARD.coin" id="PRICE_PRICE_STANDARDcoin" type="text" value="${product.price.coin}" style="margin-left:5px;margin-right:10px;width:80px;"/></span>
										<span>${pointName}：<input name="PRICE_PRICE_STANDARD.point" id="PRICE_PRICE_STANDARDpoint" type="text" value="${product.price.point}" style="margin-left:5px;margin-right:10px;width:80px;"/></span>
										<span>${scoreName}：<input name="PRICE_PRICE_STANDARD.score" id="PRICE_PRICE_STANDARDscore" type="text" value="${product.price.score}" style="margin-left:5px;margin-right:10px;width:80px;"/></span>
									</td>							
								</tr>
								<tr>
									<td style="font-size:16px; text-align:left" colspan="2">以下为产品文档内容</td>		
								</tr>
								<tr>
									<td>标签（多个请用英文逗号分开）</td>
									<td>
										<form:input path="tags" size="50"/>
										<form:errors path="tags" cssClass="errorMessage"/>
									</td>							
								</tr>
								<tr>
									<td>指定模板</td>
									<td>
										<select name="templateId" id="templateId">
											<option value="0">不选择[将使用节点模版]</option>
											<c:forEach var="t" items="${templateList}">
												<option value="${t.templateId}">${t.templateName}[${t.templateId}]</option>
											</c:forEach>
										</select>						
									</td>							
								</tr>
								<tr class="content">
									<td>内容</td>
									<td>
										<script type="text/javascript">
											var editor = new UE.ui.Editor();
											textarea:'content'; //与textarea的name值保持一致
											editor.render('content');
											</script>
											<script type="text/plain" id="content" name="content">
											${document.content}
										</script>						
									</td>							
								</tr>
								<tr>
									<td style="width:20%;text-align:right;">前台页面显示顺序</td>
									<td style="width:80%;text-align:left;">
										<form:input size="50"  path="displayIndex"/> 
										<form:errors path="displayIndex" cssClass="errorMessage"/>
									</td>							
								</tr>
								

								<tr>
									<td>发布到的位置</td>
									<td>
										<ul id="browser" class="filetree" style="background:#F9F9F9">								
										<c:forEach var="node" items="${nodeTree}" >
											<li>
												<c:set var="parentNode" value="${node}" scope="request"/>
												<c:import url="/WEB-INF/jsp/common/include/documentTreeList.jsp" />
											</li>
										</c:forEach>								
										</ul>
									</td>							
								</tr>
								<tr>
									<td>同步显示位置</td>
									<td>
										<ul id="browserSync" class="filetree" style="background:#F9F9F9">								
										<c:forEach var="nodeSync" items="${nodeTree}" >
											<li>
												<c:set var="parentNodeSync" value="${nodeSync}" scope="request"/>
												<c:import url="/WEB-INF/jsp/common/include/documentTreeListSync.jsp" />
											</li>
										</c:forEach>								
										</ul>					
									</td>							
								</tr>
								<tr id="lastTr">
								<td>
								
								
								</td>
								</tr>
							</c:if>
								
							</tbody>
				</table>
				<div class="submit"><input type="submit" value="保存产品" class="addPro querenbtn"></div>
				</form:form>
			</div>
        </div>
      </div>
    </div>
	<%@include file="/WEB-INF/jsp/common/include/footer.jsp"%>
