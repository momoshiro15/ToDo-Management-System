package com.dmm.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.form.TaskForm;
@Controller
public class MainContoroller {
	@Autowired
	private TasksRepository repo;
	@GetMapping("/main")
	public String home(Model model) {
		// 逆順で投稿をすべて取得する
		List<Tasks> list = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
//    Collections.reverse(list); //普通に取得してこちらの処理でもOK
		model.addAttribute("posts", list);
		TaskForm postForm = new TaskForm();
		model.addAttribute("postForm", postForm);
		return "main";
	}
	
	@GetMapping("/main/create/{date}")
	public String register() {
		return "create";
	}
	
//	@GetMapping("/main/create/{date}")
//	public String edit() {
//		return "edit";
//	}
}
