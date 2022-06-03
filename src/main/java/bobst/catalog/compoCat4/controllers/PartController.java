package bobst.catalog.compoCat4.controllers;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Collections;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import bobst.catalog.compoCat4.models.CatPageContent;
import bobst.catalog.compoCat4.models.NodeBom;
import bobst.catalog.compoCat4.models.NodeBomDynamic;
import bobst.catalog.compoCat4.models.Part;
import bobst.catalog.compoCat4.repositories.CatBomRepository;
import bobst.catalog.compoCat4.repositories.CatDocRepository;
import bobst.catalog.compoCat4.repositories.CatPageContentRepository;
import bobst.catalog.compoCat4.repositories.CatPageRepository;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@RestController
public class PartController {
  
	private static final String ORIGINS = "http://localhost:4200" ;
    //private static final String ORIGINS = "http://192.168.99.100:4200/";  //container

	private final CatPageContentRepository catPageContentRepository;
	private final CatBomRepository catBomRepository;
	private final CatDocRepository catDocRepository;
	private final CatPageRepository catPageRepository;

	private ArrayList<NodeBom> bomRes = new ArrayList<NodeBom>(); 
	private ArrayList<NodeBom> bomNew = new ArrayList<NodeBom>();		
	
	public PartController(CatPageContentRepository catPageContentRepository, CatBomRepository catBomRepository, CatDocRepository catDocRepository, CatPageRepository catPageRepository) {
		this.catPageContentRepository = catPageContentRepository;
		this.catBomRepository = catBomRepository;
		this.catDocRepository = catDocRepository;
		this.catPageRepository = catPageRepository;
	}
	
    public Date getDateEclatement(String idDoc){ 
		//search date eclatement of document
		java.sql.Timestamp ts = catDocRepository.findDateByIdoc(idDoc);
		return Date.valueOf(ts.toLocalDateTime().toLocalDate());
	}


	@CrossOrigin(origins = ORIGINS)
	@GetMapping("/parts")
	public ArrayList<Part> getPart() {		
		ArrayList<Part> partList = new ArrayList<Part>();
		return partList;
	}

	@CrossOrigin(origins = ORIGINS)
	@GetMapping ("/bom/{idDoc}")
	public ArrayList<NodeBom> getBom(@PathVariable String idDoc) {
		//search date eclatement of document
		java.sql.Timestamp ts = catDocRepository.findDateByIdoc(idDoc);
        Date dateEclatement = Date.valueOf(ts.toLocalDateTime().toLocalDate());

		ArrayList<NodeBom> bom = catBomRepository.findBomByIdDoc(idDoc, dateEclatement);
				
		for (NodeBom nodeBom : bom) {
			if ( ( (nodeBom.getDateD().before(dateEclatement)) & 
			       (nodeBom.getDateF().after(dateEclatement)) ) ||
			     (nodeBom.getDateD().equals(Date.valueOf("9999-01-01"))) ) {
               this.bomNew.add(nodeBom);
			} 			
		}

 	    for (NodeBom nodeBom : bomNew) {
			if (!nodeBom.getDrawing().isEmpty()) {
               this.bomRes.add(nodeBom);
			   //add parent until root
			   addParent(nodeBom);
			}
		} 

		//Collections.sort(this.bomRes,NodeBom.NbIdComparator);
		return bomRes;
		//return bomNew;
	}

	
	private void addParent(NodeBom nodeBom) {
		if (nodeBom.getParentId().equals('M')) {
			if (!this.bomRes.contains(nodeBom)) this.bomRes.add(nodeBom);
		} else {
			for (NodeBom nb : this.bomNew) {
				if (nb.getId().equals(nodeBom.getParentId())) {
					if (!this.bomRes.contains(nb)) {
						this.bomRes.add(nb);
						addParent(nb);
					}
				}
			}
		} 
	}

	@CrossOrigin(origins = ORIGINS)
	@GetMapping ("/bom/{idDoc}/{idParent}")
	public String[] getBomDynymic(@PathVariable String idDoc, @PathVariable String idParent) {
		return catBomRepository.findBomDynamic(idDoc, idParent);
	}

