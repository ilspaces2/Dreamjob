package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.job4j.dreamjob.store.PostStore;

@Controller
public class PostController {

    private final PostStore store = PostStore.instOf();

    /**
     * Метод posts принимает объект Model. Он используется Thymeleaf для поиска объектов,
     * которые нужны отобразить на виде.
     * В Model мы добавляет объект posts
     * model.addAttribute("posts", store.findAll());
     * Контроллер заполняет Model и передает два объекта в Thymeleaf – Model и View(posts.html).
     * Thymeleaf генерирует HTML и возвращает ее клиенту.
     * @param model
     * @return
     */
    @GetMapping("/posts")
    public String posts(Model model) {
        model.addAttribute("posts", store.findAll());
        return "posts";
    }
}

