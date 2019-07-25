sdad
                                 <c:when test="${empty document.documentDataMap.get('productBuyMoney').dataValue}">
                                            dataValue没钱的时候
                                            <div class="entry" itemprop="articleBody">
                                                    <p>
                                                        <a href="../../../theme/${theme}/images/learnor.png" class="highslide-image" onclick="return hs.expand(this);"><img data-tag="bdimg" class="alignnone size-full wp-image-7140" alt="learnor" style='border:0;padding: 11px;' src="../../../theme/${theme}/images/learnor.png" width="1000" height="1414" /></a>
                                                    </p>
                                                    <p>${document.content}</p>
                                                    <p style="cursor: pointer">
                                                        <c:set var="theString" value="${document.documentDataMap.subscribeContent.dataValue}"/>
                                                        <c:choose>
                                                            <c:when test="${fn:contains(theString, ',')}">
                                                                <c:set value="${fn:split(theString, ',') }" var="names" />
                                                                <c:forEach items="${names}" var="name">
                                                                        <c:if test="${fn:contains(name, '.mp4')}">
                                                                            <video width="95%" height="800" controls style="margin: 0 15px;">
                                                                                <source src="/file/${name}"  type="video/mp4">
                                                                            </video>
                                                                        </c:if>
                                                                        <c:if test="${fn:contains(name, '.f4v')}">
                                                                            <video width="95%" height="800" controls style="margin: 0 15px;">
                                                                                <source src="/file/${name}"  type="video">
                                                                            </video>
                                                                        </c:if>
                                                                        <c:if test="${fn:contains(name, '.jpg')}">
                                                                            <a href="/file/${name}">
                                                                                <img src="/file/${name}" alt="">
                                                                            </a>
                                                                        </c:if>
                                                                        <c:if test="${fn:contains(name, '.png')}">
                                                                            <a href="/file/${name}">
                                                                                <img src="/file/${name}" alt="">
                                                                            </a>
                                                                        </c:if>
                                                                </c:forEach>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <c:if test="${fn:contains(theString, '.mp4')}">
                                                                    <video width="95%" height="800" controls style="margin: 0 15px;">
                                                                        <source src="/file/${document.documentDataMap.subscribeContent.dataValue}"  type="video/mp4">
                                                                    </video>
                                                                </c:if>
                                                                <c:if test="${fn:contains(theString, '.f4v')}">
                                                                    <video width="95%" height="800" controls style="margin: 0 15px;">
                                                                        <source src="/file/${document.documentDataMap.subscribeContent.dataValue}"  type="video">
                                                                    </video>
                                                                </c:if>
                                                                <c:if test="${fn:contains(theString, '.jpg')}">
                                                                    <a href="/file/${document.documentDataMap.subscribeContent.dataValue}">
                                                                        <img src="/file/${document.documentDataMap.subscribeContent.dataValue}" alt="">
                                                                    </a>
                                                                </c:if>
                                                                <c:if test="${fn:contains(theString, '.png')}">
                                                                    <a href="/file/${document.documentDataMap.subscribeContent.dataValue}">
                                                                        <img src="/file/${document.documentDataMap.subscribeContent.dataValue}" alt="">
                                                                    </a>
                                                                </c:if>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </p>
                                                    <div class="old-message">请尊重我们的辛苦付出，未经允许，请不要转载 <a href="/" target="_blank" style='color: #31a030;'>七星教育</a> 的吉他谱和教学视频！</div>
                                            </div> 
                                        </c:when>