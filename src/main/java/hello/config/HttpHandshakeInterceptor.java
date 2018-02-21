package hello.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.Map;

public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    Logger logger = LoggerFactory.getLogger(HttpHandshakeInterceptor.class);
    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map attributes) throws Exception {
        logger.info("beforeHandshake >> attributes: {}\n request: {} ", attributes, request);
        if (request instanceof ServletServerHttpRequest) {
            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
            HttpSession session = servletRequest.getServletRequest().getSession();
            Principal principal = servletRequest.getPrincipal();
            logger.info("principal>> {}", principal);
            logger.info("sessionId>> {}", session.getId());
            /*
            * 여기서 attributes 에 넣어주는 값이 나중에 Event의 헤더영역에 항상 포함되어 날아간다.
            * handleWebSocketSubscribeListener 에서 사용하게 됨.
            * */
            attributes.put("sessionId", session.getId());
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse, WebSocketHandler webSocketHandler, Exception e) {

    }
}
