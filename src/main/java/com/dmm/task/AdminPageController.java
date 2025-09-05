package com.dmm.task;

import org.springframework.web.bind.annotation.GetMapping;

public class AdminPageController {
	@GetMapping("/adminPage")
	public String adminPage() {
		return "adminPage";
	}
}
