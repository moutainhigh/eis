function Timer(time,id,curTime){
	this.day_elem = $(id).find('.day');
	this.hour_elem = $(id).find('.hour');
	this.minute_elem = $(id).find('.minute');
	this.second_elem = $(id).find('.second');	
	curTime=new Date(curTime.replace(/-/g,'/')).getTime();
	this.end_time = new Date(time.replace(/-/g,'/')).getTime();//月份是实际月份-1
	this.sys_second = (this.end_time-curTime)/1000;
	this.parentContainer=$(id);
}
Timer.prototype.start=function(){
	    var self=this;
		var t=setInterval(function(){
		if (self.sys_second >= 1) {
			self.sys_second -= 1;
			self.day = Math.floor((self.sys_second / 3600) / 24);
			self.hour = Math.floor((self.sys_second / 3600) % 24);
			self.minute = Math.floor((self.sys_second / 60) % 60);
			self.second = Math.floor(self.sys_second % 60);
			self.day_elem && $(self.day_elem).text(self.day);//计算天
			$(self.hour_elem).text(self.hour<10?"0"+self.hour:self.hour);//计算小时
			$(self.minute_elem).text(self.minute<10?"0"+self.minute:self.minute);//计算分钟
			$(self.second_elem).text(self.second<10?"0"+self.second:self.second);//计算秒杀 

		} else {
			  self.parentContainer.parent().html("<div class='statusBox'><span class='iconIng'>进行中</span></div>")
			  clearInterval(t);
			  //location.reload();
		}}, 1000);
	}
