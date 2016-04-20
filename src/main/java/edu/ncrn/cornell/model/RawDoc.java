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

	@org.hibernate.annotations.Type(type="edu.ncrn.cornell.model.SQLXMLType")
	@Column(name="raw_xml")
	private String rawXml;

	@Column(name="schema_id")
	private String schemaId;

	@Column(name="schema_version")
	private String schemaVersion;

	private String sha256;

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

	public String getSchemaId() {
		return this.schemaId;
	}

	public void setSchemaId(String schemaId) {
		this.schemaId = schemaId;
	}

	public String getSchemaVersion() {
		return this.schemaVersion;
	}

	public void setSchemaVersion(String schemaVersion) {
		this.schemaVersion = schemaVersion;
	}

	public String getSha256() {
		return this.sha256;
	}

	public void setSha256(String sha256) {
		assert(sha256.length() == 64);
		this.sha256 = sha256;
	}

}