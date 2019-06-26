package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {
	
	private Graph<Airport,DefaultWeightedEdge> grafo;
	private ExtFlightDelaysDAO dao;
	private Map<Integer, Airport> idMapAirport;
	private Map<Airport,Integer> mapAirportCorrect;
	
	private List<Airport> best;
	private Integer tratteMax;
	private Airport destinazione;
	
	private Double pesoMax;
	private Double pesoCorrente;
	
	
	public Model() {
		this.dao = new ExtFlightDelaysDAO();
	}

	public void creaGrafo(Integer compagnie) {
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		this.idMapAirport = new HashMap<>();
		this.mapAirportCorrect = new HashMap<>();
		
		//load tutti aeroporti
		this.dao.loadAllAirports(idMapAirport);
		
		//load vertici
		this.dao.loadVertexOrigin(this.idMapAirport,this.mapAirportCorrect,compagnie);
		System.out.println("Origin "+this.mapAirportCorrect.size());
		this.dao.loadVertexDestination(this.idMapAirport,this.mapAirportCorrect,compagnie);
		System.out.println("Destination "+this.mapAirportCorrect.size());
		
		Graphs.addAllVertices(grafo, this.mapAirportCorrect.keySet());
		System.out.println("Vertici "+this.grafo.vertexSet().size());
		
		
		//load Archi
		List<Adiacenza> adiacenze = this.dao.loadAdiacenze(this.mapAirportCorrect, this.idMapAirport);
		
		for(Adiacenza a : adiacenze) {
			
			DefaultWeightedEdge edge = this.grafo.getEdge(a.getOrigine(), a.getDestinazione());
			
			if(edge==null) {
				Graphs.addEdge(grafo, a.getOrigine(), a.getDestinazione(), a.getPeso());
			}else {
				Double peso = this.grafo.getEdgeWeight(edge)+a.getPeso();
				this.grafo.removeEdge(edge);
				Graphs.addEdge(grafo, a.getOrigine(), a.getDestinazione(), peso);
			}
		}
		
		System.out.println("Archi: "+this.grafo.edgeSet().size());
		
	}
	
	
	public Set<Airport> vertexSet(){
		return this.grafo.vertexSet();
	}
	
	public List<Vicino> getVicini(Airport origine){
		
		List<Airport> vicini = Graphs.neighborListOf(grafo, origine);
		List<Vicino> v = new ArrayList<>();
		
		for(Airport vv : vicini) {
			v.add(new Vicino(vv, this.grafo.getEdgeWeight(this.grafo.getEdge(origine, vv))));
		}
		
		Collections.sort(v);
		
		return v;
	}

	public List<Airport> cercaItinerario(Airport origine, Airport destinazione, Integer tratte) {
		
		this.best = new ArrayList<>();
		this.tratteMax = tratte;
		
		this.destinazione = destinazione;
		this.pesoMax=0.0;
		this.pesoCorrente=0.0;
		
		List<Airport> parziale = new ArrayList<>();
		
		parziale.add(origine);
		
		//parziale con livello 0 = aeroporto origine
		this.cercaBest(parziale,0);
		
		return this.best;
		
		
	}

	private void cercaBest(List<Airport> parziale, int livello) {
		
		
		//arrivati al numero massimo di tratte controllo se
			//percorso valido = origine e destinazione compresi
			//ottimo = somma pesi > pesi correntti
		if(livello==this.tratteMax) {
			
			//l'ultimo è la destinazione
			if(parziale.get(parziale.size()-1).equals(destinazione)) {
				
				this.best = new ArrayList<>(parziale);
				this.pesoMax = this.pesoCorrente;
				return;
				
			}else {
				return;
			}
			
			
		}
		
		
		
		
		
		//prendo vicini dell'ultimo elemento
			//controllo se aggiunta è valida = ho ancora delle tratte e non aggiungo la destinazione
			//ULTIMA TRATTA POSSO AGGIUNGERLA
		Airport last = parziale.get(parziale.size()-1);
		List<Airport> vicini = Graphs.neighborListOf(grafo, last);
		
		for(Airport a : vicini) {
			
			if(isValid(parziale,a,livello)) {
				//aggiungo
				parziale.add(a);
				
				//calcolo il peso dell'aggiunt
				Double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(last, a));
				this.pesoCorrente+=peso;
				
				//vado avanti
				this.cercaBest(parziale, livello+1);
				
				parziale.remove(parziale.size()-1);
				this.pesoCorrente-=peso;
			}
			
		}
		
	}

	private boolean isValid(List<Airport> parziale, Airport a, int livello) {
		
		//non posso aggiungere la destinazione
		if(livello+1<this.tratteMax) {
			
			if(a.equals(destinazione)) {
				return false;
			}else {
				return true;
			}
		//VA BENE QUALSIASI AGGIUNTA se all'ultimo	
		}else {
			return true;
		}

	}

	public Double getPeso() {
		// TODO Auto-generated method stub
		return this.pesoMax;
	}

}
