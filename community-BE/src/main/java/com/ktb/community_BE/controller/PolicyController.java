package com.ktb.community_BE.controller;

import com.ktb.community_BE.service.PolicyService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@Controller
@RequestMapping("/api/v1/policy")
@RequiredArgsConstructor
public class PolicyController {

    private final PolicyService policyService;


    //이용약관
    @GetMapping("/terms")
    public String termsPage(Model model) throws IOException {
        model.addAttribute("termsSections", policyService.getTermsSections());
        return "/terms";
    }

    //개인정보
    @GetMapping("/privacy")
    public String privacyPage(Model model) throws Exception {
        model.addAttribute("privacySections", policyService.getPrivacySections());
        return "/privacy";
    }
}