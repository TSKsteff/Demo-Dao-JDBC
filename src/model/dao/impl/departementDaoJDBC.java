package model.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import db.DB;
import db.DbException;
import model.dao.DepartementDao;
import model.entities.Department;

public class departementDaoJDBC implements DepartementDao{

	private Connection conn;
	
	public departementDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Department obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("Insert Into department "
					+"(Name) "
					+"Values "
					+"(?) ", Statement.RETURN_GENERATED_KEYS);
		
			st.setString(1, obj.getName());
			int rowsAffected = st.executeUpdate();
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					obj.setId(rs.getInt(1));
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected!");
			}
		}
		catch (Exception e) {
			throw new DbException("Error: "+e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
}
		
	

	@Override
	public void update(Department obj) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("Update seller "
					+"Set name = ? "
					+"Where Id = ?");
			
			st.setString(1, obj.getName());
			st.setInt(2, obj.getId());
			
			st.executeUpdate();
			
		} 
		catch (Exception e) {
			throw new DbException("Error:"+e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void deleteById(Integer id) {
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement("DELETE  FROM department WHERE Id = ?");
			
			st.setInt(1, id);
			
			st.executeUpdate();
	
		} catch (Exception e) {
			 throw new DbException("Error:"+ e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public Department findById(Integer id) {
		
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement("Select * From department Where Id = ?");
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
		
			if(rs.next()){
				Department depp = instantieDepartment(rs);
				return depp;
			}
		}
		catch (SQLException e) {
			throw new DbException("Error: "+e.getMessage());
		}finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
		return null;
	}

	@Override
	public List<Department> finall() {
		PreparedStatement st = null;
		ResultSet rs = null;
		List<Department> list = new ArrayList<>();
		try {
			st = conn.prepareStatement("SELECT * FROM department ORDER BY Name");
			rs = st.executeQuery();
			
			while (rs.next()) {
				list.add(instantieDepartment(rs));
			}
		}
		catch (SQLException e) {
			throw new DbException("Error: "+e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
		return list;
	}

	
	private Department instantieDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}
}
