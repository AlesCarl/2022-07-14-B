package it.polito.tdp.nyc.model;

public class Event implements Comparable<Event> {

	
	public enum EventType {
		SHARE,
		STOP	
	}
	
	EventType type; 
	int durata; 
	int giorno;
	String Nta; 
	
	public Event(EventType type, int durata, int giorno,String Nta) {
		super();
		this.type = type;
		this.durata = durata;
		this.Nta=Nta; 
		this.giorno = giorno;
	}
	
	

	public EventType getType() {
		return type;
	}

	public void setType(EventType type) {
		this.type = type;
	}

	public int getDurata() {
		return durata;
	}

	public void setDurata(int durata) {
		this.durata = durata;
	}

	public int getGiorno() {
		return giorno;
	}

	public void setGiorno(int giorno) {
		this.giorno = giorno;
	}

	public String getNta() {
		return Nta;
	}



	public void setNta(String nta) {
		Nta = nta;
	}



	@Override
	public int compareTo(Event o) {
		return this.giorno-o.giorno;
	} 
	
	
	
}
