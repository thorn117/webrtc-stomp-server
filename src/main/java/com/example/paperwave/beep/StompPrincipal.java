package com.example.paperwave.beep;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.security.Principal;
import java.util.UUID;

@Data
@AllArgsConstructor
class StompPrincipal implements Principal {
    private String name;
    private String publicName;
}