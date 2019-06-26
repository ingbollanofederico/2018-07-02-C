package it.polito.tdp.extflightdelays.model;

public class Vicino implements Comparable<Vicino> {
	
	private Airport vicino;
	private Double peso;
	
	public Vicino(Airport vicino, Double peso) {
		super();
		this.vicino = vicino;
		this.peso = peso;
	}

	public Airport getVicino() {
		return vicino;
	}

	public void setVicino(Airport vicino) {
		this.vicino = vicino;
	}

	public Double getPeso() {
		return peso;
	}

	public void setPeso(Double peso) {
		this.peso = peso;
	}

	@Override
	public String toString() {
		return vicino + " " + peso;
	}

	@Override
	public int compareTo(Vicino o) {
		return -(this.peso.compareTo(o.getPeso()));
	}
	
	
	

}
