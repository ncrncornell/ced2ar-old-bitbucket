package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the profile_field database table.
 * 
 */
/**
 * The persistent class for the profile_field database table.
 * 
 */
@Entity
@Table(name="profile_field")
@NamedQuery(name="ProfileField.findAll", query="SELECT p FROM ProfileField p")
public class ProfileField implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	@Column(name="field_id")
	private String fieldId;

	@Column(name="profile_id")
	private String profileId;

	public ProfileField() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getFieldId() {
		return this.fieldId;
	}

	public void setFieldId(String fieldId) {
		this.fieldId = fieldId;
	}

	public String getProfileId() {
		return this.profileId;
	}

	public void setProfileId(String profileId) {
		this.profileId = profileId;
	}

}