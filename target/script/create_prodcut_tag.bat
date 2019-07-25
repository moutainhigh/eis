@echo off
rem 创建程序的PRODUCT标签
rem 用法 
rem 第一个参数，要创建的应用代码，如tongbao、yixian、与WebContent下的目录名一致\r\n
rem 第二个参数可选\r\n
rem 如果第二个参数是web 则只创建web页面的标签到
rem 如果第二个参数是java 则只创建java程序包的标签
rem 如果不输入第二个参数 将创建web页面和java程序包这二者的标签
rem 例如
:: target\script\create_product_tag.bat tongbao web
:: target\script\create_product_tag.bat tongbao java
:: target\script\create_product_tag.bat tongbao

set app=%1
if "%app%" == "" (
	echo Please input a valid application code to create tag
	goto end
)

if "%app%" == "yeele" (
	set internal="1"
)
if "%app%" == "yingxun" (
	set internal="1"
)



echo You can set 2nd parameter to web or java to create special tag only.
set mode=%2
if "%mode%" == "web" (
	echo Create %app%'s web tag only.
	svn delete svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/web -m ""
	svn copy svn://svn.maicard.com/eis/DEVELOP/wpt/WebContent/%1 svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/web -m ""	
	
	
	
	goto end
) else (
	if "%mode%" == "java" (
		echo Create %app%'s java programe tag only.
		svn delete svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/target -m ""
		svn copy svn://svn.maicard.com/eis/DEVELOP/target svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/target -m ""
		if defined internal (
			echo Copy internal module
			svn copy svn://svn.maicard.com/eis/DEVELOP/internal/eis-gp.jar svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/target -m ""
			svn copy svn://svn.maicard.com/eis/DEVELOP/internal/eis-sps.jar svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/target -m ""
			svn copy svn://svn.maicard.com/eis/DEVELOP/internal/eis-gallery.jar svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/target -m ""
		)

		goto end
	)
)
if "%mode%" == "" (
	echo Create %app%'s BOTH tag.
	svn delete svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/web -m ""
	svn copy svn://svn.maicard.com/eis/DEVELOP/wpt/WebContent/%1 svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/web -m ""	
	svn delete svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/target -m ""
	svn copy svn://svn.maicard.com/eis/DEVELOP/target svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/target -m ""
		if defined internal (
			echo Copy internal module
			svn copy svn://svn.maicard.com/eis/DEVELOP/internal/eis-gp.jar svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/target -m ""
			svn copy svn://svn.maicard.com/eis/DEVELOP/internal/eis-sps.jar svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/target -m ""
			svn copy svn://svn.maicard.com/eis/DEVELOP/internal/eis-gallery.jar svn://svn.maicard.com/eis/TAGS/%1/PRODUCT/target -m ""
	)

)

:end
set internal=
set mode=