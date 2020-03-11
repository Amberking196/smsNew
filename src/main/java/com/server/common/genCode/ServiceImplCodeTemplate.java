package com.server.common.genCode;

import java.util.Date;


public class ServiceImplCodeTemplate extends CodeTemplate {

	public ServiceImplCodeTemplate(GenCode mysqlMetaData) {
		super(mysqlMetaData);
		// TODO Auto-generated constructor stub
	}

	@Override
	String parse() {
		    StringBuffer sb = new StringBuffer();
	        sb.append("package " + packageOutPath + ";\r\n");
	        sb.append("\r\n");
	        sb.append("import org.apache.commons.logging.Log;");
	        sb.append("\r\n");
	        sb.append("import org.apache.commons.logging.LogFactory;");
	        sb.append("\r\n");
	        sb.append("import org.springframework.beans.factory.annotation.Autowired;");
	        sb.append("\r\n");
	        sb.append("import org.springframework.stereotype.Service;");
	        sb.append("\r\n");
	        sb.append("import org.apache.commons.logging.LogFactory;");
	        sb.append("\r\n");
	        sb.append(" import java.util.List;");
	        sb.append("\r\n");
	        sb.append("import com.server.util.ReturnDataUtil;");
	        sb.append("\r\n");
	        
	       
	        
	        // 注释部分
	        sb.append("/**\r\n");
	        sb.append(" * author name: " + authorName + "\r\n");
	       // sb.append(" */ \r\n");
	        sb.append(" * create time: " + SDF.format(new Date()) + "\r\n");
	        sb.append(" */ \r\n");
	        // 实体部分
	        sb.append("@Service");
	        sb.append("\r\n");
	        sb.append("public class  " + className +"ServiceImpl  implements "+tableToObjectName(tableName, true)+"Service"+ "{\r\n\r\n");
	        sb.append("private static Log log = LogFactory.getLog("+tableToObjectName(tableName, true)+"ServiceImpl.class);");
	        sb.append("\r\n");
	        sb.append("@Autowired");
	        sb.append("\r\n");
	        sb.append("private "+className+"Dao "+objectName+"DaoImpl;");
	        sb.append("\r\n");
	        sb.append("public ReturnDataUtil listPage("+className+"Condition condition){");
	        sb.append("\r\n");	
	        sb.append("return "+objectName+"DaoImpl.listPage(condition);");
	        sb.append("\r\n");
	        sb.append("}");
	        
	        sb.append("\r\n");
	        sb.append("public "+entityName+" add("+entityName+" entity) {");
	        sb.append("\r\n");
	        sb.append("return "+objectName+"DaoImpl.insert(entity);");
	        sb.append("\r\n");
            sb.append("}");
	        sb.append("\r\n");
	        
	        sb.append("\r\n");
	        sb.append("public boolean update("+entityName+" entity) {");
	        sb.append("\r\n");
	        sb.append("return "+objectName+"DaoImpl.update(entity);");
	        sb.append("\r\n");
            sb.append("}");
	        sb.append("\r\n");
	        
	        sb.append("\r\n");
	        sb.append("public boolean del(Object id) {");
	        sb.append("\r\n");
	        sb.append("return "+objectName+"DaoImpl.delete(id);");
	        sb.append("\r\n");
            sb.append("}");
	        sb.append("\r\n");
	        
	        sb.append("\r\n");
	        sb.append("public List<"+entityName+"> list("+className+"Condition condition) {");
	        sb.append("\r\n");
	        sb.append("return null;");
	        sb.append("\r\n");
            sb.append("}");
	        sb.append("\r\n");
	        
	        sb.append("\r\n");
	        sb.append("public "+entityName+" get(Object id) {");
	        sb.append("\r\n");
	        sb.append("return "+objectName+"DaoImpl.get(id);");
	        sb.append("\r\n");
            sb.append("}");
	        sb.append("\r\n");
	        sb.append("}\r\n");
	        return sb.toString();
	}
	
	@Override
	String getFileName() {
		
		return className+"ServiceImpl.java";
	}

}
