package com.example.todo.controller;

import com.example.todo.entity.Todo;
import com.example.todo.repository.TodoRepository;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller
public class TodoController {

    private final TodoRepository todoRepository;
    private final MessageSource messageSource;

    public TodoController(TodoRepository todoRepository, MessageSource messageSource) {
        this.todoRepository = todoRepository;
        this.messageSource = messageSource;
    }

    // Hiển thị danh sách công việc
    @GetMapping("/")
    public String listTodos(HttpSession session, Model model) {
        String ownerName = (String) session.getAttribute("ownerName");
        if (ownerName == null) {
            return "redirect:/welcome";
        }
        model.addAttribute("ownerName", ownerName);
        model.addAttribute("todos", todoRepository.findAll());
        return "list"; // Flash Attributes tự động có trong model, không cần xử lý thêm
    }

    // Hiển thị form thêm mới
    @GetMapping("/add")
    public String showAddForm(HttpSession session, Model model) {
        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }
        model.addAttribute("todo", new Todo());
        return "form";
    }

    // Lưu công việc (thêm mới hoặc cập nhật)
    @PostMapping("/save")
    public String saveTodo(@Valid @ModelAttribute("todo") Todo todo,
                           BindingResult result,
                           RedirectAttributes redirectAttributes,
                           HttpSession session,
                           Locale locale) {
        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }
        if (result.hasErrors()) {
            return "form";
        }
        todoRepository.save(todo);
        String msg = messageSource.getMessage("message.success", null, locale);
        redirectAttributes.addFlashAttribute("message", msg);
        return "redirect:/";
    }

    // Sửa công việc
    @GetMapping("/edit/{id}")
    public String editTodo(@PathVariable Long id, Model model,
                           RedirectAttributes redirectAttributes,
                           HttpSession session,
                           Locale locale) {
        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }
        Todo todo = todoRepository.findById(id).orElse(null);
        if (todo == null) {
            String msg = messageSource.getMessage("message.notfound", null, locale);
            redirectAttributes.addFlashAttribute("message", msg);
            return "redirect:/";
        }
        model.addAttribute("todo", todo);
        return "form";
    }

    // Xóa công việc
    @GetMapping("/delete/{id}")
    public String deleteTodo(@PathVariable Long id,
                             RedirectAttributes redirectAttributes,
                             HttpSession session,
                             Locale locale) {
        if (session.getAttribute("ownerName") == null) {
            return "redirect:/welcome";
        }
        if (todoRepository.existsById(id)) {
            todoRepository.deleteById(id);
            String msg = messageSource.getMessage("message.delete.success", null, locale);
            redirectAttributes.addFlashAttribute("message", msg);
        } else {
            String msg = messageSource.getMessage("message.notfound", null, locale);
            redirectAttributes.addFlashAttribute("message", msg);
        }
        return "redirect:/";
    }
}