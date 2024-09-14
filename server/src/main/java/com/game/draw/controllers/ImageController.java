package com.game.draw.controllers;

import com.game.draw.dto.QueryDTO;
import com.game.draw.services.FusionBrain;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping("/image")
public class ImageController {
    private final FusionBrain fusionBrain;

    public ImageController(FusionBrain fusionBrain) {
        this.fusionBrain = fusionBrain;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String, String>> generate(@RequestBody @Valid QueryDTO dto) {
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
}
