package bobst.catalog.compoCat4.models;

public class Part {
	
	private String repere;
	private String numBobst;
	private String contentType;
	private String description;
	private String note;
	
	public Part() {
	}
	
	public Part(String repere, String numBobst, String contentType, String description, String note) {
		super();
		this.repere = repere;
		this.numBobst = numBobst;
		this.contentType = contentType;
		this.description = description;
		this.note = note;
	}
	
	

	public String getRepere() {
		return repere;
	}


	public void setRepere(String repere) {
		this.repere = repere;
	}


	public String getNumBobst() {
		return numBobst;
	}

	public void setNumBobst(String numBobst) {
		this.numBobst = numBobst;
	}


	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	

}
