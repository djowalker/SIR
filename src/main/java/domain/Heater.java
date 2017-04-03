package domain;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "heater")
@PrimaryKeyJoinColumn(name = "id")
public class Heater extends IntelligentDevice{
static final long serialVersionUID = 1L;

 	private String source;
 	

	public Heater(String ref, Home hom, int conso,String sour) {
 		super(ref, hom, conso);
 		this.source = sour;
 	}
 	public Heater(String ref,int conso,String sour){
 		super(ref,conso);
 		this.source = sour;
 	}
 	
 	public String getSource() {
		return source;
	}
	public void setSource(String source) {
		this.source = source;
	}
	
	@Override
	public String toString() {
		return "Heater [id=" + getId() + ", reference=" + getReference() + ", consommationAvg=" + getConsommationAvg() +
				", Source d'energie : "+this.source+" ]";
	}	
}
