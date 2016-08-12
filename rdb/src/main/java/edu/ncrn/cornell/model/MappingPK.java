package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the mapping database table.
 * 
 */
@Embeddable
public class MappingPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="schema_id", insertable=false, updatable=false)
	private String schemaId;

	@Column(name="schema_version", insertable=false, updatable=false)
	private String schemaVersion;

	@Column(name="field_id", insertable=false, updatable=false)
	private String fieldId;

	public MappingPK() {
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
	public String getFieldId() {
		return this.fieldId;
	}
	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof MappingPK)) {
			return false;
		}
		MappingPK castOther = (MappingPK)other;
		return 
			this.schemaId.equals(castOther.schemaId)
			&& this.schemaVersion.equals(castOther.schemaVersion)
			&& this.fieldId.equals(castOther.fieldId);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.schemaId.hashCode();
		hash = hash * prime + this.schemaVersion.hashCode();
		hash = hash * prime + this.fieldId.hashCode();
		
		return hash;
	}
}