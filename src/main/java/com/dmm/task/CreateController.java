package com.dmm.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.dmm.task.data.repository.TaskRepository;

@Controller
public class CreateController {
	
	@Autowired
	private TaskRepository repo;
	
	@GetMapping("/create")
	public String create() {
	return "create";	
	}
	
	/**
	 * 投稿を作成。
	 * @param postForm 送信データ
	 * @param user     ユーザー情報
	 * @return 遷移先
	 */
	
	/*
	@PostMapping("/main/create")
	public String create(@Validated CreatetForm createForm, BindingResult bindingResult,
			@AuthenticationPrincipal AccountUserDetails user, Model model) {
		// バリデーションの結果、エラーがあるかどうかチェック
		if (bindingResult.hasErrors()) {
			// エラーがある場合は投稿登録画面を返す
			List<Tasks> list = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("posts", list);
			model.addAttribute("postForm", postForm);
			return "/main/create";
		}

		Tasks task = new Tasks();
		
		task.setName(user.getName());
		task.setTitle(postForm.getTitle());
		task.setText(postForm.getText());
		task.setDate(LocalDateTime.now());
		//task.setDone(

		repo.save(task);
	{
		return "/main/create";
	}
	*/
}