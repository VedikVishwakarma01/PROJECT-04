package in.co.rays.model;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import in.co.rays.bean.InitiativeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.util.JDBCDataSource;

public class InitiativeModel {

	public int nextPk() throws ApplicationException {

		int pk = 0;
		Connection conn = null;
		try {

			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select max(id) from st_initiative");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				pk = rs.getInt(1);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (SQLException e) {
			throw new ApplicationException("exception in initiative nextPk ");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk + 1;
	}

	public long add(InitiativeBean bean) throws ApplicationException {

		int pk = nextPk();
		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn
					.prepareStatement("insert into st_initiative value(?, ?, ?, ?, ?, ?, ?, ?, ?)");
			pstmt.setInt(1, pk);
			pstmt.setString(2, bean.getInitiativeName());
			pstmt.setString(3, bean.getType());
			pstmt.setDate(4, new java.sql.Date(bean.getStartDate().getTime()));
			pstmt.setInt(5, bean.getVersion());
			pstmt.setString(6, bean.getCreatedBy());
			pstmt.setString(7, bean.getModifiedBy());
			pstmt.setTimestamp(8, bean.getCreatedDatetime());
			pstmt.setTimestamp(9, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("data inserted successfully " + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception in InitiativeModel add rollback" + ex.getMessage());
			}
			throw new ApplicationException("Exception in add initiative " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return pk;
	}

	public void update(InitiativeBean bean) throws ApplicationException {

		Connection conn = null;
		try {
			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement(
					"update st_initiative set initiative_name=?, type=?, start_date=?, version_number=?,created_by=?,modified_by=?,created_datetime=?,modified_datetime=? where id=?");

			pstmt.setString(1, bean.getInitiativeName());
			pstmt.setString(2, bean.getType());
			pstmt.setDate(3, new java.sql.Date(bean.getStartDate().getTime()));
			pstmt.setInt(4, bean.getVersion());
			pstmt.setString(5, bean.getCreatedBy());
			pstmt.setString(6, bean.getModifiedBy());
			pstmt.setTimestamp(7, bean.getCreatedDatetime());
			pstmt.setTimestamp(8, bean.getModifiedDatetime());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();
			System.out.println("data updated successfully " + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (Exception ex) {
				throw new ApplicationException("Exception in InitiativeModel update rollback" + ex.getMessage());
			}
			throw new ApplicationException("Exception in update initiative " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
	}

	public void delete(InitiativeBean bean) throws ApplicationException {

		Connection conn = null;

		try {

			conn = JDBCDataSource.getConnection();
			conn.setAutoCommit(false);
			PreparedStatement pstmt = conn.prepareStatement("delete from st_initiative where id  = ?");

			pstmt.setLong(1, bean.getId());

			int i = pstmt.executeUpdate();
			conn.commit();
			pstmt.close();

			System.out.println("Deleted Succesfully...." + i);

		} catch (Exception e) {
			try {
				conn.rollback();
			} catch (SQLException e1) {
				throw new ApplicationException("Exception in delete Initiative rollback " + e);
			}
			throw new ApplicationException("Exception : Exception in delete Initiative " + e);
		} finally {
			JDBCDataSource.closeConnection(conn);
		}

	}
	
	public InitiativeBean findByPk(long id) throws ApplicationException {

		InitiativeBean bean = null;
		Connection conn = null;

		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("select * from st_initiative where id=?");
			pstmt.setLong(1, id);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new InitiativeBean();

				bean.setId(rs.getLong(1));
				bean.setInitiativeName(rs.getString(2));
				bean.setType(rs.getString(3));
				bean.setStartDate(rs.getDate(4));
				bean.setVersion(rs.getInt(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));

			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			throw new ApplicationException("Exception : Exception in getting initiative by PK");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return bean;
	}

	
	public List<InitiativeBean> list() throws ApplicationException{
		return search(null, 0, 0);
	}

	public List<InitiativeBean> search(InitiativeBean bean, int pageNo, int pageSize) throws ApplicationException {

		StringBuffer sql = new StringBuffer("select * from st_initiative where 1=1");

		if (bean != null) {
			if (bean.getInitiativeName() != null && bean.getInitiativeName().length() > 0) {
				sql.append(" and initiative_name like '" + bean.getInitiativeName() + "%'");
			}
			if (bean.getType() != null && bean.getType().length() > 0) {
				System.out.println("Searching by type: " + bean.getType());

				sql.append(" and type like '" + bean.getType() + "%'");
			}
			if (bean.getStartDate() != null) {
				sql.append(" and  start_date =  '" + bean.getStartDate() + "%'");
			}
			if (bean.getVersion() > 0 ) {
				sql.append(" and version_number like '" + bean.getVersion() + "%'");
			}
		}

		if (pageSize > 0) {
			pageNo = (pageNo - 1) * pageSize;
			sql.append(" limit " + pageNo + ", " + pageSize);
		}
		System.out.println("sql "+sql.toString());

		Connection conn = null;
		List<InitiativeBean> list = new ArrayList<InitiativeBean>();
		try {
			conn = JDBCDataSource.getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql.toString());
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				bean = new InitiativeBean();

				bean.setId(rs.getLong(1));
				bean.setInitiativeName(rs.getString(2));
				bean.setType(rs.getString(3));
				bean.setStartDate(rs.getDate(4));
				bean.setVersion(rs.getInt(5));
				bean.setCreatedBy(rs.getString(6));
				bean.setModifiedBy(rs.getString(7));
				bean.setCreatedDatetime(rs.getTimestamp(8));
				bean.setModifiedDatetime(rs.getTimestamp(9));

				list.add(bean);
			}
			JDBCDataSource.closeConnection(rs, pstmt);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ApplicationException("Exception in search initiative");
		} finally {
			JDBCDataSource.closeConnection(conn);
		}
		return list;
	}

}
