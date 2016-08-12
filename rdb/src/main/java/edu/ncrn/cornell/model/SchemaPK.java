package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the schema database table.
 * 
 */
@Embeddable
public class SchemaPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String id;

	private String version;

	public SchemaPK() {
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getVersion() {
		return this.version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof SchemaPK)) {
			return false;
		}
		SchemaPK castOther = (SchemaPK)other;
		return 
			this.id.equals(castOther.id)
			&& this.version.equals(castOther.version);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id.hashCode();
		hash = hash * prime + this.version.hashCode();
		
		return hash;
	}
}