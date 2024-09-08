package com.game.draw.config;

import com.game.draw.handlers.ImageWebSocketHandler;
import com.game.draw.handlers.PlayersWebSocketHandler;
import com.game.draw.handlers.RoomsWebSocketHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ImageWebSocketHandler imageHandler;
    private final RoomsWebSocketHandler roomsHandler;
    private final PlayersWebSocketHandler playersHandler;

    public WebSocketConfig(ImageWebSocketHandler imageHandler, RoomsWebSocketHandler roomsHandler, PlayersWebSocketHandler playersHandler) {
        this.imageHandler = imageHandler;
        this.roomsHandler = roomsHandler;
        this.playersHandler = playersHandler;
    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(imageHandler, "/ws/images")
                .setAllowedOrigins("*");

        registry.addHandler(roomsHandler, "/ws/rooms")
                .setAllowedOrigins("*");

        registry.addHandler(playersHandler, "/ws/players")
                .setAllowedOrigins("*");
    }

    @Bean
    public ServletServerContainerFactoryBean createWebSocketContainer() {
        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
        container.setMaxTextMessageBufferSize(1024 * 1024);
        return container;
    }
}
