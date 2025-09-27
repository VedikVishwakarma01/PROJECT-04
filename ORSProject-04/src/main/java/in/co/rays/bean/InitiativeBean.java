package in.co.rays.bean;

import java.util.Date;

/**
 * Initiative JavaBean contains Initiative attributes
 * 
 * @author Vedik Vishwakarma
 *
 */
public class InitiativeBean extends BaseBean {

	private String initiativeName;
	private String type;
	private Date StartDate;
	private int version;

	public String getInitiativeName() {
		return initiativeName;
	}

	public void setInitiativeName(String initiativeName) {
		this.initiativeName = initiativeName;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Date getStartDate() {
		return StartDate;
	}

	public void setStartDate(Date startDate) {
		StartDate = startDate;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}
	
	@Override
	
	public String getKey() {
		return id + "";
	}
	
	@Override
	public String getValue() {
		return type;
	}

}
