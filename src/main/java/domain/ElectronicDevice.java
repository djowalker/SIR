package domain;

import javax.persistence.*;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Table(name = "electronic_device")
@PrimaryKeyJoinColumn(name = "id")
public class ElectronicDevice extends IntelligentDevice{

	static final long serialVersionUID = 1L;

	private String name ;
	
	
	public ElectronicDevice(String ref, Home hom, int conso,String nam) {
		super(ref, hom, conso);
		this.name = nam;
	}

	public ElectronicDevice(String ref,int conso,String nam){
		super(ref,conso);
		this.name = nam;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Electronic Device [id= "  + getId() + ", reference= " + getReference() + ", consommationAvg= " + getConsommationAvg() + 
				", name= " + this.name+" ]";
	}	
}
