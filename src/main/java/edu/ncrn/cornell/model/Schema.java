package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;
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

	//bi-directional many-to-one association to Mapping
	@OneToMany(mappedBy="schema")
	private List<Mapping> mappings;

	//bi-directional many-to-one association to RawDoc
	@OneToMany(mappedBy="schema")
	private List<RawDoc> rawDocs;

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

	public List<Mapping> getMappings() {
		return this.mappings;
	}

	public void setMappings(List<Mapping> mappings) {
		this.mappings = mappings;
	}

	public Mapping addMapping(Mapping mapping) {
		getMappings().add(mapping);
		mapping.setSchema(this);

		return mapping;
	}

	public Mapping removeMapping(Mapping mapping) {
		getMappings().remove(mapping);
		mapping.setSchema(null);

		return mapping;
	}

	public List<RawDoc> getRawDocs() {
		return this.rawDocs;
	}

	public void setRawDocs(List<RawDoc> rawDocs) {
		this.rawDocs = rawDocs;
	}

	public RawDoc addRawDoc(RawDoc rawDoc) {
		getRawDocs().add(rawDoc);
		rawDoc.setSchema(this);

		return rawDoc;
	}

	public RawDoc removeRawDoc(RawDoc rawDoc) {
		getRawDocs().remove(rawDoc);
		rawDoc.setSchema(null);

		return rawDoc;
	}

}