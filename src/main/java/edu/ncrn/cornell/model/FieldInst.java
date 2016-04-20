package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Date;
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

	@SequenceGenerator(name="pk_sequence",sequenceName="field_inst_id_seq", allocationSize=1)
	@GeneratedValue(strategy=GenerationType.SEQUENCE,generator="pk_sequence")
	@Id
	private Long id;

	@Column(name="canonical_xpath")
	private String canonicalXpath;

	@Column(name="field_id")
	private String fieldId;

	@Column(name="raw_doc_id")
	private String rawDocId;

	@Basic(optional = false)
	@Column(name = "transaction_date", insertable = false, updatable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date transactionDate;

	private String value;

	public FieldInst() {
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCanonicalXpath() {
		return this.canonicalXpath;
	}

	public void setCanonicalXpath(String canonicalXpath) {
		this.canonicalXpath = canonicalXpath;
	}

	public String getFieldId() {
		return this.fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getRawDocId() {
		return this.rawDocId;
	}

	public void setRawDocId(String rawDocId) {
		this.rawDocId = rawDocId;
	}

	public Date getTransactionDate() {
		return this.transactionDate;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}