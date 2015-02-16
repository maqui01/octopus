/**
 * 
 */
package papasoft.octopus.domain;



/**
 * @author maqui
 *
 */
public class SalesUser extends User {
	
	private static final long serialVersionUID = -4253145755592035340L;
	
	private Boolean canCreateCreditNotes = true;

	/**
	 * @return the canCreateCreditNotes
	 */
	public Boolean getCanCreateCreditNotes() {
		return canCreateCreditNotes;
	}

	/**
	 * @param canCreateCreditNotes the canCreateCreditNotes to set
	 */
	public void setCanCreateCreditNotes(Boolean canCreateCreditNotes) {
		this.canCreateCreditNotes = canCreateCreditNotes;
	}
	
}
