package com.book.book.email;

public interface EmailSender {
    String send(String to, String email);
    String buildEmail(String name, String link);
}