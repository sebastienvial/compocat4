package bobst.catalog.compoCat4.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;



public class NodeBomDynamic {    
	
	private String item;
	private String designation;
	private String drawing;
	private Number level;
    private Boolean expandable;
    private Boolean isLoading;
	private Date dateD;
	private Date dateF;

    public NodeBomDynamic(String item, String designation, String drawing, Number level, Boolean expandable,
            Boolean isLoading, Date dateD, Date dateF) {
        this.item = item;
        this.designation = designation;
        this.drawing = drawing;
        this.level = level;
        this.expandable = expandable;
        this.isLoading = isLoading;
        this.dateD = dateD;
        this.dateF = dateF;
    }

    public NodeBomDynamic(String item, String designation, String drawing, Date dateD, Date dateF) {
        this.item = item;
        this.designation = designation;
        //this.drawing = drawing;
        this.level = 1;
        this.expandable = false;
        this.isLoading = false;
        this.dateD = dateD;
        this.dateF = dateF;

        
		if (drawing != null) {
			this.drawing = drawing + "_P01.svg";
			this.dateD = dateD;
			this.dateF = dateF;
		} else {
			this.drawing = "";
			try {
				this.dateD = new SimpleDateFormat("yyyy-MM-dd").parse("9999-01-01");
				this.dateF = new SimpleDateFormat("yyyy-MM-dd").parse("9999-01-01");
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
		}
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getDrawing() {
        return drawing;
    }

    public void setDrawing(String drawing) {
        this.drawing = drawing;
    }

    public Number getLevel() {
        return level;
    }

    public void setLevel(Number level) {
        this.level = level;
    }

    public Boolean getExpandable() {
        return expandable;
    }

    public void setExpandable(Boolean expandable) {
        this.expandable = expandable;
    }

    public Boolean getIsLoading() {
        return isLoading;
    }

    public void setIsLoading(Boolean isLoading) {
        this.isLoading = isLoading;
    }

    public Date getDateD() {
        return dateD;
    }

    public void setDateD(Date dateD) {
        this.dateD = dateD;
    }

    public Date getDateF() {
        return dateF;
    }

    public void setDateF(Date dateF) {
        this.dateF = dateF;
    }
    
    //Comparator for sorting the list by IdParent
	public static Comparator<NodeBomDynamic> NbIdComparator = new Comparator<NodeBomDynamic>() {
		public int compare (NodeBomDynamic n1, NodeBomDynamic n2) {
			String id1 = n1.getItem().toUpperCase();
			String id2 = n2.getItem().toUpperCase();
			//ascending order
			return id1.compareTo(id2);
		}
	};
}
