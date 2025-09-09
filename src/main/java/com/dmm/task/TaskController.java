package com.dmm.task;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.form.TaskForm;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class TaskController {
	@Autowired
	private TasksRepository repo;

	@GetMapping("/main/create/{date}")
	public String register() {
		return "create";
	}
	/**
	 * 投稿を作成.
	 * 
	 * @param taskForm 送信データ
	 * @param user     ユーザー情報
	 * @return 遷移先
	 */
	@PostMapping("/main/create")
	public String create(@Validated TaskForm taskForm, BindingResult bindingResult,
			@AuthenticationPrincipal AccountUserDetails user, Model model) {
		Tasks task = new Tasks();
		task.setName(user.getName());
		task.setTitle(taskForm.getTitle());
		task.setText(taskForm.getText());
		task.setDate(taskForm.getDate().atStartOfDay());
		task.setDone(false);
		repo.save(task);

		return "redirect:/main";
	}

	/**
	 * 投稿を削除する
	 * 
	 * @param id 投稿ID
	 * @return 遷移先
	 */
	@PostMapping("/main/delete/{id}")
	public String delete(@PathVariable Integer id) {
		repo.deleteById(id);
		return "redirect:/main";
	}
	
	@GetMapping("/main/edit/{id}")
	public String edit(@PathVariable("id") Integer id, Model model) {
		Tasks task = repo.findById(id)
		        .get();

		    // ビューに渡す
		    model.addAttribute("task", task);

		return "edit";
	}
	
	@PostMapping("/main/edit/{id}")
	public String update(@PathVariable("id")Integer id,
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("title") String title,
            @RequestParam("text") String text,
            @RequestParam(value = "done", defaultValue = "false") boolean done) {
		Tasks task = repo.findById(id)
		        .get();

		        task.setDate(date.atStartOfDay()); 
		        task.setTitle(title);
		        task.setText(text);
		        task.setDone(done);

		        repo.save(task); 

		return "redirect:/main";
	}


}
