package ri.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Properties;
import java.util.Set;
import org.semanticweb.owl.align.Alignment;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.AlignmentProcess;
import org.semanticweb.owl.align.AlignmentVisitor;
import org.semanticweb.owlapi.apibinding.OWLManager;
import org.semanticweb.owlapi.model.IRI;
import org.semanticweb.owlapi.model.OWLOntology;
import org.semanticweb.owlapi.model.OWLOntologyCreationException;
import org.semanticweb.owlapi.model.OWLOntologyManager;
import fr.inrialpes.exmo.align.impl.eval.PRecEvaluator;
import fr.inrialpes.exmo.align.impl.method.ClassStructAlignment;
import fr.inrialpes.exmo.align.impl.method.EditDistNameAlignment;
import fr.inrialpes.exmo.align.impl.method.NameAndPropertyAlignment;
import fr.inrialpes.exmo.align.impl.method.NameEqAlignment;
import fr.inrialpes.exmo.align.impl.method.SMOANameAlignment;
import fr.inrialpes.exmo.align.impl.renderer.RDFRendererVisitor;
import fr.inrialpes.exmo.align.parser.AlignmentParser;
import fr.inrialpes.exmo.ontowrap.OntowrapException;

public class AlignMain {
	
	public static int[] generateAlign () throws URISyntaxException , AlignmentException, FileNotFoundException, UnsupportedEncodingException, OWLOntologyCreationException {
		int[] matcheurResults = new int[6];
		//URI onto1= new URI("http://oaei.ontologymatching.org/tests/101/onto.rdf");
		//URI onto2=new URI("http://oaei.ontologymatching.org/tests/304/onto.rdf");
		
		URI onto1= new URI("http://www.irit.fr/recherches/MELODI/ontologies/FilmographieV1_Instances.owl");
		URI onto2= new URI("File:///Users/yuanbo/Documents/dbpedia_2014.owl");
		
		AlignmentProcess alignment1 =new NameEqAlignment ();
		//AlignmentProcess alignment2 =new EditDistNameAlignment();
		//AlignmentProcess alignment3 =new SMOANameAlignment();
		//AlignmentProcess alignment4 =new NameAndPropertyAlignment();
		//AlignmentProcess alignment5 =new ClassStructAlignment();
		NewMatcher alignment6 =new NewMatcher();
		
		alignment1.init (onto1 , onto2);
		alignment1.align(null,new Properties ());
		/*alignment2.init (onto1 , onto2);
		alignment2.align(null,new Properties ());
		alignment3.init (onto1 , onto2);
		alignment3.align(null,new Properties ());
		alignment4.init (onto1 , onto2);
		alignment4.align(null,new Properties ());
		alignment5.init (onto1 , onto2);
		alignment5.align(null,new Properties ());*/
		try {
			alignment6.init1 (onto1 , onto2);
		} catch (OWLOntologyCreationException e) {
			e.printStackTrace();
		} catch (OntowrapException e) {
			e.printStackTrace();
		}
		alignment6.align(null,new Properties ());
		
		matcheurResults[0] = alignment1.nbCells ();/*
		matcheurResults[1] = alignment2.nbCells ();
		matcheurResults[2] = alignment3.nbCells ();
		matcheurResults[3] = alignment4.nbCells ();
		matcheurResults[4] = alignment5.nbCells ();*/
		matcheurResults[5] = alignment6.nbCells ();
		
		evaluate(alignment1,"1");/*
		evaluate(alignment2,"2");
		evaluate(alignment3,"3");
		evaluate(alignment4,"4");
		evaluate(alignment5,"5");*/
		evaluate(alignment6,"6");
		
		render(alignment1,"/Users/yuanbo/Desktop/align1.rdf");/*
		render(alignment1,"/Users/yuanbo/Desktop/align2.rdf");
		render(alignment1,"/Users/yuanbo/Desktop/align3.rdf");
		render(alignment1,"/Users/yuanbo/Desktop/align4.rdf");
		render(alignment1,"/Users/yuanbo/Desktop/align5.rdf");*/
		render(alignment6,"/Users/yuanbo/Desktop/align6.rdf");
		
		OWLOntologyManager onto_manager = OWLManager.createOWLOntologyManager ();
		OWLOntology o1 = onto_manager.loadOntology(IRI.create(onto1));
		OWLOntology o2 = onto_manager.loadOntology(IRI.create(onto2));
		//LogMap2_Matcher logmap2 =new LogMap2_Matcher(o1,o2);
		
		//evaluate(logmap2,"logmap");
		
		//Set <MappingObjectStr> logmap2_mappings = logmap2.getLogmap2_Mappings();
		//System.out.println("LOGMAP2 Num.mappings :" + logmap2_mappings.size());
		
		return matcheurResults ;
	}
	
	public static void render (Alignment  alignment, String path) throws FileNotFoundException , UnsupportedEncodingException , AlignmentException
	{ 
		PrintWriter  writer ; 
		FileOutputStream f = new FileOutputStream (new File(path) ) ;
		writer = new PrintWriter (new BufferedWriter (new OutputStreamWriter (f,"UTF-8")),true) ;
		AlignmentVisitor  renderer = new RDFRendererVisitor (writer) ; 
		alignment.render(renderer) ;
		writer.flush() ;
		writer.close() ;
		}
	
	public static void evaluate(Alignment  alignment, String name) throws URISyntaxException , AlignmentException {
		URI reference = new URI("http://oaei.ontologymatching.org/tests/304/refalign.rdf");
		AlignmentParser  aparser = new AlignmentParser (0);
		Alignment  refalign = aparser.parse(reference);
		PRecEvaluator  evaluator = new PRecEvaluator(refalign , alignment);
		evaluator.eval(new Properties ());
		System.out.println(name+" Precision:"+ evaluator.getPrecision ());
		System.out.println(name+" Recall:"+ evaluator.getRecall ());
		System.out.println(name+" FMeasure :" + evaluator.getFmeasure ());
	}
	
	public static void main(String[] args) throws URISyntaxException, AlignmentException, FileNotFoundException, UnsupportedEncodingException, OWLOntologyCreationException{
		int[] results = generateAlign();
		int i ;
		for(i=0;i<results.length;i++){
			System.out.println("Numcorresp.générées:"+ results[i]);
		}
	}
}
