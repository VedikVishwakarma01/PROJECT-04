package in.co.rays.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import in.co.rays.bean.BaseBean;
import in.co.rays.bean.InitiativeBean;
import in.co.rays.exception.ApplicationException;
import in.co.rays.exception.DuplicateRecordException;
import in.co.rays.model.InitiativeModel;
import in.co.rays.util.DataUtility;
import in.co.rays.util.DataValidator;
import in.co.rays.util.PropertyReader;
import in.co.rays.util.ServletUtility;

/**
 * User Controller to handle adding, updating, validation and displaying User
 * form.
 * 
 * @author vedik vishwakarma
 */
@WebServlet(name = "InitiativeCtl", urlPatterns = { "/ctl/InitiativeCtl" })
public class InitiativeCtl extends BaseCtl {

	Logger log = Logger.getLogger(InitiativeCtl.class);

	/**
	 * Loads list of Roles and sets in request for dropdown.
	 * 
	 * @param request HttpServletRequest
	 */
	@Override
	protected void preload(HttpServletRequest request) {
		log.info("UserCtl preload Method Started");

		InitiativeModel model = new InitiativeModel();
		try {
			List<InitiativeBean> initiativeList = model.list();
			request.setAttribute("initiativeList", initiativeList);
		} catch (ApplicationException e) {
			log.error(e);
		}
		log.info("UserCtl preload Method Ended");
	}

	/**
	 * Validates User form data from the request.
	 * 
	 * @param request HttpServletRequest
	 * @return boolean true if valid, else false
	 */
	@Override
	protected boolean validate(HttpServletRequest request) {
		log.info("UserCtl validate Method Started");

		boolean isValid = true;

		if (DataValidator.isNull(request.getParameter("initiativeName"))) {
			request.setAttribute("initiativeName", PropertyReader.getValue("error.require", "Initiative Name"));
			isValid = false;
		} else if (!DataValidator.isName(request.getParameter("initiativeName"))) {
			request.setAttribute("initiativeName", PropertyReader.getValue("error.name", "Initiative Name"));
			isValid = false;
		}
		
		if (DataValidator.isNull(request.getParameter("type"))) {
			request.setAttribute("type", PropertyReader.getValue("error.require", "Type"));
			isValid = false;
		} else if (!DataValidator.isName(request.getParameter("type"))) {
			request.setAttribute("type", PropertyReader.getValue("error.name", "Type"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("startDate"))) {
			request.setAttribute("startDate", PropertyReader.getValue("error.require", "Start Date"));
			isValid = false;
		} else if (!DataValidator.isDate(request.getParameter("startDate"))) {
			request.setAttribute("startDate", PropertyReader.getValue("error.date", "Start Date"));
			isValid = false;
		}

		if (DataValidator.isNull(request.getParameter("version"))) {
			request.setAttribute("version", PropertyReader.getValue("error.require", "version"));
			isValid = false;
		} else if (!DataValidator.isInteger(request.getParameter("version"))) {
			request.setAttribute("version", PropertyReader.getValue("error.name", "version"));
			isValid = false;
		}

		log.info("UserCtl validate Method Ended");
		return isValid;
	}

	/**
	 * Populates UserBean from request parameters.
	 * 
	 * @param request HttpServletRequest
	 * @return BaseBean populated UserBean
	 */
	@Override
	protected BaseBean populateBean(HttpServletRequest request) {
		log.info("UserCtl populateBean Method Started");

		InitiativeBean bean = new InitiativeBean();

		bean.setId(DataUtility.getLong(request.getParameter("id")));
		bean.setInitiativeName(DataUtility.getString(request.getParameter("initiativeName")));
		bean.setType(DataUtility.getString(request.getParameter("type")));
		bean.setStartDate(DataUtility.getDate(request.getParameter("startDate")));
		bean.setVersion(DataUtility.getInt(request.getParameter("version")));

		populateDTO(bean, request);

		log.info("UserCtl populateBean Method Ended");
		return bean;
	}

	/**
	 * Handles GET request for user form. If id is passed, loads UserBean to edit.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("UserCtl doGet Method Started");

		long id = DataUtility.getLong(request.getParameter("id"));

		InitiativeModel model = new InitiativeModel();

		if (id > 0) {
			
			try {
				InitiativeBean bean = model.findByPk(id);
				ServletUtility.setBean(bean, request);
			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		}

		log.info("UserCtl doGet Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Handles POST request for save, update, cancel, and reset operations.
	 * 
	 * @param request  HttpServletRequest
	 * @param response HttpServletResponse
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		log.info("UserCtl doPost Method Started");

		String op = DataUtility.getString(request.getParameter("operation"));

		InitiativeModel model = new InitiativeModel();

		long id = DataUtility.getLong(request.getParameter("id"));

		if (OP_SAVE.equalsIgnoreCase(op)) {
			InitiativeBean bean = (InitiativeBean) populateBean(request);

			try {
				model.add(bean);
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Initiative Add Successful", request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		} else if (OP_UPDATE.equalsIgnoreCase(op)) {
			InitiativeBean bean = (InitiativeBean) populateBean(request);

			try {
				if (id > 0) {
					model.update(bean);
				}
				ServletUtility.setBean(bean, request);
				ServletUtility.setSuccessMessage("Initiative Updated Successfully!", request);

			} catch (ApplicationException e) {
				log.error(e);
				ServletUtility.handleException(e, request, response);
				return;
			}
		} else if (OP_CANCEL.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.INITIATIVE_LIST_CTL, request, response);
			return;
		} else if (OP_RESET.equalsIgnoreCase(op)) {
			ServletUtility.redirect(ORSView.INITIATIVE_CTL, request, response);
			return;
		}

		log.info("UserCtl doPost Method Ended");
		ServletUtility.forward(getView(), request, response);
	}

	/**
	 * Returns the view for User form.
	 * 
	 * @return String view path
	 */
	@Override
	protected String getView() {
		return ORSView.INITIATIVE_VIEW;
	}

}
