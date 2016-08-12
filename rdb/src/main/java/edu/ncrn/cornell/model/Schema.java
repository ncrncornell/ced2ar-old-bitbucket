package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;

import edu.ncrn.cornell.model.SchemaPK;

import java.util.List;

/**
 * The persistent class for the schema database table.
 * 
 */
@Entity
@NamedQuery(name="Schema.findAll", query="SELECT s FROM Schema s")
public class Schema implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private SchemaPK id;

	private String url;

	public Schema() {
	}

	public SchemaPK getId() {
		return this.id;
	}

	public void setId(SchemaPK id) {
		this.id = id;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}