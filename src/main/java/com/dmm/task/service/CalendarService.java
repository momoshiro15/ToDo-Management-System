package com.dmm.task.service;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.dmm.task.data.entity.Tasks;

public class CalendarService {
	  public static List<List<LocalDate>> buildCalendar(int year, int month) {
	        List<List<LocalDate>> calendar = new ArrayList<>();

	        LocalDate firstDay = LocalDate.of(year, month, 1);
	        DayOfWeek firstDow = firstDay.getDayOfWeek();
	        int offset = firstDow.getValue() % 7;

	        LocalDate start = firstDay.minusDays(offset);
	        LocalDate lastDay = firstDay.withDayOfMonth(firstDay.lengthOfMonth());
	        DayOfWeek lastDow = lastDay.getDayOfWeek();
	        int trailing = 6 - (lastDow.getValue() % 7);
	        LocalDate end = lastDay.plusDays(trailing);

	        LocalDate current = start;
	        List<LocalDate> week = new ArrayList<>();

	        while (!current.isAfter(end)) {
	            week.add(current);
	            if (current.getDayOfWeek() == DayOfWeek.SATURDAY) {
	                calendar.add(week);
	                week = new ArrayList<>();
	            }
	            current = current.plusDays(1);
	        }

	        if (!week.isEmpty()) {
	            calendar.add(week);
	        }

	        return calendar;
	    }

	    public static MultiValueMap<LocalDate, Tasks> groupTasksByDate(List<Tasks> tasks) {
	        MultiValueMap<LocalDate, Tasks> taskMap = new LinkedMultiValueMap<>();
	        for (Tasks task : tasks) {
	            LocalDate dateKey = task.getDate().toLocalDate(); // 時間切り捨て
	            taskMap.add(dateKey, task);
	        }
	        return taskMap;
	    }
}
