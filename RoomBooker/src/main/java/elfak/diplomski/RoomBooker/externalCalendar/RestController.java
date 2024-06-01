package elfak.diplomski.RoomBooker.externalCalendar;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

@org.springframework.web.bind.annotation.RestController(value = "/auth/callback")
public class RestController {
    @GetMapping("/aaa")
    public void callback(RequestBody body) {
        System.out.println(body);
    }
}
