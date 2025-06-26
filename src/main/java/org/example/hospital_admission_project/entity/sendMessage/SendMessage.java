package org.example.hospital_admission_project.entity.sendMessage;

import java.io.Serializable;

public record SendMessage(boolean success, String message, Object data) implements Serializable {

}
