package office.module.webhook.controller;

import office.module.webhook.model.request.WebhookClientRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WebhookAPI {

    @PostMapping
    public void registerWebhook(WebhookClientRequest webhook){

    }
}
