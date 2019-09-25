package office.module.callback.controller;

import office.module.callback.model.entity.CallbackRestModel;
import office.module.callback.model.request.CallbackRegisterRequest;
import office.module.callback.model.response.CallbackRestModelCollectionResponse;
import office.module.callback.service.CallbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/callback")
public class CallbackController {
    @Autowired
    private CallbackService callbackService;
    @PostMapping("/register")
    public ResponseEntity<String> registerCallback(@RequestBody CallbackRegisterRequest registerRequest) {
        try {
            callbackService.addCallback(registerRequest.getCallbackKey(), registerRequest);
            return ResponseEntity
                    .ok("register callback success for url : " + registerRequest.getCallbackKey());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("register callback failed for url : " + registerRequest.getCallbackKey());
        }
    }

    @GetMapping("/list")
    public ResponseEntity<CallbackRestModelCollectionResponse> listCallback() {
        Map<String, CallbackRestModel> list = callbackService.list();
        return ResponseEntity.ok(new CallbackRestModelCollectionResponse(list.values()));
    }
}
