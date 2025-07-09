package org.example.hospital_admission_project.entity.sendMessage;

import java.io.Serializable;

/**
 * SendMessage - foydalanuvchiga javob qaytarishda foydalaniladigan standart javob formati.
 *
 * @param success  Amaliyot muvaffaqiyatli bajarilganligini bildiruvchi qiymat.
 * @param message  Foydalanuvchiga ko‘rsatiladigan xabar.
 * @param data     Qo‘shimcha ma’lumot (token, foydalanuvchi ma’lumotlari, xatolik tafsilotlari va boshqalar).
 */
public record SendMessage(boolean success, String message, Object data) implements Serializable {

    // Muvaffaqiyatli javob yaratish uchun yordamchi konstruktorlar:

    public static SendMessage success(String message) {
        return new SendMessage(true, message, null);
    }

    public static SendMessage success(String message, Object data) {
        return new SendMessage(true, message, data);
    }

    // Xato bo'lgan javoblar uchun yordamchi konstruktorlar:

    public static SendMessage failure(String message) {
        return new SendMessage(false, message, null);
    }
    public static SendMessage failure(String message, Object data) {
        return new SendMessage(false, message, data);
    }
}
