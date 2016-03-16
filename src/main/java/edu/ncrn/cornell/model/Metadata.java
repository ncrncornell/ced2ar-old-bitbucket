package edu.ncrn.cornell.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the metadata database table.
 * 
 */
@Entity
@NamedQuery(name="Metadata.findAll", query="SELECT m FROM Metadata m")
public class Metadata implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	private Integer id;

	private String handle;

	@Column(name="handle_version")
	private String handleVersion;

	private String title;

	public Metadata() {
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHandle() {
		return this.handle;
	}

	public void setHandle(String handle) {
		this.handle = handle;
	}

	public String getHandleVersion() {
		return this.handleVersion;
	}

	public void setHandleVersion(String handleVersion) {
		this.handleVersion = handleVersion;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

}