package mengToolKits;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.ResIterator;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.Statement;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.reasoner.Reasoner;
import org.apache.jena.reasoner.ReasonerRegistry;
import org.apache.jena.util.FileManager;
import org.apache.jena.vocabulary.OWL;
import org.apache.jena.vocabulary.RDF;
import org.apache.jena.vocabulary.RDFS;

public class RdfRW {
	
	static PrintWriter fOut=null;
	
	public static void main(String[] args) throws IOException {
	
		outOneClass("legalRole.ttl","TURTLE","http://xmlns.com/foaf/0.1/Person");
		//addOWLReasoner();
	}
	public static void readDemo(String fName, String format) throws IOException{
		
		Model oneRdf=ModelFactory.createDefaultModel();
		oneRdf.read(fName,format);
		
		Resource tSubj;		//subject
		Property tPred;		//predicate
		RDFNode tObj;		//object
		
		//iterate statements
		StmtIterator it1=oneRdf.listStatements();
		Statement aStmt=it1.nextStatement();
		
		System.out.println(aStmt.getSubject().toString());
		System.out.println(aStmt.getPredicate().toString());
		tObj=aStmt.getObject();
		if(tObj instanceof Resource ){
			System.out.println(tObj.toString());
		}else{
			System.out.printf("\"%s\"\n", tObj.toString());
		}
		// iterate subjects
		ResIterator it2=oneRdf.listSubjects();
		tSubj=it2.nextResource();
		System.out.println(tSubj.toString());
		
		// withProperty
		String root="http://www.w3.org/2000/01/rdf-schema#";
		Property pred=oneRdf.createProperty(root+"label");
		it2=oneRdf.listSubjectsWithProperty(pred);
		tSubj=it2.nextResource();
		System.out.println(tSubj.toString());
		
		//list statements with sub, pred, obj
		tSubj=oneRdf.createResource("http://www.frdc.com/legalDocument/ontologies/legalRole#affiliates");
		tObj=oneRdf.createLiteral("任职于","zh");		// create dataproperty   
		it1=oneRdf.listStatements(null, pred, (RDFNode)tObj);
		aStmt=it1.nextStatement();
		
		System.out.println(aStmt.getSubject().toString());
		System.out.println(aStmt.getPredicate().toString());
		System.out.println(aStmt.getObject().toString());
	}
	public static void addRDFSReasoner(){
		
		String NS = "urn:x-hp-jena:eg/";

		// Build a trivial example data set
		Model rdfsExample =  FileManager.get().loadModel("legal.owl","OWL");
		Property humanP = rdfsExample.createProperty(NS, "human");
		Property age = rdfsExample.createProperty(NS, "age");
	
		rdfsExample.add(age, RDFS.subPropertyOf, humanP);
		Resource a=rdfsExample.createResource(NS+"Person");
		
		a.addProperty(age, "5years");
		humanP.addProperty(RDFS.domain, a);
		InfModel inf = ModelFactory.createRDFSModel(rdfsExample);
		
		Resource a1 = inf.getResource("http://www.frdc.com/legalDocument/ontologies/legalRole#award");

		System.out.println("Statement:a " + a1.toString());
		System.out.println("Statement:age " + a1.getProperty(RDFS.subPropertyOf));


	}
	public static void addOWLReasoner(){
		
		Model schema = ModelFactory.createDefaultModel();//.loadModel("file:data/owlDemoSchema.owl");
		Model data = FileManager.get().loadModel("legalRole.owl","OWL");
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		reasoner = reasoner.bindSchema(schema);
		InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
		Resource award=infmodel.getResource("http://www.frdc.com/legalDocument/ontologies/legalRole#award");
		
		StmtIterator iter=award.listProperties(RDFS.domain);
		Statement stmt;
		RDFNode tObj;
		while (iter.hasNext()) {
			stmt=iter.next();
			if(stmt.getObject().isAnon()){
				getUnBObj((Resource)stmt.getObject());
			}else{
				System.out.println(stmt.getObject().toString());
			}
		}
		System.out.println("finished");


	}
	// out class, dataProperty, objectProperty
	// for example:  :actAs rdf:type owl:ObjectProperty ;
	public static void outSpecialType(String fName, String format, String typeStr) throws IOException{
		
		Model oneRdf = ModelFactory.createDefaultModel() ;
		oneRdf.read(fName,format) ;
		
		fOut=new PrintWriter("metaData.txt");
		
		String rdfs="http://www.w3.org/2000/01/rdf-schema#";
		String rdf="http://www.w3.org/1999/02/22-rdf-syntax-ns#";
//		
		Model m=ModelFactory.createDefaultModel();
		
		Property p=m.createProperty(rdf+"type");
		RDFNode o=m.createProperty(typeStr);
		Property p2=m.createProperty(rdfs+"label");
		
		StmtIterator iter = oneRdf.listStatements((Resource)null,p, (RDFNode)o);
		StmtIterator iter2;
		Statement stmt, stmt2;// = iter.nextStatement();
		
		Resource tSubj;		//subject
		Property tPred;		//predicate
		RDFNode tObj;		//object
		
		// print out the predicate, subject and object of each statement
		while (iter.hasNext()) {
			
			stmt= iter.nextStatement();
			tSubj=stmt.getSubject();
			
			iter2=oneRdf.listStatements(tSubj,p2,(RDFNode)null);
			if(iter2.hasNext()){
				stmt2=iter2.nextStatement();
				tObj=stmt2.getObject();
				if(tObj.toString().indexOf("zh")==-1)	fOut.print("**");
				fOut.println(tObj.toString()+"\t"+tSubj.toString());
			}else{
				fOut.println("**"+tSubj.toString());
			}
		}
		fOut.close();
	}
	public static void outObjProperty(String fName, String format) throws IOException{
		
		String owl="http://www.w3.org/2002/07/owl#";
		String objStr="ObjectProperty";
		outSpecialType(fName,format,owl+objStr);
	}
	public static void outDataProperty(String fName, String format) throws IOException{
		
		String owl="http://www.w3.org/2002/07/owl#";
		String objStr="DatatypeProperty";
		outSpecialType(fName,format,owl+objStr);
	}

