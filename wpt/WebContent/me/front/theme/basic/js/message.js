        $(function() {
            // 选项卡切换
            $('#post_message').click(function() {
                $(this).css({
                    'background': '#31a030',
                    'color': '#ffffff'
                });
                $('#set-box,#email-box,#public-tell').css({
                    'background': '#EDEDED',
                    'color': '#333'
                });
                $('#fep-content .messagebox').css('display','none');
                $('#fep-content .messageboxhave').css('display','none');
                $('#fep-content .public').css('display','none');
                $('#fep-content .oneLove').css('display','none');
                $('#fep-content .from').css('display','block');
                $('#fep-content .replyMessage').css('display','none');
            })
            $('#email-box').click(function() {
                $(this).css({
                    'background': '#31a030',
                    'color': '#ffffff'
                });
                $('#post_message,#set-box,#public-tell').css({
                    'background': '#EDEDED',
                    'color': '#333'
                });
                $('#fep-content .public').css('display','none');
                $('#fep-content .oneLove').css('display','none');
                $('#fep-content .from').css('display','none');
                $('#fep-content .replyMessage').css('display','none');
                $('.comments-loading').css('display','block');
                var uuid = $('#uuid').val();
                var userMessage = '';
                var userMessageRead = '';
                var userMessageSent = '';
                    $.ajax({
                        type: "get",
                        url: '/userMessage/list.json',
                        data: {
                            uuid:uuid
                        },
                        dataType: 'json',
                        // async: false,
                        success: function(data) {
                            $('.comments-loading').css('display','none');
                            if(data.totalSizeNum == 0){
                                $('#fep-content .messagebox').css('display','block');
                                return false;
                            }else{
                                $('#totalSize').text(data.totalSizeNum);
                                $('#fep-content .messageboxhave').css('display','block');
                                $('#fep-content .messageboxhave table tbody').empty();
                                if(data.userMessage.length != 0){
                                    for(var i = 0;i<data.userMessage.length;i++){
                                        userMessage +="<tr class='trodd"+i+"'>";
                                        userMessage += "<td>"+data.userMessage[i].senderName+"</td>";
                                        userMessage +="<td><a href='#'>"+data.userMessage[i].receiverName+"</a></td>";
                                        userMessage += "<td><a href='#' class='notRead'>"+data.userMessage[i].title+"<input type='hidden' value='"+data.userMessage[i].content+"'/></a><br/><small>未读</small></td>";
                                        userMessage += "<td>"+data.userMessage[i].receiverName+"<br/><small>4分钟 前</small></td>";
                                        userMessage +="<td><a href='#' class='deleteMessage'>删除</a><input type='hidden' value='"+data.userMessage[i].messageId+"' /></td>"
                                        userMessage +="</tr>";
                                    }
                                }
                                if(data.userMessageRead.length != 0){
                                    for(var i = 0;i<data.userMessageRead.length;i++){
                                        // console.log(data.userMessage[i].receiveTime);
                                        // console.log(data.userMessage[i].sendTime);
                                        userMessageRead +="<tr class='trodd"+i+"'>";
                                        userMessageRead += "<td>"+data.userMessageRead[i].senderName+"</td>";
                                        userMessageRead +="<td><a href='#'>"+data.userMessageRead[i].receiverName+"</a></td>";
                                        userMessageRead += "<td><a href='#' class='notRead'>"+data.userMessageRead[i].title+"<input type='hidden' value='"+data.userMessageRead[i].content+"'/></a><br/><small>已读</small></td>";
                                        userMessageRead += "<td>"+data.userMessageRead[i].receiverName+"<br/><small>4分钟 前</small></td>";
                                        userMessageRead +="<td><a href='#' class='deleteMessage'>删除</a><input type='hidden' value='"+data.userMessageRead[i].messageId+"' /></td>"
                                        userMessageRead +="</tr>";
                                    }
                                }
                                if(data.userMessageSent.length != 0){
                                    for(var i = 0;i<data.userMessageSent.length;i++){
                                        userMessageSent +="<tr class='trodd"+i+"'>";
                                        userMessageSent += "<td>"+data.userMessageSent[i].senderName+"</td>";
                                        userMessageSent +="<td><a href='#'>"+data.userMessageSent[i].receiverName+"</a></td>";
                                        userMessageSent += "<td><a href='#' class='notRead'>"+data.userMessageSent[i].title+"<input type='hidden' value='"+data.userMessageSent[i].content+"'/></a><br/><small>已发送</small></td>";
                                        userMessageSent += "<td>"+data.userMessageSent[i].receiverName+"<br/><small>4分钟 前</small></td>";
                                        userMessageSent +="<td><a href='#' class='deleteMessage'>删除</a><input type='hidden' value='"+data.userMessageSent[i].messageId+"' /></td>"
                                        userMessageSent +="</tr>";
                                    }
                                }
                                if($('#fep-content .messageboxhave tr').length > 1){
                                    return false;
                                }else{
                                    $('.messageboxhave table tbody').append(userMessage);
                                    $('.messageboxhave table tbody').append(userMessageRead);
                                    $('.messageboxhave table tbody').append(userMessageSent);
                                }  
                                          
                                // 删除信箱条数
                                $('.deleteMessage').each(function(){
                                    $(this).click(function(){
                                        var messageId = $(this).siblings('input').val();
                                        $.ajax({
                                            type: "get",
                                            url: '/userMessage/delete.json',
                                            data: {
                                                messageId:messageId
                                            },
                                            dataType: 'json',
                                            // async: false,
                                            success: function(data) {
                                                if (data.message.operateCode == 102008) {
                                                    $('#email-box').click();
                                                }else{
                                                    alert(data.message.message); 
                                                }
                                            },
                                            error: function(XMLResponse) {
                                                alert("操作失败:" + XMLResponse.responseText);
                                            },
                                        }, 'json');
                                    })
                                })

                                // 未读点击已读
                                $('.notRead').each(function(){
                                    $(this).click(function(){
                                        $('#fep-content .replyMessage').css('display','block');
                                        $('#fep-content .messageboxhave').css('display','none');
                                        $('.sendName').text($(this).parent().siblings('td').eq(0).text());
                                        $('.pmtext span').text($(this).text());
                                        $('.pmtext p').text($(this).children('input').val());
                                        if($(this).parent('td').find('small').text() == '未读'){
                                            var messageId = $(this).parent().siblings('td').eq(3).find('input').val();
                                            $.ajax({
                                                type: "get",
                                                url: '/userMessage/clickRead.json',
                                                data: {
                                                    messageId:messageId
                                                },
                                                dataType: 'json',
                                                // async: false,
                                                success: function(data) {
                                                    if (data.message.operateCode == 102008) {
                                                        $('#fep-content .messageboxhave table tbody').empty();
                                                    }else{
                                                        alert(data.message.message); 
                                                    }
                                                },
                                                error: function(XMLResponse) {
                                                    alert("操作失败:" + XMLResponse.responseText);
                                                },
                                            }, 'json');
                                        }
                                    })
                                })
                            }
                        },
                        error: function(XMLResponse) {
                            alert("操作失败:" + XMLResponse.responseText);
                        },
                    }, 'json');
            })
            $('#public-tell').click(function() {
                $(this).css({
                    'background': '#31a030',
                    'color': '#ffffff'
                });
                $('#post_message,#email-box,#set-box').css({
                    'background': '#EDEDED',
                    'color': '#333'
                });
                $('#fep-content .messagebox').css('display','none');
                $('#fep-content .messageboxhave').css('display','none');
                $('#fep-content .public').css('display','block');
                $('#fep-content .oneLove').css('display','none');
                $('#fep-content .from').css('display','none');
                $('#fep-content .replyMessage').css('display','none');
            })
            $('#set-box').click(function() {
                $(this).css({
                    'background': '#31a030',
                    'color': '#ffffff'
                });
                $('#post_message,#email-box,#public-tell').css({
                    'background': '#EDEDED',
                    'color': '#333'
                });
                $('#fep-content .messagebox').css('display','none');
                $('#fep-content .messageboxhave').css('display','none');
                $('#fep-content .public').css('display','none');
                $('#fep-content .oneLove').css('display','block');
                $('#fep-content .from').css('display','none');
                $('#fep-content .replyMessage').css('display','none');
            })
        })

        function b() {
            var color = '[b][/b]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function i() {
            var color = '[i][/i]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function u() {
            var color = '[u][/u]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function s() {
            var color = '[s][/s]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function code() {
            var color = '[code][/code]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function quote() {
            var color = '[quote][/quote]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function list() {
            var color = '[list][/list]';

            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function lover() {
            var color = '[*]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function url() {
            var color = '[url][/url]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function img() {
            var color = '[img][/img]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function email() {
            var color = '[email][/email]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function color() {
            var color = '[color=#][/color]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        function embed() {
            var color = '[embed][/embed]';
            var long = $('textarea').val().indexOf('][/') + 1;
            var arr = $('textarea').val().split('');
            arr.splice(long, 0, color);
            $('textarea').val(arr.join(''));
        }

        // 站内信息发送按钮
        function send() {
            var message_to = $('.from input[name="message_to"]').val();
            var message_title = $('.from input[name="message_title"]').val();
            var message_content = $('.from textarea[name="message_content"]').val();
            var uuid = $('#uuid').val();
            var name = $('.user-avatar p').text();
            if (message_to.length == 0 || message_title.length == 0 || message_content == '') {
                alert('还有未填的信息，请填写完整后发布！')
            }else if(message_to == name){
                 alert("不能向自己发送邮件！");
            }else{
                $.ajax({
                    type: "post",
                    url: '/user/sendEmail.json',
                    data: {
                        receiverName:message_to,
                        title:message_title,
                        content:message_content,
                        uuid:uuid
                    },
                    dataType: 'json',
                    // async: false,
                    success: function(data) {
                        if (data.message.operateCode == 500016) {
                                alert("登录成功！")
                                window.location.href = "/content/friend/upload/index.shtml";
                        } else {
                            alert(data.message.message);
                            window.location.reload();
                        }
                    },
                    error: function(XMLResponse) {
                        alert("操作失败:" + XMLResponse.responseText);
                    },
                }, 'json');
            }
        }

        // 回复邮件
        function reply(){
            var message_to = $('.sendName').text();
            var message_title = $('.pmtext span').text();
            var message_content = $('.replyMessage form textarea').val();
            var uuid = $('#uuid').val();
            var name = $('.user-avatar p').text();
            if (message_to.length == 0 || message_title.length == 0 || message_content == '') {
                alert('还有未填的信息，请填写完整后发布！')
            }else if(message_to == name){
                 alert("不能向自己发送邮件！");
            }else{
                $.ajax({
                    type: "post",
                    url: '/user/sendEmail.json',
                    data: {
                        receiverName:message_to,
                        title:message_title,
                        content:message_content,
                        uuid:uuid
                    },
                    dataType: 'json',
                    // async: false,
                    success: function(data) {
                        if (data.message.operateCode == 500016) {
                                alert("登录成功！")
                                window.location.href = "/content/friend/upload/index.shtml";
                        } else {
                            alert(data.message.message);
                            window.location.reload();
                        }
                    },
                    error: function(XMLResponse) {
                        alert("操作失败:" + XMLResponse.responseText);
                    },
                }, 'json');
            }
        }

        // 保存设置
        function set() {
            var uuid = $('#uuid').val();
            var current = '';
            if($('.oneLove ul li input[type="checkbox"]').is(':checked')){
                 current = 'Y';
            }else{
                current = 'N';
            }
            $.ajax({
                type: "post",
                url: '/user/updateMessageSet.json',
                data: {
                    uuid:uuid,
                    allowOtherToSelf:current
                },
                dataType: 'json',
                // async: false,
                success: function(data) {
                    if (data.message.operateCode == 102008) {
                        alert(data.message.message);
                    }else{
                        alert(data.message.message); 
                    }
                },
                error: function(XMLResponse) {
                    alert("操作失败:" + XMLResponse.responseText);
                },
            }, 'json');
        }
        


