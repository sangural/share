package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class maincontroler {
@RequestMapping("/")
		public String index(String s) {	
	
	
	return"index";
	}


}
