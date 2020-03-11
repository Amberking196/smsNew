package com.server.common.genCode;

import java.util.Date;

import org.springframework.web.bind.annotation.RequestBody;

public class ControllerCodeTemplate extends CodeTemplate {

	public ControllerCodeTemplate(GenCode mysqlMetaData) {
		super(mysqlMetaData);
		// TODO Auto-generated constructor stub
	}

	@Override
	String parse() {
		 StringBuffer sb = new StringBuffer();
	        sb.append("package " + packageOutPath + ";\r\n");
	        sb.append("\r\n");
	       
	        sb.append("import org.springframework.beans.factory.annotation.Autowired;");
	        sb.append("\r\n");
	        sb.append("import org.springframework.web.bind.annotation.GetMapping;");
	        sb.append("\r\n");
	        sb.append("import org.springframework.web.bind.annotation.RequestMapping;");
	        sb.append("\r\n");
	        sb.append("import org.springframework.web.bind.annotation.ResponseBody;");
	        sb.append("\r\n");
	        sb.append("import org.springframework.web.bind.annotation.RestController;");
	        sb.append("\r\n");
	        sb.append("\r\n");
	        sb.append("import io.swagger.annotations.Api;");
	        sb.append("\r\n");
	        sb.append("import io.swagger.annotations.ApiOperation;");
	        sb.append("\r\n");
	        sb.append("import com.server.util.ReturnDataUtil;");
	        sb.append("\r\n");
	        sb.append("import org.springframework.http.MediaType;");
	        sb.append("\r\n");
	        sb.append("import org.springframework.web.bind.annotation.PostMapping;");
	        sb.append("\r\n");
	        

	        // 注释部分
	        sb.append("/**\r\n");
	        sb.append(" * author name: " + authorName + "\r\n");
	        sb.append(" * create time: " + SDF.format(new Date()) + "\r\n");
	        sb.append(" */ \r\n");
	       // @Api(value = "ReplenishLogController", description = "货道商品变更api")
	        sb.append("@Api(value =\""+className+"Controller\",description=\""+zwtitle+"\")");
	        sb.append("\r\n");
	        // 实体部分
	        sb.append("@RestController");
	        sb.append("\r\n");
	        sb.append("@RequestMapping(\"/"+tableToObjectName(tableName, false)+"\")");
	        sb.append("\r\n");
	        sb.append("public class  " + className +"Controller"+ "{\r\n\r\n");
	        sb.append("\r\n");
	        sb.append("@Autowired");
	        sb.append("\r\n");
	        sb.append("private "+className+"Service "+objectName+"ServiceImpl;");
	        sb.append("\r\n");
	        //	@ApiOperation(value = "售货机列表", notes = "售货机列表", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)

	        sb.append("@ApiOperation(value = \""+zwtitle+"列表\",notes = \"listPage\",  httpMethod = \"GET\", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)");
	        sb.append("\r\n");
	        sb.append("@GetMapping(value = \"/listPage\", produces = \"application/json;charset=UTF-8\")");
	        sb.append("\r\n");
	        sb.append("public ReturnDataUtil listPage("+className+"Condition condition){");
	        sb.append("\r\n");	
	        sb.append("return "+objectName+"ServiceImpl.listPage(condition);");
	        sb.append("\r\n");
	        sb.append("}");
	        
	        sb.append("\r\n");
	        sb.append("@ApiOperation(value = \""+zwtitle+"添加\",notes = \"add\",  httpMethod = \"POST\", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)");
	        sb.append("\r\n");
	        sb.append("@PostMapping(value = \"/add\", produces = \"application/json;charset=UTF-8\")");
	        sb.append("\r\n");
	        sb.append("public ReturnDataUtil  add(@RequestBody "+entityName+" entity){");
	        sb.append("\r\n");	
	        sb.append("return new ReturnDataUtil("+objectName+"ServiceImpl.add(entity));");
	        sb.append("\r\n");
	        sb.append("}");
	        sb.append("\r\n");
	        sb.append("@ApiOperation(value = \""+zwtitle+"修改\",notes = \"update\",  httpMethod = \"POST\", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)");
	        sb.append("\r\n");
	        sb.append("@PostMapping(value = \"/update\", produces = \"application/json;charset=UTF-8\")");
	        sb.append("\r\n");
	        sb.append("public ReturnDataUtil update(@RequestBody "+entityName+" entity){");
	        sb.append("\r\n");	
	        sb.append("return new ReturnDataUtil("+objectName+"ServiceImpl.update(entity));");
	        sb.append("\r\n");
	        sb.append("}");
	        
	        sb.append("\r\n");
	        sb.append("@ApiOperation(value = \""+zwtitle+"删除\",notes = \"del\",  httpMethod = \"POST\", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)");
	        sb.append("\r\n");
	        sb.append("@PostMapping(value = \"/del\", produces = \"application/json;charset=UTF-8\")");
	        sb.append("\r\n");
	        sb.append("public ReturnDataUtil del(Object  id){");
	        sb.append("\r\n");	
	        sb.append("return new ReturnDataUtil("+objectName+"ServiceImpl.del(id));");
	        sb.append("\r\n");
	        sb.append("}");
	        
	        
	        sb.append("\r\n");
	        sb.append("}\r\n");
	        return sb.toString();
	   
	    
	}
	
	@Override
	String getFileName() {
		
		return className+"Controller.java";
	}

}
