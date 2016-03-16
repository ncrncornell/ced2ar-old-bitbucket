package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the mapping database table.
 * 
 */
@Entity
@NamedQuery(name="Mapping.findAll", query="SELECT m FROM Mapping m")
public class Mapping implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private MappingPK id;

	private String xpath;

	//bi-directional many-to-one association to Field
	@ManyToOne
	private Field field;

	//bi-directional many-to-one association to Schema
	@ManyToOne
	@JoinColumns({
		@JoinColumn(name="schema_id", referencedColumnName="id", insertable = false, updatable = false),
		@JoinColumn(name="schema_version", referencedColumnName="version", insertable = false, updatable = false)
		})
	private Schema schema;

	public Mapping() {
	}

	public MappingPK getId() {
		return this.id;
	}

	public void setId(MappingPK id) {
		this.id = id;
	}

	public String getXpath() {
		return this.xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public Field getField() {
		return this.field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public Schema getSchema() {
		return this.schema;
	}

	public void setSchema(Schema schema) {
		this.schema = schema;
	}

}