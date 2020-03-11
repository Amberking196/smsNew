package com.server.common.genCode;

import java.util.Date;

public class EntityCodeTemplate extends CodeTemplate {

	public EntityCodeTemplate(GenCode mysqlMetaData) {
		super(mysqlMetaData);
	}

	@Override
	String parse() {
		StringBuffer sb = new StringBuffer();
        sb.append("package " + packageOutPath + ";\r\n");
        sb.append("\r\n");
        sb.append("import com.server.common.persistence.Entity;");
        sb.append("\r\n");
        sb.append("import lombok.Data;");
        sb.append("\r\n");
        sb.append("import com.fasterxml.jackson.annotation.JsonIgnore;");
        sb.append("\r\n");
        
        // 注释部分
        sb.append("/**\r\n");
        sb.append(" * table name:  " + tableName + "\r\n");
        sb.append(" * author name: " + authorName + "\r\n");
        sb.append(" * create time: " + SDF.format(new Date()) + "\r\n");
        sb.append(" */ \r\n");
        // 实体部分
        sb.append("@Data");
        sb.append("\r\n");
        sb.append("@Entity(tableName=\""+tableName+"\",id=\"\",idGenerate=\"auto\")");
        sb.append("\r\n");
        sb.append("public class " +entityName+ "{\r\n\r\n");
        String select="select * from "+tableName+" where 1=1 \";";
        StringBuilder select1=new StringBuilder("select ");
        for (int i=0;i<colNames.length;i++){
        	select1.append(colNames[i]);
        	if(i!=colNames.length-1)
        	select1.append(",");
        }
        select1.append(" from "+tableName+" where 1=1 \";");
        sb.append("\r\n");
        sb.append("@JsonIgnore");
        sb.append("\tpublic String tableName=\""+tableName+"\";");
        sb.append("\r\n");
        sb.append("@JsonIgnore");
        sb.append("\tpublic String selectSql=\"").append(select);
        sb.append("\r\n");
        sb.append("@JsonIgnore");
        sb.append("\tpublic String selectSql1=\"").append(select1.toString());
        sb.append("\r\n");

        processAllAttrs(sb);// 属性
        sb.append("\r\n");
       // processAllMethod(sb);// get set方法
        sb.append("}\r\n");
        return sb.toString();
	}
	@Override
	String getFileName() {
		
		return className+"Bean.java";
	}
}
