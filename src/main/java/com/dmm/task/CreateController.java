package com.dmm.task;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.dmm.task.data.repository.TaskRepository;

@Controller
public class CreateController {
	
	@Autowired
	private TaskRepository repo;
	
	/**
	 * 投稿を作成。
	 * @param postForm 送信データ
	 * @param user     ユーザー情報
	 * @return 遷移先
	 */
	
	/*
	@PostMapping("/create/create")
	public String create(@Validated PostForm postForm, BindingResult bindingResult,
			@AuthenticationPrincipal AccountUserDetails user, Model model) {
		// バリデーションの結果、エラーがあるかどうかチェック
		if (bindingResult.hasErrors()) {
			// エラーがある場合は投稿登録画面を返す
			List<Tasks> list = repo.findAll(Sort.by(Sort.Direction.DESC, "id"));
			model.addAttribute("posts", list);
			model.addAttribute("postForm", postForm);
			return "/posts";
		}

		Posts post = new Posts();
		post.setName(user.getName());
		post.setTitle(postForm.getTitle());
		post.setText(postForm.getText());
		post.setDate(LocalDateTime.now());

		repo.save(post);
	{
		return "create";
	}
	*/
}