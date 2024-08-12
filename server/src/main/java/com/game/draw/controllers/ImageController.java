package com.game.draw.controllers;

import com.game.draw.dto.QueryDTO;
import com.game.draw.services.FusionBrain;
import com.game.draw.services.Unsplash;
import com.game.draw.util.ErrorResponse;
import com.game.draw.util.FusionBrainException;
import com.game.draw.util.UnsplashException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/image")
public class ImageController {
    private final Unsplash unsplash;
    private final FusionBrain fusionBrain;

    public ImageController(Unsplash unsplash, FusionBrain fusionBrain) {
        this.unsplash = unsplash;
        this.fusionBrain = fusionBrain;
    }

    @GetMapping("/template")
    public ResponseEntity<Map<String, String>> getTemplate() {
        Map<String, String> response = new HashMap<>();
        response.put("url", unsplash.getImageUrl());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generate(@RequestBody QueryDTO dto) {
        Map<String, String> response = new HashMap<>();
        response.put("uuid", fusionBrain.generate(dto.getQuery()));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/status/{uuid}")
    public ResponseEntity<Map<String, String>> checkStatus(@PathVariable String uuid) {
        Map<String, String> response = new HashMap<>();
        response.put("image", fusionBrain.checkGeneration(uuid));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UnsplashException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(FusionBrainException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
