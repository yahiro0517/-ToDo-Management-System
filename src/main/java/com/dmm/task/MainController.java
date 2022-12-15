package com.dmm.task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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
		// 1. 2次元表になるので、ListのListを用意する
		List<List<LocalDate>> month = new ArrayList<>();
		
		// 2. 1週間分のLocalDateを格納するListを用意する
		List<LocalDate> week = new ArrayList<>();
		
		// 3. その月の1日のLocalDateを取得
		LocalDate day;
		day = LocalDate.now();
		day = LocalDate.of(day.getYear(), day.getMonthValue(), 1);
		
		// 4. 曜日を表すDayOfWeekを取得し、上で取得したLocalDateに曜日の値（DayOfWeek#getValue)をマイナスして前月分のLocalDateを求め
		DayOfWeek w = day.getDayOfWeek();
		// マイナスして前月分のLocalDateを求める
		day = day.minusDays(w.getValue());
		
		// 5. 1日ずつ増やしてLocalDateを求めていき、2．で作成したListへ格納していき、1週間分詰めたら1．のリストへ格納する
		for(int i = 1; i <= 7; i++) {
			day = day.plusDays(1);
			week.add(day);
		}
		month.add(week);
		week = new ArrayList<>();    // 次週分のリストを用意
		
		// 6. 2週目以降は単純に1日ずつ日を増やしながらLocalDateを求めてListへ格納していき、土曜日になったら1．のリストへ格納して新しいListを生成する（月末を求めるにはLocalDate#lengthOfMonth()を使う）
		for(int i = 7; i <= day.lengthOfMonth(); i++) {
			day = day.plusDays(1);
			week.add(day);
			// 土曜日かどうかを判定
			if (w == DayOfWeek.SATURDAY) {
				month.add(week);
				week = new ArrayList<>();    // 次週分のリストを用意				
			}			
			// 7. 最終週の翌月分をDayOfWeekの値を使って計算し、6．で生成したリストへ格納し、最後に1．で生成したリストへ格納する
			day = day.plusDays(w.getValue());    // これでdayには翌月分
			week.add(day);
			
		}
		month.add(week);			
		
		model.addAttribute("matrix", month);
		return "/main";
		
	}
}
