package com.server.common.genCode;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.text.SimpleDateFormat;

import lombok.Data;
@Data
public class GenCode {
    private static final GenCode INSTANCE = new GenCode();

    private String tableName;// 表名
    private String[] colNames; // 列名数组
    private String[] colTypes; // 列名类型数组
    private int[] colSizes; // 列名大小数组
    private boolean needUtil = false; // 是否需要导入包java.util.*
    private boolean needSql = false; // 是否需要导入包java.sql.*
    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final String SQL = "SELECT * FROM ";// 数据库操作

    // TODO 需要修改的地方
    private static final String URL = "jdbc:mysql://192.168.0.104:3306/ys";
    private static final String NAME = "root";
    private static final String PASS = "123456";
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    public String packageOutPath = "com.server.module.system.machineManage";// 指定实体生成所在包的路径
    private String authorName = "yjr";// 作者名字
    public String baseDir="C:/code";
    
    public String className="";
    public String objectName="";
    public String zwtitle="";

    /**
     * 类的构造方法
     */
    private GenCode() {
    }

   

    /**
     * @param sb
     * @description 生成所有成员变量
     * @author paul
     * @date 2017年8月18日 下午5:15:04
     * @update 2017年8月18日 下午5:15:04
     * @version V1.0
     */
    private void processAllAttrs(StringBuffer sb) {
        for (int i = 0; i < colNames.length; i++) {
            sb.append("\tprivate " + sqlType2JavaType(colTypes[i]) + " " + tableToObjectName(colNames[i], false) + ";\r\n");
        }
    }

    /**
     * @param sb
     * @description 生成所有get/set方法
     * @author paul
     * @date 2017年8月18日 下午5:14:47
     * @update 2017年8月18日 下午5:14:47
     * @version V1.0
     */
   /* private void processAllMethod(StringBuffer sb) {
        for (int i = 0; i < colNames.length; i++) {
            sb.append("\tpublic void set" + getTransStr(colNames[i], true) + "(" + sqlType2JavaType(colTypes[i]) + " "
                    + getTransStr(colNames[i], false) + "){\r\n");
            sb.append("\t\tthis." + getTransStr(colNames[i], false) + "=" + getTransStr(colNames[i], false) + ";\r\n");
            sb.append("\t}\r\n");
            sb.append("\tpublic " + sqlType2JavaType(colTypes[i]) + " get" + getTransStr(colNames[i], true) + "(){\r\n");
            sb.append("\t\treturn " + getTransStr(colNames[i], false) + ";\r\n");
            sb.append("\t}\r\n");
        }
    }*/

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
    private String tableToObjectName(String before, boolean firstChar2Upper) {
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

    /**
     * @return
     * @description 查找sql字段类型所对应的Java类型
     * @author paul
     * @date 2017年8月18日 下午4:55:41
     * @update 2017年8月18日 下午4:55:41
     * @version V1.0
     */
    private String sqlType2JavaType(String sqlType) {
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
        } else if (sqlType.equalsIgnoreCase("datetime")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("date")) {
            return "Date";
        }else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        }
        return null;
    }

    /**
     * 
     * @description 生成方法
     * @author paul
     * @date 2017年8月18日 下午2:04:20
     * @update 2017年8月18日 下午2:04:20
     * @version V1.0
     * @throws Exception
     */
    private void generate(String table) throws Exception {
        //与数据库的连接
        Connection con;
        PreparedStatement pStemt = null;
        Class.forName(DRIVER);
        con = DriverManager.getConnection(URL, NAME, PASS);
        System.out.println("connect database success...");
        //获取数据库的元数据
        DatabaseMetaData db = con.getMetaData();
        //从元数据中获取到所有的表名
        String tableSql=SQL+table;
        this.tableName=table;
        
        pStemt = con.prepareStatement(tableSql);
        ResultSetMetaData rsmd = pStemt.getMetaData();
        int size = rsmd.getColumnCount();
        colNames = new String[size];
        colTypes = new String[size];
        colSizes = new int[size];
        //获取所需的信息
        for (int i = 0; i < size; i++) {
            colNames[i] = rsmd.getColumnName(i + 1);
            colTypes[i] = rsmd.getColumnTypeName(i + 1);
            if (colTypes[i].equalsIgnoreCase("datetime")) {
                needUtil = true;
            }
            if (colTypes[i].equalsIgnoreCase("image") || colTypes[i].equalsIgnoreCase("text")) {
                needSql = true;
            }
            colSizes[i] = rsmd.getColumnDisplaySize(i + 1);
        }
        
        className=tableToObjectName(tableName, true);
        objectName=tableToObjectName(tableName, false);
        
        CodeTemplate entityCode=new EntityCodeTemplate(this);
        entityCode.writeToFile();
        
        entityCode=new ConditionCodeTemplate(this);
        entityCode.writeToFile();
        
        entityCode=new DaoCodeTemplate(this);
        entityCode.writeToFile();
        
        entityCode=new DaoImplCodeTemplate(this);
        entityCode.writeToFile();
        
        entityCode=new ServiceCodeTemplate(this);
        entityCode.writeToFile();
        
        entityCode=new ServiceImplCodeTemplate(this);
        entityCode.writeToFile();
        
        entityCode=new ControllerCodeTemplate(this);
        entityCode.writeToFile();
    
        con.close();
    }
   
    
    public void genDao(){
    	
    }

    /**
     * @param args
     * @description 执行方法
     * @author paul
     * @date 2017年8月18日 下午2:03:35
     * @update 2017年8月18日 下午2:03:35
     * @version V1.0
     */
    public static void main(String[] args) {
        try {
        	//goodsManager\replenishLog
        	//自定义 packageOutPath  zwtitle 
            INSTANCE.packageOutPath = "com.server.module.trade.order.bean";// 指定实体生成所在包的路径
            INSTANCE.zwtitle="客户";
            INSTANCE.generate("warehouse_output_bill");//表名
          //  INSTANCE.packageOutPath="";

            System.out.println("generate classes success!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}