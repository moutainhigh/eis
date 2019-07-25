/**
 * Simple Map
 * 
 * 
 * var m = new Map(); m.put('key','value'); ... var s = "";
 * m.each(function(key,value,index){ s += index+":"+ key+"="+value+"\n"; });
 * alert(s);
 * 
 * @author xie_pkuan
 * @date 2013-04-01
 */
function Map() {
	/** 存放键的数组(遍历用到) */
	this.keys = new Array();
	/** 存放数据 */
	this.data = new Object();

	/**
	 * 放入一个键值对
	 * 
	 * @param {String}
	 *            key
	 * @param {Object}
	 *            value
	 */
	this.put = function(key, value) {
		if (this.data[key] == null) {
			this.keys.push(key);
		}
		this.data[key] = value;
	};

	/**
	 * 获取某键对应的值
	 * 
	 * @param {String}
	 *            key
	 * @return {Object} value
	 */
	this.get = function(key) {
		return this.data[key];
	};

	/**
	 * 删除一个键值对
	 * 
	 * @param {String}
	 *            key
	 */
	this.remove = function(key) {
		this.keys.remove(key);
		this.data[key] = null;
	};

	/**
	 * 遍历Map,执行处理函数
	 * 
	 * @param {Function}
	 *            回调函数 function(key,value,index){..}
	 */
	this.each = function(fn) {
		if (typeof fn != 'function') {
			return;
		}
		var len = this.keys.length;
		for (var i = 0; i < len; i++) {
			var k = this.keys[i];
			fn(k, this.data[k], i);
		}
	};

	/**
	 * 获取键值数组(类似Java的entrySet())
	 * 
	 * @return 键值对象{key,value}的数组
	 */
	this.entrys = function() {
		var len = this.keys.length;
		var entrys = new Array(len);
		for (var i = 0; i < len; i++) {
			entrys[i] = {
				key : this.keys[i],
				value : this.data[i]
			};
		}
		return entrys;
	};

	/**
	 * 判断Map是否为空
	 */
	this.isEmpty = function() {
		return this.keys.length == 0;
	};

	/**
	 * 获取键值对数量
	 */
	this.size = function() {
		return this.keys.length;
	};

	/**
	 * 重写toString
	 */
	this.toString = function() {
		var s = "{";
		for (var i = 0; i < this.keys.length; i++, s += ',') {
			var k = this.keys[i];
			s += (k + "=" + this.data[k]);
		}
		s += "}";
		return s;
	};

	/**
	 * 清空Map
	 */
	this.clear = function() {
		var idx = this.keys.length;
		for (idx; idx > -1; idx--) {
			var k = this.keys[idx];
			this.remove(k);
		}
	};

	/**
	 * 
	 * @param {Object}
	 *            key
	 * @return {Boolean}
	 */
	this.containKey = function(key) {
		return this.keys.contains(key);
	};

	/**
	 * 
	 * @param {Object} value
	 * @return {Boolean}
	 */
	this.containValue = function(value) {
		for (var idx = 0; idx < this.keys.length; idx++) {
			var k = this.keys[idx];
			if(this.data[k] == value)
				return true;
		}
		return false;
	};
}