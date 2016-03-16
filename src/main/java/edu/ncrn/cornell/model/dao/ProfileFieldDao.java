package edu.ncrn.cornell.model.dao;

import java.util.List;

import edu.ncrn.cornell.model.Profile;
import edu.ncrn.cornell.model.ProfileField;

public interface ProfileFieldDao extends BaseRepository<ProfileField, Integer> {
	
	List<ProfileField> findByProfileId(String Id);
	
}
