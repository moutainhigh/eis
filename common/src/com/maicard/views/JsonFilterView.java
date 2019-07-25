package com.maicard.views;

/**
 * 用于对类的属性进行不同的输出
 * 1、未做任何@JsonView标记的属性，默认应当输出，这个可以通过修改objectMapper的配置来改变
 * 2、定义为某个级别才输出的属性，自动应用到扩展自它的Class
 * 例如，定义为Partner才输出的属性，不会在Front级别输出，但是会在Boss和Full级别输出
 * 
 *
 *
 * @author NetSnake
 * @date 2015年11月16日
 *
 */
public class JsonFilterView {
	public static class Front {};	//前端级别
	public static class Partner  extends Front {};	//合作伙伴级别
	public static class Boss extends Partner{};		//BOSS级别
	public static class Full extends Boss{};			//全部，标记为该级别才输出的属性，几乎不会被输出



}
