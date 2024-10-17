package com.eduardo.discordapp.dto.request;

public record MessageDTO(
        String id,
        String content,
        String authorId,
        String authorName,
        String timestamp

) {
}
