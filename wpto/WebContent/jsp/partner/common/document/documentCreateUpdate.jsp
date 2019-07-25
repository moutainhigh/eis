<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/WEB-INF/jsp/common/include/tags.jsp" %>

<c:forEach items="${validAttributeMap}" var="map">
	<tr>	
		<c:choose>
			<c:when test="${map.value.readonly=='true'}">
				<td style="width:20%;text-align:right;" class="productLeft"><spring:message code="DataName.${map.value.columnName}"  />:&nbsp</td>
				<c:choose>
					<c:when test="${map.value.columnType == 'extra'}">
						<td style="width:80%;text-align:left;" class="productRight">${document.documentDataMap[map.value.columnName].dataValue} <input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" value="${document.documentDataMap[map.value.columnName].dataValue}" /></td>
					</c:when>
					<c:otherwise>
						<td style="width:80%;text-align:left;" class="productRight">${document[map.value.columnName]} <input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" value="${document[map.value.columnName]}" /></td>
					</c:otherwise>
				</c:choose>
			</c:when>	
			<c:when test="${map.value.hidden=='2'}">
				<!-- no this attribute: ${map.value.columnName} -->
			</c:when>	
			<c:when test="${map.value.hidden=='1'}">
				<c:choose>
					<c:when test="${map.value.columnType == 'extra'}">
						<input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" value="${document.documentDataMap[map.value.columnName].dataValue}" />
					</c:when>
					<c:otherwise>
						<input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" value="${document[map.value.columnName]}" />
					</c:otherwise>
				</c:choose>
			</c:when>								
		
			<c:otherwise>			
				<td style="width:20%;text-align:right;" class="productLeft"><spring:message code="DataName.${map.value.columnName}"  />：</td>
				<c:choose>
					<c:when test="${map.value.inputMethod=='file'}"> 
						<td style="width:80%;text-align:left;" class="productRight">
						<c:choose>
							<c:when test="${map.value.columnType == 'extra'}">
								<!--<input type="file" name="${map.value.columnName}" id="${map.value.columnName}" value="${document.documentDataMap[map.value.columnName].dataValue}" />
								<e:media>${document.documentDataMap[map.value.columnName].dataValue}</e:media>-->
								<c:choose>
									<c:when test="${map.value.columnName == 'videoClip'}">
										<input type="file" name="${map.value.columnName}" id="${map.value.columnName}" />
										${document.documentDataMap[map.value.columnName].dataValue}
									</c:when>
									<c:otherwise>
										<input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" />
										<div id="uploadready" class="marleft75 martop30"><!--productGallery0-->
											<div class="box_input" id="box_input_${map.value.columnName}" style="margin-bottom: 10px;">
												<a class="btn_addPic inputa" href="javascript:void(0);"><span>添加文件</span>
												<!-- <input type="file" class="filePrew" name="${map.value.columnName}" id="${map.value.columnName}" onchange="dropHandler(this.files, '${map.value.columnName}')" value="" accept="image/gif,image/jpeg,image/png,image/bmp" class="blue" /> -->
												<input type="file" class="filePrew" name="${map.value.columnName}" id="${map.value.columnName}"value=""  multiple="multiple" data-name="${map.value.columnName}"class="blue"/>
												</a>
											</div>
											<div class="dropbox">
												<ul class="pageContent" id="pageContent_${map.value.columnName}">
												</ul>
												<ul class="uploaded">
													<!--<e:media>${document.documentDataMap[map.value.columnName].dataValue}</e:media>-->
													<c:if test="${!empty document.documentDataMap[map.value.columnName].dataValue}">
														<c:set value="${fn:split(document.documentDataMap[map.value.columnName].dataValue, ',')}" var="img1" />
														<c:forEach items="${ img1 }" var="img">
															<li>
																<div class="showPic" id="showPic_${map.value.columnName}">
																	<img src="/upload/${img}" alt="/upload/${img}"/>
																</div>
																<span class="btn_delPic" id="btn_delPic_${map.value.columnName}" onclick="delPic('${map.value.columnName}','')">确认删除</span>
															</li>
														</c:forEach>
													</c:if>
												 </ul> 
											</div>
										</div>
									</c:otherwise>
							</c:choose>
							</c:when>
							<c:otherwise>
								<input type="file" name="${map.value.columnName}" id="${map.value.columnName}" value="${document[map.value.columnName]}" />
								<e:media>${document[map.value.columnName]}</e:media>
							</c:otherwise>
						</c:choose>
						</td>
					</c:when>
					<c:when test="${map.value.inputMethod=='gallery'}"> 
						<td style="width:80%;text-align:left;" class="productRight">
							<input type="hidden" name="${map.value.columnName}" id="${map.value.columnName}" />
							<div id="uploadready" class="marleft75 martop30"><!--productGallery0-->
								<div class="box_input" id="box_input_${map.value.columnName}" style="margin-bottom: 10px;">
									<a class="btn_addPic inputa" href="javascript:void(0);"><span>添加文件</span>
									<!-- <input type="file" class="filePrew" name="${map.value.columnName}" id="${map.value.columnName}" value="" onchange="dropHandler(this.files, '${map.value.columnName}')" accept="image/gif,image/jpeg,image/png,image/bmp" class="blue" /> -->
									<input type="file" class="filePrew" name="${map.value.columnName}" id="${map.value.columnName}"value=""  multiple="multiple" data-name="${map.value.columnName}"class="blue"/>
									</a>
								</div>
								
								<div class="dropbox">
									<ul class="pageContent" id="pageContent_${map.value.columnName}">
									</ul>
									<ul class="uploaded" id='uploaded_productGallery'>
										<c:if test="${!empty document.documentDataMap[map.value.columnName].dataValue}">
											<c:set value="${fn:split(document.documentDataMap[map.value.columnName].dataValue, ',')}" var="img1" />
											<c:forEach items="${ img1 }" var="img">
												<li>
													<div class="showPic_${map.value.columnName}" id="${img}">
														<img src="/upload/${img}" alt="/upload/${img}"/>
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
						<td style="width:80%;text-align:left;" class="productRight">
						<c:choose>
							<c:when test="${map.value.columnType == 'extra'}">
								<textarea rows='5' cols='100'  name="${map.value.columnName}" id="${map.value.columnName}" value="${document.documentDataMap[map.value.columnName].dataValue}" ></textarea>
							</c:when>
							<c:otherwise>
								<textarea rows='5' cols='100' name="${map.value.columnName}" id="${map.value.columnName}" value="${document[map.value.columnName]}" ></textarea>
							</c:otherwise>
						</c:choose>
						</td>
					</c:when>
					<c:when test="${map.value.inputMethod=='date'}">	
						<td style="width:80%;text-align:left;" class="productRight">							
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
						<td style="width:80%;text-align:left;" class="productRight">									
							<select name="${map.value.columnName}" id="${map.value.columnName}">
								<c:forEach var="s" items="${map.value.validValue}">											
									<c:choose>
										<c:when test="${map.value.columnType == 'extra'}">
												<option value="${s}" <c:if test="${s == document.documentDataMap[map.value.columnName].dataValue}"> selected="selected"</c:if> ><!--${s}--> <spring:message code="${map.value.useMessagePrefix}.${s}" /></option>
										</c:when>
										<c:otherwise>
												<option value="${s}" <c:if test="${s == document[map.value.columnName]}"> selected="selected"</c:if> ><!--${s} == ${document[map.value.columnName]} --> <spring:message code="${map.value.useMessagePrefix}.${s}" /></option>
										</c:otherwise>
									</c:choose>												
								</c:forEach>
							</select>
						</td>
					</c:when>
					<c:otherwise>	
						<td style="width:80%;text-align:left;" class="productRight">							
						<c:choose>
							<c:when test="${map.value.columnType == 'extra'}">
								<c:if test="${map.value.columnName == 'productCode'}">
									<input type="text" onfocus='setStyle()' name="${map.value.columnName}" id="${map.value.columnName}" value="${document.documentDataMap[map.value.columnName].dataValue}" />
								</c:if>
								<c:if test="${map.value.columnName != 'productCode'}">
									<input type="text" name="${map.value.columnName}" id="${map.value.columnName}" value="${document.documentDataMap[map.value.columnName].dataValue}" />
								</c:if>
							</c:when>
							<c:otherwise>
									<input type="text" name="${map.value.columnName}" id="${map.value.columnName}" value="${document[map.value.columnName]}" />
							</c:otherwise>
						</c:choose>
						</td>
					</c:otherwise>								
				</c:choose>
		
			</c:otherwise>
		</c:choose>
	</tr>
</c:forEach>
<c:if test="${documentType.data.isProduct == '1'}">
<!--  当documentType.flag == 1的时候，是产品文档，此时应当放入价格处理部分 -->
<%@include file="/WEB-INF/jsp/common/include/editPrice.jsp" %>

</c:if>


<tr>
	<td>指定模板</td>
	<td>
		<select name="templateId" id="templateId">
			<option value="0">不选择[将使用节点模版]</option>
			<c:forEach var="t" items="${templateList}">
				<option value="${t.templateId}" <c:if test="${t.templateId == document.templateId}"> selected="selected" </c:if> >${t.templateName}[${t.templateId}]</option>
			</c:forEach>
		</select>						
	</td>							
</tr>
