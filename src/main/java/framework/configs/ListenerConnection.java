package framework.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Configuration
public class ListenerConnection {
	@EventListener
	private void handleSessionConnected(SessionConnectEvent event) {
		System.out.println("Cennect");
		@SuppressWarnings("rawtypes")
		Message msg = event.getMessage();
		StompHeaderAccessor accessor = StompHeaderAccessor.wrap(msg);
		System.out.println("SessionId: " + accessor.getSessionId());
		System.out.println("Username: " + accessor.getLogin());

	}

	@EventListener
	private void handleSessionDisconnect(SessionDisconnectEvent event) {
		System.out.println("SessionDisconnectEvent");
		System.out.println(event);
	}

}
