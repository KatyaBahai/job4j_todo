package ru.job4j.todo.controller;

import lombok.AllArgsConstructor;
import net.jcip.annotations.ThreadSafe;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Task;
import ru.job4j.todo.service.TaskService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@AllArgsConstructor
@ThreadSafe
@RequestMapping("/tasks")
public class TaskController {
    TaskService taskService;

    @GetMapping("/add")
    public String getCreationPage() {
        return "tasks/create";
    }

    @PostMapping("/add")
    public String addNewTask(Model model, @ModelAttribute Task task) {
        var savedTask = taskService.add(task);
        if (savedTask.isEmpty()) {
            model.addAttribute("message", "There's been some problem. Please try again.");
            return "errors/404";
        }
        return "redirect:/index";
    }

    @GetMapping("/list/{filter}")
    public String getAllTasks(@PathVariable String filter, Model model) {
        List<Task> allTasks = taskService.findAll();
        List<Task> filteredTasks = switch (filter) {
            case "all" -> allTasks;
            case "pending" -> allTasks.stream()
                    .filter(task -> !task.getDone())
                    .collect(Collectors.toList());
            case "completed" -> allTasks.stream()
                    .filter(Task::getDone)
                    .collect(Collectors.toList());
            default -> allTasks;
        };

        model.addAttribute("tasks", filteredTasks);
        return "tasks/list";
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
        return "tasks/edit";
    }

    @PostMapping("/done")
    public String changeDone(@RequestParam int id, Model model) {
        Optional<Task> existingTask = taskService.findById(id);
        if (existingTask.isEmpty()) {
            model.addAttribute("message", "There's no task to edit with this identifier.");
            return "/errors/404";
        }
        Task updatedTask = existingTask.get();
        updatedTask.setDone(!updatedTask.getDone());
        Optional<Task> savedTask = taskService.edit(updatedTask);
        if (savedTask.isEmpty()) {
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
}
