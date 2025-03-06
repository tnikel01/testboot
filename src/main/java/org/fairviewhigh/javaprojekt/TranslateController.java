package org.fairviewhigh.javaprojekt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TranslateController {
    @GetMapping("/")
    public String index() {
        return "index";
    }
}