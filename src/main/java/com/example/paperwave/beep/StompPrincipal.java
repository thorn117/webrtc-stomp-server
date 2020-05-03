package com.example.paperwave.beep;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;

@Data
@AllArgsConstructor
class StompPrincipal implements Principal {
    private String name;
}