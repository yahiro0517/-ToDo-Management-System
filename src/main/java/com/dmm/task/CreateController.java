package com.dmm.task;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CreateController {
	@GetMapping("/create")
	String create() {
		return "create";
	}
}