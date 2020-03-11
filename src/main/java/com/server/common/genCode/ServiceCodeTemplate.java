package com.server.common.genCode;

import java.util.Date;


public class ServiceCodeTemplate extends CodeTemplate {

	public ServiceCodeTemplate(GenCode mysqlMetaData) {
		super(mysqlMetaData);
		// TODO Auto-generated constructor stub
	}

	@Override
	String parse() {
		StringBuffer sb = new StringBuffer();
        sb.append("package " + packageOutPath + ";\r\n");
        sb.append("\r\n");
        sb.append("import java.util.List;");
        sb.append("\r\n");
        sb.append("import com.server.util.ReturnDataUtil;");
        // 注释部分
        sb.append("/**\r\n");
        sb.append(" * author name: " + authorName + "\r\n");
        sb.append(" * create time: " + SDF.format(new Date()) + "\r\n");
        sb.append(" */ \r\n");
        // 实体部分
        sb.append("public interface  " + className +"Service"+ "{\r\n\r\n");
        sb.append("\r\n");
        sb.append("public ReturnDataUtil listPage("+className+"Condition condition);");
        sb.append("\r\n");
        sb.append("public List<"+entityName+"> list("+className+"Condition condition);");
        sb.append("\r\n");
        sb.append("public boolean update("+entityName+" entity);");
        sb.append("\r\n");
        sb.append("public boolean del(Object id);");
        sb.append("\r\n");
        sb.append("public "+entityName+" get(Object id);");
        sb.append("\r\n");
        sb.append("public "+entityName+" add("+entityName+" entity);");
        sb.append("\r\n");
        
        	
        sb.append("}\r\n");
        return sb.toString();
	}
	@Override
	String getFileName() {
		
		return className+"Service.java";
	}

}
