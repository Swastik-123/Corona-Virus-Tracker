package com.example.demo.Controller;

import com.example.demo.service.coronaVirusDataService;

import java.util.List;

import com.example.demoModels.LocationState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;



@Controller
public class HomeController {
	
	
	@Autowired
	coronaVirusDataService coronaVirusDataService;
	
	@RequestMapping("/")
	public String home(Model model) {
		List<LocationState> value = coronaVirusDataService.getAllStats();
		int totalCase = value.stream().mapToInt(stat -> stat.getLatestTotalCases()).sum();//just for calculating total number of cases.
		int totalNewCase = value.stream().mapToInt(stat -> stat.getDiffFromPrevDay()).sum();
		
		model.addAttribute("locationState",coronaVirusDataService.getAllStats());
		model.addAttribute("totalListedCases",totalCase);
		model.addAttribute("totalNewCase",totalNewCase);
		return "home";
	}

}
