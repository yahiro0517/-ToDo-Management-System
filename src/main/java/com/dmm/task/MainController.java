package com.dmm.task;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.dmm.task.data.entity.Tasks;
import com.dmm.task.data.repository.TaskRepository;
import com.dmm.task.form.MainForm;
import com.dmm.task.service.AccountUserDetails;

@Controller
public class MainController {
	
	@Autowired
	private TaskRepository repo;
	private String name;
	
	/**
	 * カレンダーを作成
	 * @param model モデル
	 * @return 遷移先
	 */
	@GetMapping("/main")
	public String main(@AuthenticationPrincipal AccountUserDetails user, Model model) {
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
			week.add(day);
			day = day.plusDays(1);
			
		}
		month.add(week);
		week = new ArrayList<>();    // 次週分のリストを用意
		
		// 6. 2週目以降は単純に1日ずつ日を増やしながらLocalDateを求めてListへ格納していき、土曜日になったら1．のリストへ格納して新しいListを生成する（月末を求めるにはLocalDate#lengthOfMonth()を使う）
		for(int i = 7; i <= day.lengthOfMonth(); i++) {
			week.add(day);
			// 曜日取得
			DayOfWeek w1 = day.getDayOfWeek();
			// 土曜日かどうかを判定			
			if (w1 == DayOfWeek.SATURDAY) {
				month.add(week);
				week = new ArrayList<>();    // 次週分のリストを用意		
			}
			day = day.plusDays(1);
		}
		// 7. 最終週の翌月分をDayOfWeekの値を使って計算し、6．で生成したリストへ格納し、最後に1．で生成したリストへ格納する
		w = day.getDayOfWeek();   // 6.まででdayを進めているので、この時点でdayには月末が入っている。
		int nextMonthDays = 7 - w.getValue();    // 1週間分の日数 7 から上記を引くと、来月分の日数になる。

		for(int i = 1; i <= nextMonthDays; i++) {
			day = day.plusDays(1);
			week.add(day);
		}
		month.add(week);
		
		model.addAttribute("matrix", month);
		
		// カレンダー上部の年月表示
		YearMonth ym =YearMonth.now();
		// 文字列へ変換
		DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy年 MM月");
		String ym2 =f.format(ym);
		model.addAttribute("month", ym2);
		
		// カレンダーの日付（LocalDate）とタスク情報（Tasks）とをセットでもつためのMultiValueMap
		MultiValueMap<LocalDate, Tasks> tasks = new LinkedMultiValueMap<LocalDate, Tasks>();
		
		// タスクの追加
		List<Tasks> list;
		String name = user.getName();
		if (user.getAuthorities().stream().map(GrantedAuthority::getAuthority).anyMatch(a -> a.equals("ROLE_ADMIN"))) {
			System.out.println("適当な文字列");
			list = repo.findAll();			
		} else {
			// 当日のインスタンスを取得したあと、その月の1日のインスタンスを得る
			LocalDate start = LocalDate.now().withDayOfMonth(1);
			// その月の最後の日を取得
			int length = start.lengthOfMonth();
			LocalDate end = start.withDayOfMonth(length);
			list = repo.findByDateBetween(start.atTime(0, 0), end.atTime(0, 0), name);
		}
		for (Tasks t : list) {
			tasks.add(t.getDate().toLocalDate(), t);
		}
		
		// main.htmlの${tasks.get(day)}の ${tasks} へデータをマッピング
		model.addAttribute("tasks", tasks);		
		
		return "/main";
		
	}
	
	
	/**
	 * タスクの新規作成画面
	 */
	@GetMapping("/main/create/{date}")
	public String create() {
		
		return "create";
	}
	
	/**
	 * Edit画面
	 */
	@GetMapping("/main/edit/{id}")
	public String edit() {
		
		return "edit";
	}
	
	/**
	 * タスクの新規作成
	 */
	@PostMapping("/main/create")
	public String createPost(MainForm mainForm, @AuthenticationPrincipal AccountUserDetails user) {
		Tasks t = new Tasks();
		t.setName(user.getName());
		t.setTitle(mainForm.getTitle());
		t.setDate(mainForm.getDate().atTime(0,0));
		t.setText(mainForm.getText());
		t.setDone(false);

		repo.save(t);
				
		return "redirect:/main";
	}
	
	/**
	 * タスクの新規作成画面
	 */
	@PostMapping("/main/edit/{id}")
	public String ecitPost(MainForm mainForm, @AuthenticationPrincipal AccountUserDetails user, @PathVariable Integer id) {
		Tasks t = new Tasks();
		t.setId(id);
	    t.setName(user.getName());
		t.setTitle(mainForm.getTitle());
		t.setDate(mainForm.getDate().atTime(0,0));
		t.setText(mainForm.getText());
		t.setDone(mainForm.getDone());

		repo.save(t);
		
		return "redirect:/main";
	}
	
	/**
	 * 削除ボタン作成
	 */
	/*
	@PostMapping("/main/delete/{id}")
	public String delete(@PathVariable Integer id) {
		repo.deleteById(id);
		return "redirect:/main";
	}
	*/
}