	public static void outRDFClass(String fName, String format) throws IOException{
		
		String owl="http://www.w3.org/2002/07/owl#";
		String objStr="Class";
		outSpecialType(fName,format,owl+objStr);
	}
	
	//通过OWL推理得到所有domain包含该类的dataTypeProperty
	public static void outOneClass(String fName, String format, String oneStr) throws IOException{
				
		Model schema = ModelFactory.createDefaultModel();
		Model data = FileManager.get().loadModel(fName,format);
		Reasoner reasoner = ReasonerRegistry.getOWLReasoner();
		reasoner = reasoner.bindSchema(schema);
		InfModel infmodel = ModelFactory.createInfModel(reasoner, data);
		
		Resource s=infmodel.getResource(oneStr);
		
		fOut=new PrintWriter("metaData.txt");
		
		StmtIterator iter = infmodel.listStatements((Resource)null,RDF.type, OWL.DatatypeProperty);
		StmtIterator iter2;
		Statement stmt, stmt2;// = iter.nextStatement();
		
		Resource tSubj;		//subject
		RDFNode tObj;		//object
		// print out the predicate, subject and object of each statement
		List<Resource> resList=null;
		boolean isExist=false;
		while (iter.hasNext()) {
			
			stmt= iter.nextStatement();
			tSubj=stmt.getSubject();
			
			iter2=infmodel.listStatements(tSubj,RDFS.domain,(RDFNode)null);
			if(iter2.hasNext()){
				stmt2=iter2.nextStatement();
				tObj=stmt2.getObject();
				if(tObj.equals(s))
					fOut.println(tObj.toString()+"\t"+tSubj.toString());
				if(tObj.isAnon()){
					System.out.println(tSubj.toString());
					isExist=false;
					fOut.print("**");
					resList=explodeResource((Resource)tObj);
					for(Resource tProp:resList){
						if(tProp.equals(s)){
							fOut.print(tProp.toString()+"\t");
							isExist=true;
						}
					}
					if(isExist)	fOut.print(tSubj.toString()+"\t");
					fOut.println();
				}
			}
		}
		fOut.close();
		
	}
	//list 以oneRes为subject的所有property， 如有blankNode则展开, 和以oneRes为宾语的property
	public static void outLinkageOfNode(String fName, String format, String oneResStr) throws IOException{
				
		Model oneRdf = ModelFactory.createDefaultModel() ;
		oneRdf.read(fName,format) ;
		Resource oneRes=oneRdf.createResource(oneResStr);
		
		fOut=new PrintWriter("metaData.txt");
		Statement stmt;
		Property tPred;		//predicate
		RDFNode tObj;		//object
		// print out the predicate, object of oneRes
		String tStr="";
		fOut.println(oneRes.toString()+"-->");
		StmtIterator iter;
		iter=oneRes.listProperties();
		while (iter.hasNext()) {
			stmt= iter.next();
			tPred=stmt.getPredicate();
			tObj=stmt.getObject();
			fOut.print(tPred.toString()+"\t");
			if(tObj.isAnon()){
				tObj=getUnBObj((Resource)tObj);
			}
			fOut.println(tObj.toString());		
		}
		
		iter = oneRdf.listStatements((Resource)null,null, (RDFNode)oneRes);
		fOut.println("-->"+oneRes.toString());
		Resource tSubj;
		while(iter.hasNext()){
			stmt=iter.next();
			tSubj=stmt.getSubject();
			tPred=stmt.getPredicate();
			if(tSubj.isAnon()){
				tSubj=getUnBSubj(tSubj,oneRdf);
			}
			fOut.println(tPred.toString()+"\t"+tSubj.toString());
		}
		fOut.close();
	}
	//向上找subj
	public static Resource getUnBSubj(Resource aNode,Model tRdf){
		
		if(!aNode.isAnon())	return aNode;
		
		Statement stmt;
		Property tPred;		//predicate
		Resource tSubj=null;		//object
		StmtIterator iter = tRdf.listStatements((Resource)null,null, (RDFNode)aNode);
		if(iter.hasNext()){
			stmt=iter.next();
			tSubj=stmt.getSubject();
			tPred=stmt.getPredicate();
			if(tSubj.isAnon())
				tSubj=getUnBSubj(tSubj,tRdf);	
		}
		return tSubj;
	}
	// 得到RDF.List 的所有resource
	public static List<Resource> getList(Resource aNode){
	
		List<Resource> resList=new LinkedList<Resource>();
		if(!aNode.hasProperty(RDF.first)) return resList;
		resList.add(aNode.getPropertyResourceValue(RDF.first));
		if(!aNode.getPropertyResourceValue(RDF.rest).equals(RDF.nil))
			resList.addAll(getList(aNode.getPropertyResourceValue(RDF.rest)));
		return resList;
	}
	
