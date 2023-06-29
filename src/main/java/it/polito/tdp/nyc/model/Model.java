package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	NYCDao dao;
	private SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph;
	ArrayList<String> allBorghi; 
	List<String> allNTA; 
	
	
	
public Model() {
		
		this.allNTA= new ArrayList<>();
		this.allBorghi= new ArrayList<>();
		this.graph= new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
		this.dao= new NYCDao(); 
	}
	

public List<String> getAllBorhi(){
	return dao.getAllBorhi(); 
}
	
	
public void creaGrafo(String s) {

	allNTA= dao.getVertici(s); 
	
	/** VERTICI **/ 
	
	Graphs.addAllVertices(this.graph, this.allNTA);
//	System.out.println(this.graph.vertexSet().size());

	/*
	peso pari alla somma del numero di SSID distinti che 
	compaiono nei due NTAName adiacenti all’arco. 
	Qualora tale numero sia pari a zero, l’arco non deve 
	essere inserito. 
	L’orientamento dell’arco deve andare 
	nella direzione del conteggio degli SSID crescente.
	
	 */

	//TROVO GLI SSID di ciascun NTA
	for(String a1: this.allNTA) {
		for(String a2 : this.allNTA) {
			
			

		 if(a1.compareTo(a2)!=0) {
			List<String> listNTA1= dao.getAllSSDI(a1); 
			List<String> listNTA2= dao.getAllSSDI(a2); 
			
			int peso = getPeso(listNTA1,listNTA2);
			
			if(peso != 0) {
				
//				if(listNTA1.size()>listNTA2.size())
//				Graphs.addEdgeWithVertices(this.graph, a2, a1, peso);
				
//				if(listNTA2.size()>listNTA1.size())
					Graphs.addEdgeWithVertices(this.graph, a1, a2, peso);
				
			}
		}
	}
	}

	System.out.println(this.graph.vertexSet().size());
    System.out.println(this.graph.edgeSet().size());

	
}
/**  a stupidoooooooo: **/ 

private int getPeso(List<String> listNTA1, List<String> listNTA2) {
	
	
/** MI SERVONO GLI ELEMENTI distinti di entrambe le liste **/ 
	//...aggiungo tutto in una lista...
	
	for(String ss: listNTA2) {
		if(!listNTA1.contains(ss)) {
			listNTA1.add(ss);
		}
	}
	  return listNTA1.size(); 
}



// popola la txtResult con gli archi il cui
// peso sia minore del peso medio di tutti gli archi presenti nel
// grafo. 
// Gli archi vanno stampati in ordine crescente di peso.


public List<Arco> getAnalisiArchi() {
	
	double pesoMedio= getPesoMedio(); 

	List<Arco> temp= new ArrayList<>();
	
	for(DefaultWeightedEdge ee: this.graph.edgeSet()) {
		//peso minore del peso medio...
		if(pesoMedio> this.graph.getEdgeWeight(ee)) {
			
			String source= this.graph.getEdgeSource(ee);
			String out= this.graph.getEdgeTarget(ee); 
			Arco aa= new Arco(source, out,this.graph.getEdgeWeight(ee)); 
			temp.add(aa);
			
		}
	}
	return temp;	
}


public double getPesoMedio() {
	double sum= 0; 
	int cont= 0; 
	
	
	for(DefaultWeightedEdge ee: this.graph.edgeSet() ) {
		sum+= this.graph.getEdgeWeight(ee);
		cont++; 
	}
	
	return sum/cont ;
}




/***************  COLLEGA SIMULAZIONE E MODEL   **************/ 
public  Map<String, Integer>  simula(double probabilita, int durata ) 
{
	Simulator sim = new Simulator(this.graph, probabilita,durata);
	
	sim.initialize();
	sim.run();
	
	//this.nPassiSimulatore= sim.getnPassi();
	//return sim.getStanziali() ;
}































	
}
