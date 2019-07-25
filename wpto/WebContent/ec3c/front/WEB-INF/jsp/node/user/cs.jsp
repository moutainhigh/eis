        <div class="AccountTop">
            <div class="Wrap1000">
                <div class="AccountTop_Left">您好，<span id="lblLoginAccount">${frontUser.username}</span></div>
                <ul class="AccountTop_Nav">
                    <li><a href="/">首页</a></li>
                    <li class="Cur"><a href="/content/user/pcenter.shtml">地址管理</a></li>
                    <li><a href="/content/user/addressbook.shtml">我的订单</a></li>
                </ul>
            </div>
        </div>
        <div class="SpaceBlockHorizontal20"></div>
        <div class="Wrap1000">
            <div class="Account">
                <div class="Account_Title">新增地址</div>
                <div class="InputForm" id="addaress">
                	
                    <table>
                        <tr>
                            <th>账号：</th>
                            <td>
                                <span id="lblAccount">${frontUser.username}</span></td>
                        </tr>
                        <tr>
                            <th>收货人：</th>
                            <td>
                                <input name="contact" type="text" value="" maxlength="10" id="contact" class="InputWidth270" /></td>
                        </tr>
                        <tr>
                            <th>电话/手机：</th>
                            <td>
                                <input name="mobile" type="text" maxlength="11" id="mobile" class="InputWidth270" /></td>
                        </tr>
                        <tr>
                            <th>收货地址：</th>
                            <td>
                                <textarea name="address" rows="2" cols="20" id="address" class="MulText"></textarea></td>
                        </tr>
                        <tr>
                            <th></th>
                            <td></td>
                        </tr>
                        <tr>
                            <th></th>
                            <td>
                                <input type="submit" name="btnSubmit" value="确认添加" onclick="return ValidateInput();" id="btnSubmit" class="btnRed" /></td>
                        </tr>
                    </table>
                    
                </div>
            </div>