	public static List<Resource> explodeResource(Resource aNode){
		
		List<Resource> resList=new LinkedList<Resource>();
	    Boolean needToTraverseNext=false;
	    
	    if(!aNode.isAnon()){
	    	resList.add(aNode);
	    	return resList;
	    }
	    
		List<Property> collectionProperties = 
				new LinkedList<Property>(Arrays.asList(OWL.unionOf,OWL.intersectionOf,RDF.first,RDF.rest));
	    
		for(Property cp:collectionProperties)
        {
            if(aNode.hasProperty(cp) && !aNode.getPropertyResourceValue(cp).equals(RDF.nil))
            {
                Resource nextResource=aNode.getPropertyResourceValue(cp);
                resList.addAll(explodeResource(nextResource));

                needToTraverseNext=true;
            }
        }
        if(!needToTraverseNext)
        {
        	resList.add(aNode);
        }
        return resList;
	}
	//向下遍历输出OBJ
	public static Resource getUnBObj(Resource aNode){
		
	    if(!aNode.isAnon()){
	    	System.out.println(aNode.toString());
	    	return aNode;
	    }
	    StmtIterator iter=aNode.listProperties();
	    Statement stmt;
	    Resource tRes=null;
	    while(iter.hasNext()){
	    	stmt=iter.next();
	    	if(aNode.equals(stmt.getObject())){
	    		continue;
	    	}
	    	System.out.println(stmt);	 
	    	tRes=getUnBObj((Resource)stmt.getObject());
	    }
	    return tRes;
	}
	
}

