package com.tp.todolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.tp.todolist.service.TodoService;

@Controller
public class TodoController {
    private final TodoService todoService;

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("todos", todoService.getAll());
        return "index";
    }

    @PostMapping("/add")
    public String addTodo(@RequestParam String title, @RequestParam String description) {
        todoService.create(title, description, false);
        return "redirect:/";
    }

    @GetMapping("/update/{id}")
    public String updateTodo(@PathVariable Long id) {
        todoService.update(id, true);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id) {
        todoService.delete(id);
        return "redirect:/";
    }
}
