package com.server.util.sqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.server.dbpool.BaseDB;
 
 

public class InsertFuns extends BaseDB{
	public static Log log = LogFactory.getLog(InsertFuns.class);
	public static final String SQL_GET_ID = "SELECT LAST_INSERT_ID() AS id";
	
	public void insert(String sql){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
		}  catch (SQLException e) { 
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public long getId(Connection conn,PreparedStatement ps){
		ResultSet rs  = null;
		try {
			ps = conn.prepareStatement(SQL_GET_ID);
			rs  = ps.executeQuery();
			if(rs.next()){
				return rs.getLong("id");
			}
		} catch (SQLException e) {
			log.error("�����¼��ѯ����ID�Ŵ���"+ e );
		}finally{
			try {
				rs.close();
			} catch (SQLException e1) {
				log.error("�����¼��ѯ����ID�Ŵ���"+ e1);
			}
		}
		log.error("�����¼��ѯ����ID�Ŵ���" );
		return 0L;
		
	}
	
	public long insertGetID(String sql){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	
	public void insert(String sql,Object obj1){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public long insertGetID(String sql,Object obj1){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	
	public void insert(String sql,Object obj1,Object obj2){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public long insertGetID(String sql,Object obj1,Object obj2){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	
	public void insert(String sql,Object obj1,Object obj2,Object obj3){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public long insertGetID(String sql,Object obj1,Object obj2,Object obj3){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	
	public void insert(String sql,Object obj1,Object obj2,Object obj3,Object obj4){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public long insertGetID(String sql,Object obj1,Object obj2,Object obj3,Object obj4){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	
	public void insert(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.setObject(i++, obj5);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public long insertGetID(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.setObject(i++, obj5);
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	
	public void insert(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.setObject(i++, obj5);
			ps.setObject(i++, obj6);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public long insertGetID(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.setObject(i++, obj5);
			ps.setObject(i++, obj6);
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	

	public long insertGetID(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.setObject(i++, obj5);
			ps.setObject(i++, obj6);
			ps.setObject(i++, obj7);
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	
	public long insertGetID(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.setObject(i++, obj5);
			ps.setObject(i++, obj6);
			ps.setObject(i++, obj7);
			ps.setObject(i++, obj8);
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}

	
	
	public void insert(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.setObject(i++, obj5);
			ps.setObject(i++, obj6);
			ps.setObject(i++, obj7);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public void insert(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.setObject(i++, obj5);
			ps.setObject(i++, obj6);
			ps.setObject(i++, obj7);
			ps.setObject(i++, obj8);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public void insert(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8,Object obj9){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.setObject(i++, obj5);
			ps.setObject(i++, obj6);
			ps.setObject(i++, obj7);
			ps.setObject(i++, obj8);
			ps.setObject(i++, obj9);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public void insert(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8,Object obj9,Object obj10,Object obj11,Object obj12){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			ps.setObject(i++, obj1);
			ps.setObject(i++, obj2);
			ps.setObject(i++, obj3);
			ps.setObject(i++, obj4);
			ps.setObject(i++, obj5);
			ps.setObject(i++, obj6);
			ps.setObject(i++, obj7);
			ps.setObject(i++, obj8);
			ps.setObject(i++, obj9);
			ps.setObject(i++, obj10);
			ps.setObject(i++, obj11);
			ps.setObject(i++, obj12);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	
	public void insert(String sql,Object[] obj){
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			for (Object object : obj) {
				ps.setObject(i++, object);
			}
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally{
			closeConnection(null,ps,conn);
		}
	}
	
	public Long insertGetID(String sql,Object[] obj){
		long id = 0;
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = this.openConnection();
			ps = conn.prepareStatement(sql);
			int i=1;
			for (Object object : obj) {
				ps.setObject(i++, object);
			}
			ps.executeUpdate();
			id = getId(conn,ps);
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally{
			closeConnection(null,ps,conn);
		}
		return id;
	}
	
	
	
	public static void main(String[] args) {
		InsertFuns i = new InsertFuns();
		String sql = "insert  art_menu(id,menu,admin,introduce) values(?,?,?,?)";
		i.insert(sql,22,"222","2233","22444");
		System.out.println("ok");
	}
	
}