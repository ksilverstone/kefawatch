package com.kefawatch.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank(message = "Kullanıcı adı boş bırakılamaz") @Size(min = 3, max = 64, message = "Kullanıcı adı 3-64 karakter arasında olmalıdır") String username,
        @NotBlank(message = "Email boş bırakılamaz") @Email(message = "Geçerli bir email adresi giriniz") @Size(max = 128) String email,
        @NotBlank(message = "İsim boş bırakılamaz") @Size(max = 64) String firstName,
        @NotBlank(message = "Soyisim boş bırakılamaz") @Size(max = 64) String lastName,
        @NotBlank(message = "Şifre boş bırakılamaz") @Size(min = 6, max = 128, message = "Şifre en az 6 karakter olmalıdır") String password
) {
}
