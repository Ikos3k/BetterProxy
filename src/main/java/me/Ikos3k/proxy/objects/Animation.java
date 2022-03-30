package me.Ikos3k.proxy.objects;

import lombok.Data;

@Data
public class Animation {
    private String[] text;
    private int currentIndex = 0;

    public Animation(String... text) {
        this.text = text;
    }

    public void nextFrame() {
        if(currentIndex == text.length - 1) {
            currentIndex = 0;
        }

        currentIndex++;
    }

    public void update(String... text) {
        this.text = text;
    }

    public void set(int index, String text) {
        this.text[index] = text;
    }

    public String getText() {
        return text[currentIndex];
    }
}