package com.dmm.task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TaskRepository;

@Controller
public class MainController {
	
	@Autowired
	private TaskRepository repo;
	
	/**
	 * カレンダーを作成
	 * @param model モデル
	 * @return 遷移先
	 */
	@GetMapping("/main")
	public String main(Model model) {
		// 1週間分のリスト
		List<List<Tasks>> weeks = new ArrayList<>();
			weeks.addAll(Collections.nCopies(7, null));			
		
		// その月の1日のLocalDateを取得
		LocalDate d1 = LocalDate.now().withDayOfMonth(1);
		// 曜日を取得
		DayOfWeek week = d1.getDayOfWeek();
		// マイナスして前月分のLocalDateを求める
		LocalDate lastM = d1.minusMonths(1);
		
		
		
		
		return "/main";
		
		
		
	}
}
