package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the field_indicies database table.
 * 
 */
@Entity
@Table(name="field_indicies")
@NamedQuery(name="FieldIndicy.findAll", query="SELECT f FROM FieldIndicy f")
public class FieldIndicy implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private FieldIndicyPK id;

	@Column(name="index_value")
	private String indexValue;

	//bi-directional many-to-one association to FieldInst
	@ManyToOne
	@JoinColumn(name="field_inst_id", insertable = false, updatable = false)
	private FieldInst fieldInst;

	public FieldIndicy() {
	}

	public FieldIndicyPK getId() {
		return this.id;
	}

	public void setId(FieldIndicyPK id) {
		this.id = id;
	}

	public String getIndexValue() {
		return this.indexValue;
	}

	public void setIndexValue(String indexValue) {
		this.indexValue = indexValue;
	}

	public FieldInst getFieldInst() {
		return this.fieldInst;
	}

	public void setFieldInst(FieldInst fieldInst) {
		this.fieldInst = fieldInst;
	}

}