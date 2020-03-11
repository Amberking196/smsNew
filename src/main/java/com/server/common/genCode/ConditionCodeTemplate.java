package com.server.common.genCode;

import java.util.Date;

public class ConditionCodeTemplate extends CodeTemplate {

	public ConditionCodeTemplate(GenCode mysqlMetaData) {
		super(mysqlMetaData);
		// TODO Auto-generated constructor stub
	}

	@Override
	String parse() {
		 StringBuffer sb = new StringBuffer();
	        sb.append("package " + packageOutPath + ";\r\n");
	        sb.append("\r\n");
	        
	        sb.append("import com.server.module.commonBean.PageAssist;");
	        sb.append("\r\n");
	        // 注释部分
	        sb.append("/**\r\n");
	        sb.append(" * table name:  " + tableName + "\r\n");
	        sb.append(" * author name: " + authorName + "\r\n");
	        sb.append(" * create time: " + SDF.format(new Date()) + "\r\n");
	        sb.append(" */ \r\n");
	        // 实体部分
	        
	        
	        sb.append("\r\n");
	        sb.append("public class " + className +"Condition extends PageAssist"+ "{\r\n\r\n");
	       
	        sb.append("\r\n");
	        sb.append("}\r\n");
	        return sb.toString();
	}

	@Override
	String getFileName() {
		
		return className+"Condition.java";
	}

}
