package it.polito.tdp.extflightdelays.model;

public class Adiacenza {
	
	private Airport origine;
	private Airport destinazione;
	private Integer peso;

	
	
	public Adiacenza(Airport origine, Airport destinazione, Integer peso) {
		super();
		this.origine = origine;
		this.destinazione = destinazione;
		this.peso = peso;
	}
	public Airport getOrigine() {
		return origine;
	}
	public void setOrigine(Airport origine) {
		this.origine = origine;
	}
	public Airport getDestinazione() {
		return destinazione;
	}
	public void setDestinazione(Airport destinazione) {
		this.destinazione = destinazione;
	}
	public Integer getPeso() {
		return peso;
	}
	public void setPeso(Integer peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return "Adiacenza [origine=" + origine + ", destinazione=" + destinazione + ", peso=" + peso + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((destinazione == null) ? 0 : destinazione.hashCode());
		result = prime * result + ((origine == null) ? 0 : origine.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Adiacenza other = (Adiacenza) obj;
		if (destinazione == null) {
			if (other.destinazione != null)
				return false;
		} else if (!destinazione.equals(other.destinazione))
			return false;
		if (origine == null) {
			if (other.origine != null)
				return false;
		} else if (!origine.equals(other.origine))
			return false;
		return true;
	}
	
	

}
