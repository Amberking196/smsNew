package com.server.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;
@Component
public class HtmlUtil {
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
	private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
	private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
	private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
	/**
	* @param htmlStr
	* @return
	* 删除Html标签
	*/
	public static String delHTMLTag(String htmlStr) {
	Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
	Matcher m_script = p_script.matcher(htmlStr);
	htmlStr = m_script.replaceAll(""); // 过滤script标签

	Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
	Matcher m_style = p_style.matcher(htmlStr);
	htmlStr = m_style.replaceAll(""); // 过滤style标签

	Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
	Matcher m_html = p_html.matcher(htmlStr);
	htmlStr = m_html.replaceAll(""); // 过滤html标签

	Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
	Matcher m_space = p_space.matcher(htmlStr);
	htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
	return htmlStr.trim(); // 返回文本字符串
	}

	public static String getTextFromHtml(String htmlStr){
	htmlStr = delHTMLTag(htmlStr);
	htmlStr = htmlStr.replaceAll("&nbsp;", "");
	// htmlStr = htmlStr.substring(0, htmlStr.indexOf("。")+1);
	int i=htmlStr.length();
	if(i<200) { 
		htmlStr=htmlStr.substring(0,i);
	}else {
	    htmlStr=htmlStr.substring(0,200);
	}
	return htmlStr;
	}
	
    public  static String getImg(String str) {
    	Pattern p_img = Pattern.compile("(<img[^>]+src\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>)");
    	Matcher m_img = p_img.matcher(str);
    	String f="";
    	while (m_img.find()) {
    	    f = m_img.group(2); //m_img.group(1) 为获得整个img标签  m_img.group(2) 为获得src的值
    	   break;
    	}
    	return f; 
    }
	public static void main(String[] args) {
	String str = "<div style='text-align:center;'> 整治“四风” 清弊除垢<br/>"
			+ "<img src='http://192341234.asfda.com'></img>"
			+ "<span style='font-size:14px;'> </span><span style='font-size:18px;'>"
			+ "公司召开党的群众路线教育实践活动动员大会</span><br/><title>我是标题"
			+ "</title><desc>我是desc里的内容</desc>"
			+ "<img src='asdfadsf'></img></div>";
	getImg(str);
    System.out.println(getTextFromHtml(str));
	} 
}
