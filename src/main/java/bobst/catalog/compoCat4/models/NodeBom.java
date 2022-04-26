package bobst.catalog.compoCat4.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class NodeBom {
	
	private String name;
	private String id;
	private String parentId;
	private String drawing;
	private Date dateD;
	private Date dateF;
	private String path;
	
	public NodeBom(String name, String id, String parentId, String drawing, Date dateD, Date dateF, String path)  {
		super();
		this.name = name;
		this.id = id;
		this.parentId = parentId;
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
		this.path = path;

	}

		

	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}



	public Date getDateF() {
		return dateF;
	}



	public void setDateF(Date dateF) {
		this.dateF = dateF;
	}



	public Date getDateD() {
		return dateD;
	}



	public void setDateD(Date dateD) {
		this.dateD = dateD;
	}



	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public String getDrawing() {
		return drawing;
	}

	public void setDrawing(String drawing) {
		this.drawing = drawing;
	}
	
    //Comparator for sorting the list by IdParent
	public static Comparator<NodeBom> NbIdComparator = new Comparator<NodeBom>() {
		public int compare (NodeBom n1, NodeBom n2) {
			String id1 = n1.getParentId().toUpperCase();
			String id2 = n2.getParentId().toUpperCase();
			//ascending order
			return id1.compareTo(id2);
		}
	};	

}
