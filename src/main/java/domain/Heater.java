package domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Heater {

	private Long id;
	private String reference;
	private int consommationAvg;
	private Home home;
	
	public Heater() {
	}

	public Heater(String reference, int consommationAvg) {
		this.reference = reference;
		this.consommationAvg = consommationAvg;
	}
	
	public Heater(String reference) {
		this.reference = reference;
	}

	public Heater(Long id, String reference, int consommationAvg) {
		this.id = id;
		this.reference = reference;
		this.consommationAvg = consommationAvg;
	}

	@Id
	@GeneratedValue
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}

	public int getConsommationAvg() {
		return consommationAvg;
	}

	public void setConsommationAvg(int consommationAvg) {
		this.consommationAvg = consommationAvg;
	}
	
	@ManyToOne
	public Home getHome(){
		return this.home;
	}
	
	public void setHome(Home home){
		this.home = home;
	}

	@Override
	public String toString() {
		return "Heater [id=" + id + ", reference=" + reference + ", consommationAvg=" + consommationAvg + "]";
	}



	
	
	
}
