package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the field database table.
 * 
 */
@Entity
@NamedQuery(name="Field.findAll", query="SELECT f FROM Field f")
public class Field implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String description;

	@Column(name="display_name")
	private String displayName;

	//bi-directional many-to-one association to FieldInst
	@OneToMany(mappedBy="field")
	private List<FieldInst> fieldInsts;

	//bi-directional many-to-one association to Mapping
	@OneToMany(mappedBy="field")
	private List<Mapping> mappings;

	//bi-directional many-to-many association to Profile
	@ManyToMany(mappedBy="fields")
	private List<Profile> profiles;

	public Field() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDisplayName() {
		return this.displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public List<FieldInst> getFieldInsts() {
		return this.fieldInsts;
	}

	public void setFieldInsts(List<FieldInst> fieldInsts) {
		this.fieldInsts = fieldInsts;
	}

	public FieldInst addFieldInst(FieldInst fieldInst) {
		getFieldInsts().add(fieldInst);
		fieldInst.setField(this);

		return fieldInst;
	}

	public FieldInst removeFieldInst(FieldInst fieldInst) {
		getFieldInsts().remove(fieldInst);
		fieldInst.setField(null);

		return fieldInst;
	}

	public List<Mapping> getMappings() {
		return this.mappings;
	}

	public void setMappings(List<Mapping> mappings) {
		this.mappings = mappings;
	}

	public Mapping addMapping(Mapping mapping) {
		getMappings().add(mapping);
		mapping.setField(this);

		return mapping;
	}

	public Mapping removeMapping(Mapping mapping) {
		getMappings().remove(mapping);
		mapping.setField(null);

		return mapping;
	}

	public List<Profile> getProfiles() {
		return this.profiles;
	}

	public void setProfiles(List<Profile> profiles) {
		this.profiles = profiles;
	}

}