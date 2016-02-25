package edu.ncrn.cornell.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import edu.ncrn.cornell.model.Field;
import edu.ncrn.cornell.model.dao.FieldDao;

@Controller
public class FieldController {

	@Autowired
	private FieldDao fieldDao;

	@RequestMapping(value = "/field", method = RequestMethod.GET)
	public String welcome(Model model) {
		model.addAttribute("subTitl","Welcome");
		model.addAttribute("metaDesc","Welcome to CED2AR.");
		
		List<Field> fields = fieldDao.findAll();
		model.addAttribute("fields", fields);
		
		long count = fieldDao.count();
		System.out.println("[FieldController]:: number of records in Field table: "+count);
		
		return "field";
	}
}