	@CrossOrigin(origins = ORIGINS)
	@GetMapping ("/bomd/{idDoc}/{idParent}")
	public ArrayList<NodeBomDynamic> getBomDynamic(@PathVariable String idDoc, @PathVariable String idParent) {
		//search date eclatement of document
		java.sql.Timestamp ts = catDocRepository.findDateByIdoc(idDoc);
        Date dateEclatement = Date.valueOf(ts.toLocalDateTime().toLocalDate());
		System.out.println(dateEclatement);
		ArrayList<NodeBomDynamic> bom = catBomRepository.findBomChildDynamic(idDoc, idParent);
System.out.println(bom.toString());
		
		ArrayList<NodeBomDynamic> bomNew = new ArrayList<NodeBomDynamic>();

		for (NodeBomDynamic nodeBom : bom) {
			if ( ( (nodeBom.getDateD().before(dateEclatement)) & 
			       (nodeBom.getDateF().after(dateEclatement)) ) ||
			     (nodeBom.getDateD().equals(Date.valueOf("9999-01-01"))) ) {
               bomNew.add(nodeBom);
			} 			
		}
		
		Collections.sort(bomNew,NodeBomDynamic.NbIdComparator);
		return bomNew;
	}

	@CrossOrigin(origins = ORIGINS)
	@GetMapping ("/bomPath/{idDoc}/{idParent}/{idToc}")
	public ArrayList<String> getPath(@PathVariable String idDoc, @PathVariable String idParent, @PathVariable String idToc) {
		return catBomRepository.findPath(idDoc,idParent,idToc);
	}
	

	@CrossOrigin(origins =  ORIGINS)	
	@GetMapping ("/search/{idDoc}/{searchTerm}")
	public ArrayList<NodeBom> getNodes(@PathVariable String idDoc, @PathVariable String searchTerm) {
		Date dateEclatement = this.getDateEclatement(idDoc);
		ArrayList<NodeBom> bom = catBomRepository.findNodes(idDoc,searchTerm.toUpperCase());
		ArrayList<NodeBom> bomNew = new ArrayList<NodeBom>();

		for (NodeBom nodeBom : bom) {
			if ( ( (nodeBom.getDateD().before(dateEclatement)) & 
			       (nodeBom.getDateF().after(dateEclatement)) ) ||
			     (nodeBom.getDateD().equals(Date.valueOf("9999-01-01"))) ) {
               bomNew.add(nodeBom);
			} 			
		}
		
		return bomNew;
	}


	@PostMapping("/catPageContent")
    @ResponseStatus(HttpStatus.CREATED)
    public CatPageContent createCPC(@RequestBody CatPageContent catPageContent) {
        return catPageContentRepository.save(catPageContent);
    }
	
	
	@GetMapping ("/catPageContent/{idPage}")
	public ArrayList<CatPageContent> getContentByIdPage(@PathVariable String idPage){
		return catPageContentRepository.findByIdPage(idPage);
	}
	
    @CrossOrigin(origins = ORIGINS)
	@GetMapping ("/catPartsPageContent/{idPage}")
	public ArrayList<Part> getPartsContentByIdPage(@PathVariable String idPage){
		System.out.println("ok ");
		return catPageContentRepository.findPartsByIdPage(idPage);
	}

	@CrossOrigin(origins = ORIGINS)
	@GetMapping("/drawingsE43/{idE43}")
	public ArrayList<String> getDrawingsBrother(@PathVariable String idE43){
		return catPageRepository.findAllDrawingsBrother(idE43);
	}

	@CrossOrigin(origins = ORIGINS)
	@GetMapping("/drawing/{idDoc}/{idItem}")
    public ArrayList<String> getDrawingName(@PathVariable String idDoc , @PathVariable String idItem){
		Date d = this.getDateEclatement(idDoc);
		System.out.println("Affichage date : " + d);
		System.out.println("Affivhage return :" + catPageRepository.findDrawingFromItem(idItem, this.getDateEclatement(idDoc)));
		return catPageRepository.findDrawingFromItem(idItem, this.getDateEclatement(idDoc));
	}

	@CrossOrigin(origins = ORIGINS)
	@GetMapping("/drawings/{fileName:.+}")
	public ResponseEntity downloadFileFromLocal(@PathVariable String fileName, Object HttpHeaders) {
		Path path = Paths.get("svg/" + fileName);
		if (path.toFile().exists()) {
			Resource resource = null;
			try {
				resource = new UrlResource(path.toUri());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
			String contentType = "image/svg+xml";
			return ResponseEntity.ok()
					.contentType(MediaType.parseMediaType(contentType))
					//.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + ( resource).getFilename() + "\"")
					.body(resource);
		} else {
			return ResponseEntity.ok()
					.body("not found");
		}
	}
	
	
}
