package in.co.rays.test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import in.co.rays.bean.InitiativeBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.InitiativeModel;
import in.co.rays.model.UserModel;

public class TestInitiative {

	public static void main(String[] args) throws Exception {
	//	testNextPk();
//		testSearch();
	testAdd();
//		testUpdate();
//		testDelete();
//		testFindByPk();

	}

	private static void testNextPk() throws ApplicationException {
		InitiativeModel model = new InitiativeModel();
		int pk = model.nextPk();
		System.out.println("Next pk : " + pk);

	}

	private static void testAdd() throws Exception {
		InitiativeBean bean = new InitiativeBean();
		InitiativeModel model = new InitiativeModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		bean.setInitiativeName("service");;
		bean.setType("Innovation");
		bean.setStartDate(sdf.parse("2002-07-05"));
		bean.setVersion(3);
		bean.setCreatedBy("root"); 
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.add(bean);
	}

	private static void testUpdate() throws Exception {
		InitiativeBean bean = new InitiativeBean();
		InitiativeModel model = new InitiativeModel();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		bean.setId(6);
		bean.setInitiativeName("service");;
		bean.setType("Technology");
		bean.setStartDate(sdf.parse("2002-07-05"));
		bean.setVersion(3);
		bean.setCreatedBy("root"); 
		bean.setModifiedBy("root");
		bean.setCreatedDatetime(new Timestamp(new Date().getTime()));
		bean.setModifiedDatetime(new Timestamp(new Date().getTime()));

		model.update(bean);

	}

	private static void testDelete() throws Exception {
		InitiativeBean bean = new InitiativeBean();
		bean.setId(6);

		InitiativeModel model = new InitiativeModel();
		model.delete(bean);

	}
	

	private static void testFindByPk() throws ApplicationException {
		InitiativeModel model = new InitiativeModel();
		InitiativeBean bean = model.findByPk(1);

		if (bean != null) {
			System.out.print(bean.getId());
			System.out.print("\t" + bean.getInitiativeName());
			System.out.print("\t" + bean.getType());
			System.out.print("\t" + bean.getStartDate());
			System.out.print("\t" + bean.getVersion());
			System.out.print("\t" + bean.getCreatedBy());
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		} else {
			System.out.println("Record not found");
		}

	}
	

	private static void testSearch() throws Exception {
		InitiativeModel model = new InitiativeModel();
		InitiativeBean bean = new InitiativeBean();

		List list = model.list();

		Iterator it = list.iterator();

		while (it.hasNext()) {
			bean = (InitiativeBean) it.next();

			System.out.print(bean.getId());
			System.out.print("\t" + bean.getInitiativeName());
			System.out.print("\t" + bean.getType());
			System.out.print("\t" + bean.getStartDate());;
			System.out.print("\t" + bean.getModifiedBy());
			System.out.print("\t" + bean.getCreatedDatetime());
			System.out.println("\t" + bean.getModifiedDatetime());
		}

	}
}
