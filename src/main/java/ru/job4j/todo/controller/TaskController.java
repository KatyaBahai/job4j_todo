package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.priority.PriorityService;
import ru.job4j.todo.service.task.TaskService;

import java.util.Optional;

@Controller
@AllArgsConstructor
@ThreadSafe
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final PriorityService priorityService;

    @GetMapping("/add")
    public String getCreationPage(Model model) {
        model.addAttribute("priorities", priorityService.findAll());
        return "tasks/create";
    }

    @PostMapping("/add")
    public String addNewTask(@SessionAttribute User wowUser, Model model, @ModelAttribute Task task) {
        task.setUser(wowUser);
        var savedTask = taskService.add(task);
        if (savedTask.isEmpty()) {
            model.addAttribute("message", "There's been some problem. Please try again.");
            return "errors/404";
        }
        return "redirect:/index";
    }

    @GetMapping("/{id}")
    public String getTaskById(@PathVariable int id, Model model) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "There's no such task, sorry!");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        return "tasks/description";
    }

    @GetMapping("/edit/{id}")
    public String editTask(@PathVariable int id, Model model) {
        Optional<Task> taskOptional = taskService.findById(id);
        if (taskOptional.isEmpty()) {
            model.addAttribute("message", "There's no such task available, sorry!");
            return "errors/404";
        }
        model.addAttribute("task", taskOptional.get());
        model.addAttribute("priorities", priorityService.findAll());
        return "tasks/edit";
    }

    @PostMapping("/done")
    public String changeDone(@RequestParam int id, Model model) {
        boolean isUpdated = taskService.editDone(id);
        if (!isUpdated) {
            model.addAttribute("message", "Failed to update the task.");
            return "/errors/404";
        }
        return "redirect:/index";
    }

    @PostMapping("/edit")
    public String edit(@ModelAttribute Task task, Model model) {
        Optional<Task> editedTask = taskService.edit(task);
        if (editedTask.isEmpty()) {
            model.addAttribute("message", "There's no task to edit with this identifier.");
            return "/errors/404";
        }
        model.addAttribute("id", task.getId());
        return "redirect:/tasks/" + task.getId();
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable int id, Model model) {
        var isDeleted = taskService.deleteById(id);
        if (!isDeleted) {
            model.addAttribute("message", "The task with this identifier is not found");
            return "/errors/404";
        }
        return "redirect:/index";
    }

    @GetMapping("/list/all")
    public String getAllTasks(Model model) {
         model.addAttribute("tasks", taskService.findAll());
        return "tasks/list";
    }

    @GetMapping("/list/pending")
    public String getPendingTasks(Model model) {
        model.addAttribute("tasks", taskService.findPendingTasks());
        return "tasks/list";
    }

    @GetMapping("/list/completed")
    public String getCompletedTasks(Model model) {
        model.addAttribute("tasks", taskService.findCompletedTasks());
        return "tasks/list";
    }
}
