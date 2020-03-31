package com.example.renewseviceqq;



import androidx.annotation.NonNull;

import com.google.firebase.firestore.Exclude;

public class IDtech {

    @Exclude
    public String IDtech;

    public <T extends IDtech> T withId(@NonNull final String IDtech) {
        this.IDtech = IDtech;
        return (T) this;
    }

}