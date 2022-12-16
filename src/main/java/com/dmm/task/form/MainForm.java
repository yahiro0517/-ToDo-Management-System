package com.dmm.task.form;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class MainForm {
	// titleへのバリデーション設定を追加
		//@Size(min = 1, max = 200)
		private String title;
		// textへのバリデーション設定を追加
		//@Size(min = 1, max = 200)
		private String text;
		
		private LocalDateTime date;
}
