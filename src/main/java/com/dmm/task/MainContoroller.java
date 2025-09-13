package com.dmm.task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TasksRepository;
import com.dmm.task.service.AccountUserDetails;
@Controller
public class MainContoroller {
	@Autowired
	private TasksRepository repo;
	@GetMapping("/main")
	public String showCalender( @RequestParam(value = "date", required = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
    @AuthenticationPrincipal AccountUserDetails user,
    Model model) {
		
		if (date == null) {
	        date = LocalDate.now();
	    }

	    int year = date.getYear();
	    int month = date.getMonthValue();
		 // カレンダーの2次元日付リスト
        List<List<LocalDate>> matrix = new ArrayList<>();
        LocalDate firstDay = LocalDate.of(year, month, 1);
        int offset = firstDay.getDayOfWeek().getValue() % 7; 
        LocalDate start = firstDay.minusDays(offset);

        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
        int trailing = 6 - (lastDay.getDayOfWeek().getValue() % 7);
        LocalDate end = lastDay.plusDays(trailing);

        List<LocalDate> week = new ArrayList<>();
        LocalDate current = start;

        while (!current.isAfter(end)) {
            week.add(current);
            if (current.getDayOfWeek() == DayOfWeek.SATURDAY) {
                matrix.add(week);
                week = new ArrayList<>();
            }
            current = current.plusDays(1);
        }
        if (!week.isEmpty()) {
            matrix.add(week);
        }

        model.addAttribute("matrix", matrix);

        LocalDateTime from = firstDay.atStartOfDay();
        LocalDateTime to = lastDay.atTime(23, 59, 59);
        List<Tasks> taskList = 
        		Objects.equals(user.getUsername(), "admin") ?repo.findAllByDateBetween(start.atStartOfDay(), end.atStartOfDay()):repo.findByDateBetween(start.atStartOfDay(), end.atStartOfDay(),user.getName());
        
        // -------------------------
        //  タスクを LocalDate ごとに分類
        // -------------------------
        MultiValueMap<LocalDate, Tasks> taskMap = new LinkedMultiValueMap<>();
        for (Tasks task : taskList) {
            LocalDate dateKey = task.getDate().toLocalDate();
            taskMap.add(dateKey, task);
        }

        model.addAttribute("tasks", taskMap);
        String formattedMonth = firstDay.getYear() + "年" + firstDay.getMonthValue() + "月";
        model.addAttribute("month", formattedMonth);

        // 4. 前月と翌月のLocalDateを計算してモデルに追加
        LocalDate prevMonth = firstDay.minusMonths(1);
        LocalDate nextMonth = firstDay.plusMonths(1);
        model.addAttribute("prev", prevMonth);
        model.addAttribute("next", nextMonth);
		return "main";
	}
	
	
	
	
}
