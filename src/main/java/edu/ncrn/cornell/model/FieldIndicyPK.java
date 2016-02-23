package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the field_indicies database table.
 * 
 */
@Embeddable
public class FieldIndicyPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="field_inst_id", insertable=false, updatable=false)
	private Long fieldInstId;

	private String index;

	public FieldIndicyPK() {
	}
	public Long getFieldInstId() {
		return this.fieldInstId;
	}
	public void setFieldInstId(Long fieldInstId) {
		this.fieldInstId = fieldInstId;
	}
	public String getIndex() {
		return this.index;
	}
	public void setIndex(String index) {
		this.index = index;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof FieldIndicyPK)) {
			return false;
		}
		FieldIndicyPK castOther = (FieldIndicyPK)other;
		return 
			this.fieldInstId.equals(castOther.fieldInstId)
			&& this.index.equals(castOther.index);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.fieldInstId.hashCode();
		hash = hash * prime + this.index.hashCode();
		
		return hash;
	}
}