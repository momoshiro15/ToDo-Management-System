package com.dmm.task;

import org.springframework.web.bind.annotation.GetMapping;

public class HomeContoroller {
	@GetMapping("/main")
	public String home() {
		return "main";
	}
}
