package com.server.common.persistence;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.server.dbpool.DBPool;
import com.server.util.Reflections;
import com.server.util.StringUtil;
import com.server.util.sqlUtil.MySqlFuns;

public class BaseDao<E> extends MySqlFuns{
	/**
	 * 实体类类型(由构造方法自动赋值)
	 */
	private Class<?> entityClass;
	
	/**
	 * 构造方法，根据实例类自动获取实体类类型
	 */
	public BaseDao() {
		entityClass = Reflections.getClassGenricType(getClass());
	}
	@Value("${db.show.sql}")
	public  Boolean showSql;
	
	//private BasicDataSource datasource=DBPool.getInstance().getDatasource();
	private static Log log = LogFactory.getLog(BaseDao.class);
	public E get(Object key) {
       return get(key,entityClass);
    }
	
	public E get(Object key,@SuppressWarnings("rawtypes") Class classz){
    	Row row=new Row();    	
		@SuppressWarnings("unchecked")
		Entity ann=(Entity)classz.getAnnotation(Entity.class);
		if (ann!=null){
			row.setTableName(ann.tableName());
			row.setIdGenerate(ann.idGenerate());
			row.setPrimaryKey(ann.id());
		}
		String sql=row.getGetSql();
		Connection conn=null;
		PreparedStatement pst=null;
		ResultSet rs=null;
		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql);
			List<Object> parameterList=Lists.newArrayList();		
			parameterList.add(key);
			if (showSql){
				log.info(sql);
				log.info(parameterList.toString());
			}	
			setParameters(parameterList, pst);
			rs=pst.executeQuery();
			 while(rs.next()){
	        	 //创建实例
	            Object obj = cloumnsToEntity(rs, classz);
	            return (E)obj;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			//throw e;
			
		} finally{
			try {
				if(rs!=null)
				rs.close();
				if(pst!=null)
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
    	return null;
    }
	public E insert(E entity){
		
		Row row = paserEntity(entity);
		String sql = row.getInsertSql();
		if (row.getIdGenerate().equals("auto")){
			 Object id = insert(row, sql,true);
			 
			 try {
	        	 Field f = entity.getClass().getDeclaredField(row.getPrimaryKey());
	             f.setAccessible(true);
				 f.set(entity, id);
				 return entity;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} else {
			 insert(row, sql,false);
			 return entity;
		}
		return entity;      
	}
	
	
  public Object insert(Connection conn,Object entity){
		
		Row row = paserEntity((E)entity);
		String sql = row.getInsertSql();
		if (row.getIdGenerate().equals("auto")){
			 Object id = insert(conn,row, sql,true);
			 
			 try {
	        	 Field f = entity.getClass().getDeclaredField(row.getPrimaryKey());
	             f.setAccessible(true);
				 f.set(entity, id);
				 return entity;
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		} else {
			 insert(conn,row, sql,false);
			 return entity;
		}
		return entity;      
	}
	
    public boolean update(E entity){
    	Row row = paserEntity(entity);
		String sql=row.getUpdateSql();
		return execute(row, sql);	
    }

    public boolean del(E entity){
		
		Row row = paserEntity(entity);
		String sql=row.getDelSql();
		return execute(row, sql);
	}
    /**
     * 执行插入
     * @param row
     * @param sql
     * @param autokey  是否返回主键
     * @return
     * @throws SQLException
     */
    private Object insert(Row row, String sql,boolean autoKey) {
		Connection conn=null;
		PreparedStatement pst=null;
		try {
			conn=openConnection();
            if (autoKey)
			   pst=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            else
 			   pst=conn.prepareStatement(sql);	
			List<Object> parameterList=Lists.newArrayList();
			int count=row.getListColumn().size();
			List<Column> columnList=row.getListColumn();
			for (int i=0;i<count;i++){
				Column column=columnList.get(i);
				parameterList.add(column.getValue());
			}
			setParameters(parameterList, pst);
			if (showSql){
				log.info(sql);
				log.info(parameterList.toString());
			}			
		    pst.executeUpdate();
		    if (autoKey){
			    ResultSet rs = pst.getGeneratedKeys();   
	            if (rs.next()) {  
	                Object id = rs.getObject(1);   
	                return id;
	            } 
		    }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		} finally{
			try {
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}
    
    /**
     * 执行插入 传入connection 可以自己控制事务
     * @param conn 
     * @param row
     * @param sql
     * @param autokey  是否返回主键
     * @return
     * @throws SQLException
     */
    private Object insert(Connection conn,Row row, String sql,boolean autoKey) {
		PreparedStatement pst=null;
		try {
            if (autoKey)
			   pst=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            else
 			   pst=conn.prepareStatement(sql);	
			List<Object> parameterList=Lists.newArrayList();
			int count=row.getListColumn().size();
			List<Column> columnList=row.getListColumn();
			for (int i=0;i<count;i++){
				Column column=columnList.get(i);
				parameterList.add(column.getValue());
			}
			setParameters(parameterList, pst);
			if (showSql){
				log.info(sql);
				log.info(parameterList.toString());
			}			
		    pst.executeUpdate();
		    if (autoKey){
			    ResultSet rs = pst.getGeneratedKeys();   
	            if (rs.next()) {  
	                Object id = rs.getObject(1);   
	                return id;
	            } 
		    }
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			return null;
		} finally{
			try {
				pst.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return null;
	}
    /**
     * 执行语句
     * @param row
     * @param sql
     * @return
     * @throws SQLException
     */
    private boolean execute(Row row, String sql) {
    	log.info(sql);
		Connection conn=null;
		PreparedStatement pst=null;
		try {
			conn=openConnection();
			pst=conn.prepareStatement(sql);
			List<Object> parameterList=Lists.newArrayList();
			int count=row.getListColumn().size();
			List<Column> columnList=row.getListColumn();
			for (int i=0;i<count;i++){
				Column column=columnList.get(i);
				parameterList.add(column.getValue());
			}
			setParameters(parameterList, pst);
			if (showSql){
				log.info(sql);
				log.info(parameterList.toString());
			}			
		    pst.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.error(e.getMessage());
			//throw e;
			return false;
		} finally{
			try {
				pst.close();
				closeConnection(conn);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
				
	}
	
	/**
	 * 实体转化为表相关的值
	 * @param entity
	 * @return
	 */
	private Row paserEntity(E entity) {
		Row row=new Row();
		Entity ann=(Entity)entity.getClass().getAnnotation(Entity.class);
		if (ann!=null){
			row.setTableName(ann.tableName());
			row.setIdGenerate(ann.idGenerate());
			row.setPrimaryKey(ann.id());
		}
		
		
		for (Method method : entity.getClass().getMethods()){
			String name=method.getName();
				try {
				
					if (name.indexOf("get")==0 && !name.equals("getClass")){
					   Field field=entity.getClass().getDeclaredField(StringUtil.lowerFirstCase(name.substring(3)));
					   NotField nf=field.getAnnotation(NotField.class);
						if(nf!=null){
							continue;
						}
						Object temp = null;
						temp = method.invoke(entity);
						
						if(temp!=null){
							Column column=new Column();
							column.setField(StringUtil.lowerFirstCase(name.substring(3)));
							column.setValue(temp);
							row.addColumn(column);
						}
					}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (NoSuchFieldException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		//row.parseInsertSql();
		return row;
	}
	
	/**
	 * 分页查询方法
	 * @param page
	 * @param sql from必须为小写
	 * @param parameterList
	 * @param classz 查询结果组装成的实体类型
	 * @return
	 */
	public Page listPage(Page page,String sql,List<Object> parameterList, Class classz){
		if (page==null){
			page=new Page();
		}
		Connection conn=null;
		PreparedStatement pst1=null;
		ResultSet rs1=null;
		try {
			conn=openConnection();
			long count=0;
			count = executeForCount(conn, parameterList, sql);
			page.setCount(count);			
			long off=(page.getPageNo()-1)*page.getPageSize();			
			sql=sql+" limit "+off+","+page.getPageSize();
			pst1=conn.prepareStatement(sql);
			setParameters(parameterList, pst1);
			if (showSql){
				log.info(sql);
				log.info(parameterList.toString());
			}
			rs1=pst1.executeQuery();	
			List<Object> objs = dbItemsToEntityList(classz, rs1);
			page.setCount(count);			 
			page.setList(objs);			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			if (rs1!=null){
				try {
					rs1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pst1!=null){
				try {
					pst1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn!=null){
					closeConnection(conn);
				
			}
		}
		return page;
	}

   

    /**
     * 一条记录封装到实体类
     * @param rs1
     * @param md
     * @param ccount
     * @param class1
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws SQLException
     * @throws NoSuchFieldException
     */
	private Object cloumnsToEntity(ResultSet rs1, Class<?> class1) {
		try{
		Object obj = class1.newInstance();
		ResultSetMetaData md=rs1.getMetaData();
		int ccount=md.getColumnCount();
		for(int i=1;i<=ccount;i++){
			String fieldName=md.getColumnName(i);
			 Field field = class1.getDeclaredField(fieldName) ;
		     //打破封装  实际上setAccessible是启用和禁用访问安全检查的开关,并不是为true就能访问为false就不能访问  
		     //由于JDK的安全检查耗时较多.所以通过setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目的  
			 field.setAccessible( true );
		     //给id 属性赋值
			 field.set(obj, rs1.getObject(fieldName)) ;
		}
		return obj;
		} catch (Exception e){
			e.printStackTrace();
			log.error(e.getMessage());
		}
		return null;
	}
  /**
   * 列表查询
   * @param sql
   * @param parameterList
   * @param classz
   * @return
   */
	
  public List list(String sql,List<Object> parameterList, Class classz){
		
		PreparedStatement pst1=null;
		ResultSet rs1=null;
		Connection conn = null;
		try {
			conn =openConnection();
			pst1=conn.prepareStatement(sql);
			if(parameterList!=null && parameterList.size()>0){
				setParameters(parameterList, pst1);
			}
			rs1=pst1.executeQuery();
			ResultSetMetaData md=pst1.getMetaData();
			int ccount=md.getColumnCount();			
			List<Object> objs=Lists.newArrayList();		    
			 try {
		            //创建类
		            Class<?> class1 = classz;		     		            
		            while(rs1.next()){
		            	 //创建实例
			            Object obj = class1.newInstance();
		            	for(int i=1;i<=ccount;i++){
//		            		String fieldName=md.getColumnName(i);
		            		String fieldName = md.getColumnLabel(i);
		            		 Field field = class1.getDeclaredField(fieldName) ;

		                     //打破封装  实际上setAccessible是启用和禁用访问安全检查的开关,并不是为true就能访问为false就不能访问  
		                     //由于JDK的安全检查耗时较多.所以通过setAccessible(true)的方式关闭安全检查就可以达到提升反射速度的目的  
		            		 field.setAccessible( true );
		                     //给id 属性赋值
		            		 field.set(obj, rs1.getObject(fieldName)) ;
						}
		            	objs.add(obj);
					}
		            

		            //打印 person 的属性值

		        } catch (InstantiationException e) {
		            e.printStackTrace();
		        } catch (IllegalAccessException e) {
		            e.printStackTrace();
		        } catch (SecurityException e) {
		            e.printStackTrace() ;
		        } catch (Exception e){
		        	e.printStackTrace();
		        }
			 
			return objs;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			if (rs1!=null){
				try {
					rs1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pst1!=null){
				try {
					pst1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (conn!=null){
					closeConnection(conn);
				
			}
		}
		return Lists.newArrayList();
	}
  
  /**
   * 列表查询
   * @param sql
   * @param parameterList
   * @return
   */
	
  public List<Map<String,Object>> list(String sql,List<Object> parameterList){
		PreparedStatement pst1=null;
		ResultSet rs1=null;
		try {
			Connection conn=openConnection();
			pst1=conn.prepareStatement(sql);
			setParameters(parameterList, pst1);
			rs1=pst1.executeQuery();
			ResultSetMetaData md=pst1.getMetaData();
			int ccount=md.getColumnCount();			
			List<Map<String,Object>> objs=Lists.newArrayList();		    
			 try {
		            //创建类
		            while(rs1.next()){
		            	 //创建实例
		            	Map<String,Object> map=Maps.newHashMap();
		            	for(int i=1;i<=ccount;i++){
		            		String fieldName = md.getColumnLabel(i);
		            		//String fieldName=md.getColumnName(i);
		            		//System.out.println("size:"+size+"    lable:"+label+"   fieldName :"+fieldName);
		            		map.put(fieldName,rs1.getObject(fieldName));
											
						}
		            	objs.add(map);
					}
		            //打印 person 的属性值
		        } catch (SecurityException e) {
		            e.printStackTrace() ;
		        } catch (Exception e){
		        	e.printStackTrace();
		        }
			 
			return objs;
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			
			if (rs1!=null){
				try {
					rs1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if (pst1!=null){
				try {
					pst1.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return Lists.newArrayList();
	}
    
  public void updateSql(String sql,List<Object> parameterList){
	    PreparedStatement pst1=null;
		Connection conn=null;
		try {
			conn=openConnection();
			conn.setAutoCommit(false);			
			pst1=conn.prepareStatement(sql);
			setParameters(parameterList, pst1);
			if (showSql){
				log.info(sql);
				log.info(parameterList.toString());
			}
			pst1.executeUpdate();
			conn.commit();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally{
			if (pst1!=null){
				try {
					pst1.close();
					closeConnection(conn);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
  }
	private long executeForCount(Connection conn, List<Object> parameterList, String sql)
			throws SQLException {
		
		PreparedStatement pst=conn.prepareStatement(countSql(sql));
		setParameters(parameterList, pst);
		ResultSet rs=pst.executeQuery();
		long count=0;
		while(rs.next()){
			count=rs.getInt(1);
		}
		if(rs!=null){
			rs.close();
		}
		if(pst!=null){
			pst.close();
		}
		return count;
	}
	
	  public long selectCountBySql(String sql,List<Object> parameterList){
		    PreparedStatement pst1=null;
			Connection conn=null;
			ResultSet rs=null;

			try {
				conn=openConnection();
				pst1=conn.prepareStatement(sql);
				setParameters(parameterList, pst1);
				
				rs=pst1.executeQuery();
				while(rs.next()){
					long count=rs.getLong(1);
					return count;
				}
				return 0;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} finally{
				//if (pst1!=null){
					try {
						if(rs!=null)
						rs.close();
						if(pst1!=null)
						pst1.close();
						closeConnection(conn);
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				//}
			}
			return 0l;
			
	  }

	private void setParameters(List<Object> parameterList, PreparedStatement pst) throws SQLException {
		if (parameterList!=null && parameterList.size()>0)
		for(int i=0;i<parameterList.size();i++){
			pst.setObject(i+1,parameterList.get(i));

		}
	}

	public String countSql(String sql) {
		// TODO Auto-generated method stub
		int start=sql.indexOf("from");
		int end=sql.indexOf(" order ", start);
		if (end==-1)
		  sql="select count(*) "+sql.substring(start);
		else
		   sql="select count(*) "+sql.substring(start,end);
		return sql;
	}
	 /**
     * 多条数据封装到实体类列表
     * @param classz
     * @param rs1
     * @param md
     * @param ccount
     * @return
     */
	private List<Object> dbItemsToEntityList(Class classz, ResultSet rs1) {
		List<Object> objs=Lists.newArrayList();	
		 try {
		        //创建类
		        Class<?> class1 = classz;		     		            
		        while(rs1.next()){
		        	 //创建实例
		            Object obj = cloumnsToEntity(rs1, class1);
		        	objs.add(obj);
				}
		        //打印 person 的属性值
		    } catch (SecurityException e) {
		        e.printStackTrace() ;
		    } catch (Exception e){
		    	e.printStackTrace();
		    }
		return objs;
	}
}
