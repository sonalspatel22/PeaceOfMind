package com.sonal.apple.peaceofmind.Event;

import com.sonal.apple.peaceofmind.model.playlistmodel;

public class AddEvent {
    public static int ActionAdd = 1;
    private playlistmodel plmodel;

    public AddEvent(int i, playlistmodel playlistmodel) {
        this.ActionAdd = i;
        this.plmodel = playlistmodel;
    }

    public int getActionAdd() {
        return ActionAdd;
    }

    public playlistmodel getplmodel() {
        return plmodel;
    }
}
