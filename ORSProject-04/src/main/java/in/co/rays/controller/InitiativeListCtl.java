package in.co.rays.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.InitiativeBean;
import in.co.rays.bean.RoleBean;
import in.co.rays.bean.UserBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.model.InitiativeModel;
import in.co.rays.model.RoleModel;
import in.co.rays.model.UserModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * Initiative List Controller to handle list, search, delete operations of User.
 * 
 * @author vedik vishwakarma
 */
@WebServlet(name = "InitiativeListCtl", urlPatterns = "/ctl/InitiativeListCtl")
public class InitiativeListCtl extends BaseCtl {

	Logger log = Logger.getLogger(InitiativeListCtl.class);

	/**
	 * Loads Initiative list to request scope for dropdown.
	 * 
	 * @param request HttpServletRequest
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.debug("InitiativeListCtl preload Method Started");

		InitiativeModel model = new InitiativeModel();
		try {
			List<InitiativeBean> initiativeList = model.list();
//			request.setAttribute("initiativeList", initiativeList);

			HashMap<String, String> typeMap = new HashMap<>();
			for (InitiativeBean bean : initiativeList) {
				if (bean.getType() != null && !bean.getType().trim().isEmpty()) {
					typeMap.put(bean.getType(), bean.getType());
				}
			}
			request.setAttribute("typeMap", typeMap);

		} catch (ApplicationException e) {
			log.error(e);
		}
		log.debug("InitiativeListCtl preload Method Ended");
	}

	/**
	 * Populates InitiativeBean from request parameters for search criteria.
	 * 
	 * @param request HttpServletRequest
	 * @return BaseBean containing UserBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.debug("InitiativeListCtl populateBean Method Started");

		InitiativeBean bean = new InitiativeBean();

		bean.setInitiativeName(DataUtility.getString(request.getParameter("initiativeName")));
		bean.setType(DataUtility.getString(request.getParameter("type")));
		bean.setVersion(DataUtility.getInt(request.getParameter("version")));

		log.debug("InitiativeListCtl populateBean Method Ended");
		return bean;
	}

	/**
	 * Handles HTTP GET request for initial Initiative list page load.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("InitiativeListCtl doGet Method Started");

		int pageNo = 1;
		int pageSize = DataUtility.getInt(PropertyReader.getValue("page.size"));

		InitiativeModel model = new InitiativeModel();
		InitiativeBean bean = (InitiativeBean) populateBean(request);

		try {
			List<InitiativeBean> list = model.search(bean, pageNo, pageSize);
			List<InitiativeBean> next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage("No record found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}

		log.debug("InitiativeListCtl doGet Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Handles HTTP POST request for searching, pagination, delete, reset
	 * operations.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.debug("InitiativeListCtl doPost Method Started");

		List<InitiativeBean> list = null;
		List<InitiativeBean> next = null;

		int pageNo = DataUtility.getInt(request.getParameter("pageNo"));
		int pageSize = DataUtility.getInt(request.getParameter("pageSize"));

		pageNo = (pageNo == 0) ? 1 : pageNo;
		pageSize = (pageSize == 0) ? DataUtility.getInt(PropertyReader.getValue("page.size")) : pageSize;

		InitiativeBean bean = (InitiativeBean) populateBean(request);
		InitiativeModel model = new InitiativeModel();

		String op = DataUtility.getString(request.getParameter("operation"));
		String[] ids = request.getParameterValues("ids");

		try {

			if (OP_SEARCH.equalsIgnoreCase(op) || OP_NEXT.equalsIgnoreCase(op) || OP_PREVIOUS.equalsIgnoreCase(op)) {
				if (OP_SEARCH.equalsIgnoreCase(op)) {
					pageNo = 1;
				} else if (OP_NEXT.equalsIgnoreCase(op)) {
					pageNo++;
				} else if (OP_PREVIOUS.equalsIgnoreCase(op) && pageNo > 1) {
					pageNo--;
				}
			} else if (OP_NEW.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.INITIATIVE_CTL, request, response);
				return;
			} else if (OP_DELETE.equalsIgnoreCase(op)) {
				pageNo = 1;
				if (ids != null && ids.length > 0) {
					InitiativeBean deleteBean = new InitiativeBean();
					for (String id : ids) {
						deleteBean.setId(DataUtility.getLong(id));
						model.delete(deleteBean);
					}
					ServletUtility.setSuccessMessage("Data deleted Successfully", request);
				} else {
					ServletUtility.setErrorMessage("Select at least One Record", request);
				}
			} else if (OP_RESET.equalsIgnoreCase(op) || OP_BACK.equalsIgnoreCase(op)) {
				ServletUtility.redirect(ORSView.INITIATIVE_LIST_CTL, request, response);
				return;
			}

			list = model.search(bean, pageNo, pageSize);
			next = model.search(bean, pageNo + 1, pageSize);

			if (list == null || list.isEmpty()) {
				ServletUtility.setErrorMessage("No Records Found", request);
			}

			ServletUtility.setList(list, request);
			ServletUtility.setPageNo(pageNo, request);
			ServletUtility.setPageSize(pageSize, request);
			ServletUtility.setBean(bean, request);
			request.setAttribute("nextListSize", next.size());

		} catch (ApplicationException e) {
			log.error(e);
			ServletUtility.handleException(e, request, response);
		}
		log.debug("InitiativeListCtl doPost Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns the view path of Initiative List.
	 * 
	 * @return String view path
	 */
	@Override
	protected String getView() {
		return ORSView.INITIATIVE_LIST_VIEW;
	}

}
