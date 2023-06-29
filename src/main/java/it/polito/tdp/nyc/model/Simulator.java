package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.nyc.model.Event.EventType;

public class Simulator {

	
	// 100 giorni tot di simulazione
	
	
	// parametri di input: 
	double probabilita;    // di condivisione di un nuovo file. in un NTA casuale del grafo
	int durata;           //  file condiviso rimosso dopo "durata" tempo
	SimpleDirectedWeightedGraph<String, DefaultWeightedEdge>  graph; 
	
	
	
	//variabili di uscita
	private Map <String,Integer> idMapTotShare;  //tiene conto degli share

		
		
	
		
	//coda degli eventi
	PriorityQueue<Event> queue;
	
	
	private ArrayList<String> listVertici;
	private Map <String,Integer> numShare;  
	/* tiene conto degli di chi SHARE in real time */ 
	

	
	
	public Simulator(SimpleDirectedWeightedGraph<String, DefaultWeightedEdge> graph, double probabilita, int durata) {
	
		this.graph= graph; 
		this.probabilita= probabilita; 
		this.durata= durata;  // in giorni
		
	}
	
	
	// EVENTI: 
	
	
	public void initialize() {
	
		this.queue = new PriorityQueue<Event>();
		
		this.numShare= new HashMap<String,Integer>(); 
		this.idMapTotShare= new HashMap<String,Integer>(); 
		
		
		for(String c: this.graph.vertexSet()) {
			this.numShare.put(c, 0);
			this.idMapTotShare.put(c, 0);			
		}
		
		this.listVertici = new ArrayList<>(this.graph.vertexSet());

		for(int t=0; t<100; t++) {
			
			if(Math.random()<probabilita) { // avviene la condivisione
				
				String nta= verticeCasuale(); 

				this.queue.add(new Event(EventType.SHARE,durata, t,nta));
		        // con t che tiene conto dei giorni, max 100
		 
			}
			
		}
		
	}



	/*
	 Ogni volta in cui un file è condiviso in un NTAName, nel giorno successivo esso risulterà 
	 ri-condiviso anche in uno degli NTA confinanti, in particolare in quello con il peso più 
	 alto (tra gli adiacenti che non abbiano attualmente alcun file condiviso 
	 */
	
	public void run() {
		
		
		
	      while (!this.queue.isEmpty()) {
				
				Event e= this.queue.poll();
				
				
				if(e.getGiorno()>=100)
					break; 
				

				// le  3 variabili dell'evento:
				int time= e.getGiorno();
				String NTA= e.getNta();
				int durata = e.getDurata();
				
				switch ( e.getType()) {
				
				
				case SHARE:
					
	             //  in entrambe le mappe incremento di 1.
					 this.numShare.put(NTA, this.numShare.get(NTA)+1); 
					 this.idMapTotShare.put(NTA, this.idMapTotShare.get(NTA)+1);

					 
					
					 //creo un nuovo evento di STOP quando scade la durata di "d" giorni.
						this.queue.add(new Event(EventType.STOP,0, time+durata ,NTA));
						
						 if(durata/2 !=0) { // arrotondata 
							 
							 String NtaNuovo= trovaNta(NTA); 

							 if(NtaNuovo!= null) {
								 // ricondivido su questo nuovo 
									this.queue.add(new Event(EventType.SHARE,time+1, durata/2 ,NtaNuovo));
							   }
							 
						 }
						 break; 
						 
						 
							/** 2o CASO:  **/  
							case STOP:
								
							//decremento il numero di file condivisi.	
							 this.numShare.put(NTA, this.numShare.get(NTA)-1);
							 break; 
							}			
				}
						
	      }
	     
	
private String verticeCasuale() {		 
	 
	 int n = (int) (Math.random() * this.listVertici.size());
	 return listVertici.get(n);

}
	
private String trovaNta(String nTA) {
	int max= -1; 
	String ntaBest=null; 
	
	//vedo tutti gli archi uscenti da questo: 
	for(DefaultWeightedEdge e: this.graph.outgoingEdgesOf(nTA) ) {
	
		String ntaVicino= Graphs.getOppositeVertex(this.graph, e, nTA);
		int peso= (int) this.graph.getEdgeWeight(e); 
		
		
	if(peso>max && this.numShare.get(ntaVicino)==0) {
		max= peso;
		ntaBest= ntaVicino;
	 }
		
		
	}
	
	return ntaBest;
}

}
