package elfak.diplomski.RoomBooker.externalCalendar;

import database.tables.pojos.User;
import elfak.diplomski.RoomBooker.models.ExternalCalendarRequest;
import elfak.diplomski.RoomBooker.models.ReservationsWithAdditionalInfo;
import elfak.diplomski.RoomBooker.query.IReservationQuery;
import elfak.diplomski.RoomBooker.query.IUserQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ExternalCalendarService {

    @Autowired
    IUserQuery userQuery;

    @Autowired
    IReservationQuery reservationQuery;
    private static final String URL = "https://www.googleapis.com/calendar/v3/calendars/djurke98@gmail.com/events";

    public void insertEvent(/*ReservationsWithAdditionalInfo reservation*/) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://www.googleapis.com/calendar/v3/calendars/primary/events";
        String accessToken = "ya29.a0AXooCgsLrLn2vKJxBxxQNWt9Hyjqwdz14smYiUJh50ZqEuRexGAq0u4RTZDxwQRyLsHoicPiXLp-S6eh8O7HJbVCiW2Yhuq6U5yG2lfMPoVOlS0xTDWxg4CG5BYmxRV4t14OAlwwtq9r7c1sMppGcSM4o7l6GocV6CtNaCgYKAQ4SARISFQHGX2Mim6M3tJ1O6FQO4TmRAxoFwg0171";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "OAuth2 " + accessToken);
        headers.set("Content-Type", "application/json");

//        AuthorizationCodeResourceDetails radiPls = new AuthorizationCodeResourceDetails();
//        radiPls.setId("roombooker-424821");
//        radiPls.setClientId("178430082395-ip4chk02agllkf4lus71dne0o1h8et7i.apps.googleusercontent.com");
//        radiPls.setClientSecret("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");
//        radiPls.setAccessTokenUri("https://oauth2.googleapis.com/token");
//        radiPls.setUserAuthorizationUri("https://accounts.google.com/o/oauth2/v2/auth");
//        radiPls.setTokenName("djurinToken");
//        radiPls.setPreEstablishedRedirectUri("http://localhost:8080");
//        radiPls.setScope(Arrays.asList("https://www.googleapis.com/auth/userinfo.email", " https://www.googleapis.com/auth/calendar"));

//        OAuth2RestTemplate template = new OAuth2RestTemplate(radiPls);
//        AccessTokenProvider accessTokenProvider = new AccessTokenProviderChain(
//                Arrays.<AccessTokenProvider>asList(
//                        new MyAuthorizationCodeAccessTokenProvider(),
//                        new ImplicitAccessTokenProvider(),
//                        new ResourceOwnerPasswordAccessTokenProvider(),
//                        new ClientCredentialsAccessTokenProvider())
//        );


        List<ReservationsWithAdditionalInfo> reservations = reservationQuery.getReservationsWithExternalData();
        ReservationsWithAdditionalInfo reservation = reservations.get(0);
        User user = userQuery.getUserByUuid(reservation.getUserUuid());

        ExternalCalendarRequest externalCalendarRequest = new ExternalCalendarRequest();
        externalCalendarRequest.setStartDateTime(reservation.getStartTime());
        externalCalendarRequest.setEndDateTime(reservation.getEndTime());

        HttpEntity<ExternalCalendarRequest> externalCalendarRequestHttpEntity = new HttpEntity(externalCalendarRequest);
        String body = "{\"end\": {\"date\": \"2024-06-01T14:00:00\", \"timeZone\": \"Europe/Belgrade\"},"
                + "\"start\": {\"date\": \"2024-06-01T13:00:00\",  \"timeZone\": \"Europe/Belgrade\"}}";

        ResponseEntity<String> exchange = restTemplate.exchange(URL, HttpMethod.POST, new HttpEntity<>(body, headers), String.class);
        System.out.println(exchange);

    }

}
