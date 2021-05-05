package com.project.sinchon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/test/*")
public class HostController {
	
	@RequestMapping(value="/getjsp")
	public String getJSP() {
		return "test";
	}
}
