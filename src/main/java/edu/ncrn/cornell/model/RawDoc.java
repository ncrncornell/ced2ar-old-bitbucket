package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;
import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the raw_doc database table.
 * 
 */
@Entity
@Table(name="raw_doc")
@NamedQuery(name="RawDoc.findAll", query="SELECT r FROM RawDoc r")
public class RawDoc implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	@Column(name="codebook_id")
	private String codebookId;

	@Column(name="last_sync")
	private Timestamp lastSync;

	@Column(name="raw_xml")
    @org.hibernate.annotations.Type(type="edu.ncrn.cornell.model.SQLXMLType")
    private String rawXml;

	@Column(name="sha256")
	private String sha256;

	//bi-directional many-to-one association to FieldInst
	@OneToMany(mappedBy="rawDoc")
	private List<FieldInst> fieldInsts;

	//bi-directional many-to-one association to Schema
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="schema_id", referencedColumnName="id"),
		@JoinColumn(name="schema_version", referencedColumnName="version")
		})
	private Schema schema;

	public RawDoc() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCodebookId() {
		return this.codebookId;
	}

	public void setCodebookId(String codebookId) {
		this.codebookId = codebookId;
	}

	public Timestamp getLastSync() {
		return this.lastSync;
	}

	public void setLastSync(Timestamp lastSync) {
		this.lastSync = lastSync;
	}

	public String getRawXml() {
		return this.rawXml;
	}

	public void setRawXml(String rawXml) {
		this.rawXml = rawXml;
	}

    public String getSha256() { return sha256; }

    public void setSha256(String sha256) {
        assert(sha256.length() == 64);
        this.sha256 = sha256;
    }

	public List<FieldInst> getFieldInsts() {
		return this.fieldInsts;
	}

	public void setFieldInsts(List<FieldInst> fieldInsts) {
		this.fieldInsts = fieldInsts;
	}

	public FieldInst addFieldInst(FieldInst fieldInst) {
		getFieldInsts().add(fieldInst);
		fieldInst.setRawDoc(this);

		return fieldInst;
	}

	public FieldInst removeFieldInst(FieldInst fieldInst) {
		getFieldInsts().remove(fieldInst);
		fieldInst.setRawDoc(null);

		return fieldInst;
	}

	public Schema getSchema() {
		return this.schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

}