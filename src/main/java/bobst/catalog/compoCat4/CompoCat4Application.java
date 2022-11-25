package bobst.catalog.compoCat4;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.xml.sax.SAXException;

import bobst.catalog.compoCat4.services.WatchCaptureService;

@SpringBootApplication

/* Branch Master4 
 * New Branch for BOBST developpement regarding PartsView 4.0 
 * Date  : 25.11.2022
 * Autor : Vial Sébastien
 */

public class CompoCat4Application {

	public static void main(String[] args) throws InterruptedException, IllegalStateException, ParserConfigurationException, SAXException {
		 
		SpringApplication.run(CompoCat4Application.class, args);
		WatchCaptureService.run();
	}

}
