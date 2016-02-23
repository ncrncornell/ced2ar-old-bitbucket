package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the field_inst database table.
 * 
 */
@Entity
@Table(name="field_inst")
@NamedQuery(name="FieldInst.findAll", query="SELECT f FROM FieldInst f")
public class FieldInst implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Long id;

	@Column(name="transaction_date")
	private Timestamp transactionDate;

	private String value;

	//bi-directional many-to-one association to FieldIndicy
	@OneToMany(mappedBy="fieldInst")
	private List<FieldIndicy> fieldIndicies;

	//bi-directional many-to-one association to Field
	@ManyToOne
	private Field field;

	//bi-directional many-to-one association to RawDoc
	@ManyToOne
	@JoinColumn(name="raw_doc_id")
	private RawDoc rawDoc;

	public FieldInst() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Timestamp getTransactionDate() {
		return this.transactionDate;
	}

	public void setTransactionDate(Timestamp transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public List<FieldIndicy> getFieldIndicies() {
		return this.fieldIndicies;
	}

	public void setFieldIndicies(List<FieldIndicy> fieldIndicies) {
		this.fieldIndicies = fieldIndicies;
	}

	public FieldIndicy addFieldIndicy(FieldIndicy fieldIndicy) {
		getFieldIndicies().add(fieldIndicy);
		fieldIndicy.setFieldInst(this);

		return fieldIndicy;
	}

	public FieldIndicy removeFieldIndicy(FieldIndicy fieldIndicy) {
		getFieldIndicies().remove(fieldIndicy);
		fieldIndicy.setFieldInst(null);

		return fieldIndicy;
	}

	public Field getField() {
		return this.field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public RawDoc getRawDoc() {
		return this.rawDoc;
	}

	public void setRawDoc(RawDoc rawDoc) {
		this.rawDoc = rawDoc;
	}

}