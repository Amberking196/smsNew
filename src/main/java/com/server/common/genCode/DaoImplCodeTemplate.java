package com.server.common.genCode;

import java.util.Date;
import java.util.List;

public class DaoImplCodeTemplate extends CodeTemplate {

	public DaoImplCodeTemplate(GenCode mysqlMetaData) {
		super(mysqlMetaData);
		// TODO Auto-generated constructor stub
	}

	@Override
	String parse() {
		 StringBuffer sb = new StringBuffer();
	        sb.append("package " + packageOutPath + ";\r\n");
	        sb.append("\r\n");
	        sb.append("import com.server.common.persistence.BaseDao;");
	        sb.append("\r\n");
	        sb.append("import com.server.dbpool.DBPool;");
	        sb.append("\r\n");
	        sb.append("import com.server.module.system.companyManage.CompanyDao;");
	        sb.append("\r\n");
	        sb.append("import com.server.util.ReturnDataUtil;");
	        sb.append("\r\n");
	        sb.append("import com.server.util.StringUtil;");
	        sb.append("\r\n");
	        sb.append("import java.sql.Connection;");
	        sb.append("\r\n");
	        sb.append("import java.sql.PreparedStatement;");
	        sb.append("\r\n");
	        sb.append("import com.server.common.persistence.BaseDao;");
	        sb.append("\r\n");
	        sb.append("import java.sql.ResultSet;");
	        sb.append("\r\n");
	        sb.append("import java.sql.SQLException;");
	        sb.append("\r\n");
	        sb.append("import org.apache.commons.logging.Log;");
	        sb.append("\r\n");
	        sb.append("import org.apache.commons.logging.LogFactory;");
	        sb.append("\r\n");
	        sb.append("import org.springframework.stereotype.Repository;");
	        sb.append("\r\n");
	        sb.append("import com.google.common.collect.Lists;");
	        sb.append("\r\n");
	        sb.append("import com.server.util.ReturnDataUtil;");
	        sb.append("\r\n");
	        sb.append("import java.util.List;");
	        sb.append("\r\n");
	        
	        
	        // 注释部分
	        sb.append("/**\r\n");
	        sb.append(" * author name: " + authorName + "\r\n");
	        sb.append(" * create time: " + SDF.format(new Date()) + "\r\n");
	        sb.append(" */ \r\n");
	        // 实体部分
	        sb.append("@Repository");
	        sb.append("\r\n");
	        sb.append("public class  " + className +"DaoImpl extends BaseDao<"+tableToObjectName(tableName, true)+"Bean> implements "+tableToObjectName(tableName, true)+"Dao"+ "{\r\n\r\n");
	        sb.append("private static Log log = LogFactory.getLog("+className+"DaoImpl.class);");
	        sb.append("\r\n");
	        sb.append("public ReturnDataUtil listPage("+className+"Condition condition) {");
	        sb.append("\r\n");
	        sb.append("ReturnDataUtil data=new ReturnDataUtil();");
	        sb.append("\r\n");
	        sb.append("StringBuilder sql=new StringBuilder();");
	        sb.append("\r\n");
	        StringBuilder select1=new StringBuilder("select ");
	        for (int i=0;i<colNames.length;i++){
	        	select1.append(colNames[i]);
	        	if(i!=colNames.length-1)
	        	select1.append(",");
	        }
	        select1.append(" from "+tableName+" where 1=1 ");
	       
	        sb.append("sql.append(\""+select1.toString()+"\");");
	        
	        sb.append("\r\n");
	        sb.append("List<Object> plist=Lists.newArrayList();");
	        sb.append("\r\n");
	        sb.append("Connection conn=null;");
	        sb.append("\r\n");
	        sb.append("PreparedStatement pst=null;");
	        sb.append("\r\n");
	        sb.append("ResultSet rs=null;");
	        sb.append("\r\n");
	        sb.append("try {");
	        sb.append("\r\n");
	        sb.append("conn=openConnection();");
	        sb.append("\r\n");
	        sb.append("pst=conn.prepareStatement(super.countSql(sql.toString()));");
	        sb.append("\r\n");
	        sb.append("if (plist!=null && plist.size()>0)");
	        sb.append("\r\n");
	        sb.append("for (int i=0;i<plist.size();i++){");
	        sb.append("\r\n");
	        sb.append("pst.setObject(i+1, plist.get(i));");
	       
	        sb.append("\r\n");
	        sb.append("}");
	        sb.append("\r\n");
	        sb.append("rs = pst.executeQuery();");
	        sb.append("\r\n");
	        sb.append("long count=0;");
	        sb.append("\r\n");
	        sb.append("while(rs.next()){");
	        sb.append("\r\n");
	        sb.append("count=rs.getInt(1);");
	        sb.append("\r\n");
	        sb.append("}");
	        sb.append("\r\n");
	        sb.append("long off=(condition.getCurrentPage()-1)*condition.getPageSize();");
	        sb.append("\r\n");
	        sb.append("pst=conn.prepareStatement(sql.toString()+\" limit \"+off+\",\"+condition.getPageSize());");
	        sb.append("\r\n");
	        sb.append("\r\n");
	        sb.append("if (plist!=null && plist.size()>0)");
	        sb.append("\r\n");
	        sb.append("for (int i=0;i<plist.size();i++){");
	        sb.append("\r\n");
	        sb.append("pst.setObject(i+1, plist.get(i));");
	        sb.append("}");
	        sb.append("\r\n");
	        sb.append("rs = pst.executeQuery();");
	        sb.append("\r\n");
	        sb.append("List<"+entityName+"> list=Lists.newArrayList();");
	        
				
	        sb.append("\r\n");
	        sb.append("while(rs.next()){");
	        sb.append("\r\n");
	        sb.append(entityName+" bean = new "+entityName+"();");
	        sb.append("\r\n");
	 
	        for(int i=0;i<colNames.length;i++){//colTypes
	        	
	        	String type=tableToObjectName(sqlType2JavaType(colTypes[i]), true);
	        	if(type.equals("Integer")){
	        		type="Int";
	        	}
	        	sb.append("bean.set"+tableToObjectName(colNames[i], true)+"(rs.get"+type+"(\""+colNames[i]+"\"));");
	        	 sb.append("\r\n");
	        }
	       
	        sb.append(" list.add(bean);");
	        sb.append("\r\n");
	        sb.append("}");
			
	        sb.append("\r\n");
	        sb.append(" if (showSql){");
	        sb.append("\r\n");
	        sb.append("log.info(sql);");
	        sb.append("\r\n");
	        sb.append("log.info(plist.toString());");
	        sb.append("\r\n");
	        sb.append("}");
	        sb.append("\r\n");
	        sb.append("data.setCurrentPage(condition.getCurrentPage());");
	        sb.append("\r\n");
	        sb.append("data.setTotal(count);");
	        sb.append("\r\n");
	        sb.append("data.setReturnObject(list);");
	        sb.append("\r\n");
	        sb.append("data.setStatus(1);");
	        sb.append("\r\n");
	        sb.append("return data;");
	        sb.append("\r\n");
	        sb.append("} catch (SQLException e) {");
	        sb.append("\r\n");
	        sb.append("e.printStackTrace();");
	        sb.append("\r\n");
	        sb.append("log.error(e.getMessage());");
	        sb.append("\r\n");
	        sb.append("return data;");
	        sb.append("\r\n");
	        sb.append("} finally{");
	        sb.append("\r\n");
	        sb.append("try {");
	        sb.append("\r\n");
	        sb.append("rs.close();");
	        sb.append("\r\n");
	        sb.append("pst.close();");
	        sb.append("\r\n");
	        sb.append("closeConnection(conn);");
	        sb.append("\r\n");
	        sb.append("} catch (SQLException e) {");
	        sb.append("\r\n");
	        sb.append("e.printStackTrace();");
	        sb.append("\r\n");
	        sb.append("}");
	        sb.append("\r\n");
	        sb.append("}");
	        sb.append("\r\n");
	        sb.append("}");
	        sb.append("\r\n");
	        
	        sb.append("\r\n");
	        sb.append("public "+entityName+" get(Object id) {");
	        sb.append("\r\n");
	        sb.append("return super.get(id);");
	        sb.append("\r\n");
	        sb.append("}");
	        
	        sb.append("\r\n");
	        sb.append("public boolean delete(Object id) {");
	        sb.append("\r\n");
	        sb.append(entityName+" entity=new "+entityName+"();");
	        sb.append("\r\n");
	        sb.append("return super.del(entity);");
	        sb.append("\r\n");
	        sb.append("}");
	        
	        sb.append("\r\n");
	        sb.append("public boolean update("+entityName+" entity) {");
	        sb.append("\r\n");
	        sb.append("return super.update(entity);");
	        sb.append("\r\n");
	        sb.append("}");    
	        
	        sb.append("\r\n");
	        sb.append("public "+entityName+" insert("+entityName+" entity) {");
	        sb.append("\r\n");
	        sb.append("return super.insert(entity);");
	        sb.append("\r\n");
	        sb.append("}");  
	        sb.append("\r\n");
	        sb.append("public List<"+entityName+"> list("+className+"Condition condition) {");
	        sb.append("\r\n");
	        sb.append("return null;");
	        sb.append("\r\n");
	        sb.append("}");  
	     
	        sb.append("}\r\n");
	        return sb.toString();
	}
	@Override
	String getFileName() {
		
		return className+"DaoImpl.java";
	}

}
