package ru.job4j.dreamjob.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import ru.job4j.dreamjob.model.Post;
import ru.job4j.dreamjob.store.PostStore;

import java.time.LocalDateTime;

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

    @GetMapping("/formAddPost")
    public String addPost(Model model) {
        model.addAttribute("posts", new Post(0, "Pascal", "School", LocalDateTime.now()));
        return "addPost";
    }

    /**
     * createPost обрабатывает пост запрос по адресу /createPost.
     * Для этого используется th:action="@{/createPost}" method="POST" в html документе.
     * @ModelAttribute Post post - собирает полученные данные из
     * инпутов в html документе (input type="text" class="form-control" name="name")
     *
     * Алтернативный вариант сбора информации с использованием класса HttpServletRequest
     * Необходимо будет получить данные с помощью гет методов и вставить их в объект при создании
     */
    @PostMapping("/createPost")
    public String createPost(@ModelAttribute Post post) {
        store.add(post);
        return "redirect:/posts";
    }

    /**
     * Получаем по /formUpdatePost/{postId} id объекта post
     * Передаем id в метод в качестве параметра, чтобы ее увидеть указываем @PathVariable("postId")
     * Ищем по id и передаем в модель post и возвращаем модель в updatePost
     */
    @GetMapping("/formUpdatePost/{postId}")
    public String formUpdatePost(Model model, @PathVariable("postId") int id) {
        model.addAttribute("post", store.findById(id));
        return "updatePost";
    }

    /**
     * После метода formUpdatePost попадаем на страницу редактирования /updatePost.
     * В нее передается объект post. Редактируем объект и возвращаемся на страницу постов
     */
    @PostMapping("/updatePost")
    public String updatePost(@ModelAttribute Post post) {
        store.update(post);
        return "redirect:/posts";
    }
}

