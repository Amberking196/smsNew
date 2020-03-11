package com.server.common.genCode;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;

import lombok.Data;
@Data
abstract class CodeTemplate {
	GenCode mysqlMetaData;
	String baseDir = "";
	String fileName = "";
	String zwtitle="";
	String authorName = "yjr";// 作者名字
	String className = "";
	String objectName = "";
	String entityName="";
	String packageOutPath = "";// 指定实体生成所在包的路径
	String tableName;// 表名
	String[] colNames; // 列名数组
	String[] colTypes; // 列名类型数组
	int[] colSizes; // 列名大小数组
    static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	abstract String parse();
	abstract String getFileName();
	public CodeTemplate(GenCode mysqlMetaData){
		this.mysqlMetaData=mysqlMetaData;
		copy(this.mysqlMetaData);
		//this.fileName=this.className+"Bean.java";
	}
    public void copy(GenCode src){
    	this.baseDir=src.baseDir;
    	this.className=src.className;
    	this.colNames=src.getColNames();
    	this.colTypes=src.getColTypes();
    	this.colSizes=src.getColSizes();
    	this.objectName=src.getObjectName();
    	this.packageOutPath=src.getPackageOutPath();
    	this.objectName=src.getObjectName();
    	this.tableName=src.getTableName();
    	this.entityName=src.getClassName()+"Bean";
    	this.zwtitle=src.getZwtitle();
    	
    }
	public void writeToFile() {
		
		PrintWriter pw = null;
		String content = parse();
		String outputPath = baseDir + "/" +getFileName();
		System.out.println("outputpath:" + outputPath);
		try {
			FileWriter fw = new FileWriter(outputPath);
			pw = new PrintWriter(fw);
			pw.println(content);
			pw.flush();
			System.out.println("create class >>>>> " + fileName);
		} catch (Exception e) {
           e.printStackTrace();
		}

		if (pw != null)
			pw.close();
	}
	
	/**
     * @param sb
     * @description 生成所有成员变量
     * @author paul
     * @date 2017年8月18日 下午5:15:04
     * @update 2017年8月18日 下午5:15:04
     * @version V1.0
     */
    void processAllAttrs(StringBuffer sb) {
        for (int i = 0; i < colNames.length; i++) {
            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + tableToObjectName(colNames[i], false) + ";\r\n");
        }
    }
    
    /**
     * @return
     * @description 查找sql字段类型所对应的Java类型
     * @author paul
     * @date 2017年8月18日 下午4:55:41
     * @update 2017年8月18日 下午4:55:41
     * @version V1.0
     */
    protected String sqlType2JavaType(String sqlType) {
        if (sqlType.equalsIgnoreCase("bit")) {
            return "boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("bigint unsigned")) {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "Float";
        } else if (sqlType.equalsIgnoreCase("double")) {
            return "Double";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric")
                || sqlType.equalsIgnoreCase("real") || sqlType.equalsIgnoreCase("money")
                || sqlType.equalsIgnoreCase("smallmoney")) {
            return "BigDecimal";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char")
                || sqlType.equalsIgnoreCase("nvarchar") || sqlType.equalsIgnoreCase("nchar")
                || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("timestamp")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("date")) {
            return "Date";
        }else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        }
        return null;
    }
    /**
     * @param str 传入字符串
     * @return
     * @description 将传入字符串的首字母转成大写
     * @author paul
     * @date 2017年8月18日 下午5:12:12
     * @update 2017年8月18日 下午5:12:12
     * @version V1.0
     */
    private String initCap(String str) {
        char[] ch = str.toCharArray();
        if (ch[0] >= 'a' && ch[0] <= 'z')
            ch[0] = (char) (ch[0] - 32);
        return new String(ch);
    }
    /**
     * @return
     * @description 将mysql中表名和字段名转换成驼峰形式
     * @author paul
     * @date 2017年8月18日 下午4:55:07
     * @update 2017年8月18日 下午4:55:07
     * @version V1.0
     */
    String tableToObjectName(String before, boolean firstChar2Upper) {
        //不带"_"的字符串,则直接首字母大写后返回
        if (!before.contains("_"))
            return firstChar2Upper ? initCap(before) : before;
        String[] strs = before.split("_");
        StringBuffer after = null;
        if (firstChar2Upper) {
            after = new StringBuffer(initCap(strs[0]));
        } else {
            after = new StringBuffer(strs[0]);
        }
        if (strs.length > 1) {
            for (int i=1; i<strs.length; i++)
                after.append(initCap(strs[i]));
        }
        return after.toString();
    }
  
}
