package entity;

import java.time.LocalDateTime;

import enums.LockerSize;

public class Package {
    private String packageId;
    private String recipientName;
    private String recipientNumber;
    private String recipientEmail;
    public LocalDateTime entryTime; // Changed to public for demo purposes
    private LockerSize size;

    public Package(String packageId, String recipientName, String recipientNumber, String recipientEmail, LockerSize size) {
        this.packageId = packageId;
        this.recipientName = recipientName;
        this.recipientNumber = recipientNumber;
        this.recipientEmail = recipientEmail;
        this.entryTime = LocalDateTime.now();
        this.size = size;
    }

    public String getId() {
        return packageId;
    }

    public LocalDateTime getEntryTime() {
        return entryTime;
    }

    public String getPackageId() {
        return packageId;
    }

    public LockerSize getSize() {
        return size;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public String getRecipientNumber() {
        return recipientNumber;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

}
