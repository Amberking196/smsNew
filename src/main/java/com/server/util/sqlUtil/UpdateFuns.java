package com.server.util.sqlUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.server.dbpool.BaseDB;
 

public class UpdateFuns extends BaseDB {
	public static Log log = LogFactory.getLog(UpdateFuns.class);
	public void upate(String sql) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
			ps.executeUpdate(); 
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
			ps = conn.prepareStatement(sql);
			ps.setObject(1, obj1);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	
	public void upate(String sql,Object obj1,Object obj2) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8,Object obj9) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8,Object obj9,Object obj10) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8,Object obj9,Object obj10,Object obj11) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8,Object obj9,Object obj10,Object obj11,Object obj12) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8,Object obj9,Object obj10,Object obj11,Object obj12,Object obj13) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			ps.setObject(i++, obj13);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,Object obj7,Object obj8,Object obj9,Object obj10,Object obj11,Object obj12,Object obj13,Object obj14) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			ps.setObject(i++, obj13);
			ps.setObject(i++, obj14);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,
			Object obj7,Object obj8,Object obj9,Object obj10,Object obj11,Object obj12,Object obj13,
			Object obj14,Object obj15) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			ps.setObject(i++, obj13);
			ps.setObject(i++, obj14);
			ps.setObject(i++, obj15);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,
			Object obj7,Object obj8,Object obj9,Object obj10,Object obj11,Object obj12,Object obj13,
			Object obj14,Object obj15,Object obj16) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			ps.setObject(i++, obj13);
			ps.setObject(i++, obj14);
			ps.setObject(i++, obj15);
			ps.setObject(i++, obj16);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,
			Object obj7,Object obj8,Object obj9,Object obj10,Object obj11,Object obj12,Object obj13,
			Object obj14,Object obj15,Object obj16,Object obj17) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			ps.setObject(i++, obj13);
			ps.setObject(i++, obj14);
			ps.setObject(i++, obj15);
			ps.setObject(i++, obj16);
			ps.setObject(i++, obj17);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,
			Object obj7,Object obj8,Object obj9,Object obj10,Object obj11,Object obj12,Object obj13,
			Object obj14,Object obj15,Object obj16,Object obj17,
			Object obj18,Object obj19,Object obj20,Object obj21,
			Object obj22,Object obj23) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			ps.setObject(i++, obj13);
			ps.setObject(i++, obj14);
			ps.setObject(i++, obj15);
			ps.setObject(i++, obj16);
			ps.setObject(i++, obj17);
			ps.setObject(i++, obj18);
			ps.setObject(i++, obj19);
			ps.setObject(i++, obj20);
			ps.setObject(i++, obj21);
			ps.setObject(i++, obj22);
			ps.setObject(i++, obj23);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public void upate(String sql,Object obj1,Object obj2,Object obj3,Object obj4,Object obj5,Object obj6,
			Object obj7,Object obj8,Object obj9,Object obj10,Object obj11,Object obj12,Object obj13,
			Object obj14,Object obj15,Object obj16,Object obj17,
			Object obj18,Object obj19,Object obj20,Object obj21,
			Object obj22,Object obj23,Object obj24) {
		Connection conn = null;
		PreparedStatement ps = null;
		try {
			conn = openConnection();
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
			ps.setObject(i++, obj13);
			ps.setObject(i++, obj14);
			ps.setObject(i++, obj15);
			ps.setObject(i++, obj16);
			ps.setObject(i++, obj17);
			ps.setObject(i++, obj18);
			ps.setObject(i++, obj19);
			ps.setObject(i++, obj20);
			ps.setObject(i++, obj21);
			ps.setObject(i++, obj22);
			ps.setObject(i++, obj23);
			ps.setObject(i++, obj24);
			ps.executeUpdate();
			conn.commit();
		}  catch (SQLException e) { 
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		} finally {
			this.closeConnection(null, ps, conn);
		}
	}
	
	public static void main(String[] args) {
		UpdateFuns u = new UpdateFuns();
		String sql = "update art_menu set admin=? where id=?";
		u.upate(sql,"zhutou",22);
	}
}
