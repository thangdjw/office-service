package office.module.excel.service.broadcast;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

// broadcast to multi channel
public class ExportBroadcastService {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    public void completeTask(String message, Object payload){
        simpMessagingTemplate.convertAndSend("/office-topic/extract-sheet");
    }

    public void cancelTask(String error, Object payload){

    }

}
