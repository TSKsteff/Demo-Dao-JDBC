package model.dao.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

public class SellerDaoJDBC implements SellerDao {

	private Connection conn;
	
	public SellerDaoJDBC(Connection conn) {
		this.conn = conn;
	}
	
	@Override
	public void insert(Seller obj) {
		
		PreparedStatement st = null;
		
		try {
			st = conn.prepareStatement(
					"Insert Into seller "
					+"(Name, Email, BirthDate, BaseSalary, departmentId) "
					+"Values "
					+"(?, ?, ?, ?, ?) ", Statement.RETURN_GENERATED_KEYS);
			
			st.setString(1, obj.getName());
			st.setString(2, obj.getEmail());
			st.setDate(3, new java.sql.Date(obj.getBirthDate().getTime()));
			st.setDouble(4, obj.getBaseSalary());
			st.setInt(5, obj.getDepartement().getId());
			
			int rowsAffected = st.executeUpdate();
			
			if(rowsAffected > 0) {
				ResultSet rs = st.getGeneratedKeys();
				if(rs.next()) {
					int id = rs.getInt(1);
					obj.setId(id);
				}
				DB.closeResultSet(rs);
			}
			else {
				throw new DbException("Unexpected error! No rows affected");
			}
	
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
		}
	}

	@Override
	public void update(Seller obj) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteById(Integer id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Seller findById(Integer id) {
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			st = conn.prepareStatement(
					"Select seller.*,department.Name as DepName "
					+"From seller Inner Join department "
					+"On seller.DepartmentId = department.Id "
					+"Where seller.Id = ? "
					);
			
			st.setInt(1, id);
			
			rs = st.executeQuery();
			
			if(rs.next()) {
				Department dep = instantieDepartment(rs);
				
				Seller sel = instantieSeller(rs, dep);
				return sel;
			}
			return null;
		}
		catch (SQLException e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeStatement(st);
			DB.closeResultSet(rs);
		}
}
	private Seller instantieSeller(ResultSet rs, Department dep) throws SQLException {
		Seller sel = new Seller();
		sel.setId(rs.getInt("Id"));
		sel.setName(rs.getString("Name"));
		sel.setEmail(rs.getString("Email"));
		sel.setBaseSalary(rs.getDouble("BaseSalary"));
		sel.setBirthDate(rs.getDate("BirthDate"));
		sel.setDepartement(dep);
		return sel;
	}

	private Department instantieDepartment(ResultSet rs) throws SQLException {
		Department dep = new Department();
		dep.setId(rs.getInt("DepartmentId"));
		dep.setName(rs.getString("DepName"));
		return dep;
	}

	@Override
	public List<Seller> findAll() {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"Select seller.*,department.Name as DepName "
					+"From seller Inner Join department "
					+"On seller.DepartmentId = department.Id "
					+"Order By Name ");
			
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<Seller>();
			Map<Integer, Department> depmap = new HashMap<>();
			
			while(rs.next()) {
				
				Department depp = depmap.get(rs.getInt("DepartmentId"));
				
				if(depp == null) {
					depp = instantieDepartment(rs);
					depmap.put(rs.getInt("DepartmentId"), depp);
				}
				Seller sel = instantieSeller(rs, depp);
				list.add(sel);
			}
			return list;
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
		
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		PreparedStatement st = null;
		ResultSet rs = null;
		
		try {
			st = conn.prepareStatement(
					"Select seller.*,department.Name as DepName "
					+ "From seller Inner Join department "
					+ "On seller.DepartmentId = department.Id "
					+ "Where DepartmentId = ? "
					+ "Order BY Name");
			st.setInt(1, department.getId());
			rs = st.executeQuery();
			
			List<Seller> list = new ArrayList<>();
			Map<Integer, Department> map = new HashMap<>();
			
			while (rs.next()) {
				
				Department dep = map.get(rs.getInt("DepartmentId"));
				
				if(dep == null) {
					dep = instantieDepartment(rs);	
					map.put(rs.getInt("DepartmentId"), dep);
				}
				
				Seller sel = instantieSeller(rs, dep); 
				list.add(sel);
			}
			return list;
		} catch (Exception e) {
			throw new DbException(e.getMessage());
		}
		finally {
			DB.closeResultSet(rs);
			DB.closeStatement(st);
		}
	}

}
