package domain;

import java.io.Serializable;

import javax.persistence.*;


@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name="intelligent_devices")
public abstract class IntelligentDevice implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private long id;

	private String reference;

	private Home home;
	
	private int consommationAvg;
	
	public IntelligentDevice(String ref,Home hom,int conso){
		this.reference = ref;
		this.home = hom;
		this.consommationAvg = conso;
	}
	
	public IntelligentDevice(String ref,int conso){
		this.reference = ref;
		this.consommationAvg = conso;
	}
	
	@Id
	@GeneratedValue (strategy= GenerationType.AUTO)
	public long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column (name ="ref")
	public String getReference() {
		return reference;
	}
	public void setReference(String reference) {
		this.reference = reference;
	}
	
	@Column (name ="consoAvg")
	public int getConsommationAvg() {
		return consommationAvg;
	}
	public void setConsommationAvg(int consommationAvg) {
		this.consommationAvg = consommationAvg;
	}
	
	@ManyToOne
	public Home getHome() {
		return home;
	}

	public void setHome(Home home) {
		this.home = home;
	}
}
