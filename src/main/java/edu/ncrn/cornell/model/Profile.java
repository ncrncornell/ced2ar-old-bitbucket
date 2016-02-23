package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the profile database table.
 * 
 */
@Entity
@NamedQuery(name="Profile.findAll", query="SELECT p FROM Profile p")
public class Profile implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private String id;

	private String description;

	//bi-directional many-to-many association to Field
	@ManyToMany
	@JoinTable(
		name="profile_field"
		, joinColumns={
			@JoinColumn(name="profile_id")
			}
		, inverseJoinColumns={
			@JoinColumn(name="field_id")
			}
		)
	private List<Field> fields;

	public Profile() {
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

	public List<Field> getFields() {
		return this.fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}

}