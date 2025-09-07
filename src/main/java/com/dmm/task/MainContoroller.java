package com.dmm.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.service.AccountUserDetails;
import com.dmm.task.service.CalendarService;
@Controller
public class MainContoroller {
	@Autowired
	private TasksRepository repo;
	@GetMapping("/main")
	public String showCalender(@RequestParam int year,
            @RequestParam int month,
            @AuthenticationPrincipal AccountUserDetails user,
            Model model) {
		
		 // カレンダーの2次元日付リスト
        List<List<LocalDate>> calendarList = new ArrayList<>();
        model.addAttribute("calendar", calendarList);

        // 期間を指定してタスクを取得
        LocalDateTime from = LocalDate.of(year, calendarList, 1).atStartOfDay();
        LocalDateTime to = from.withDayOfMonth(from.toLocalDate().lengthOfMonth());

        List<Tasks> tasks = repo.findByDateBetween(from, to, user.getName());

        // 日付ごとにタスクをまとめる
        MultiValueMap<LocalDate, Tasks> taskMap = CalendarService.groupTasksByDate(tasks);
        model.addAttribute("taskMap", taskMap);

		
		
		
//		// 逆順で投稿をすべて取得する
//		List<Tasks> list = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
////    Collections.reverse(list); //普通に取得してこちらの処理でもOK
//		model.addAttribute("tasks", list);
//		TaskForm taskForm = new TaskForm();
//		model.addAttribute("TaskForm", taskForm);
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
