package com.chaosbuffalo.mkultra.party;

import java.util.UUID;

/**
 * Created by Jacob on 7/30/2016.
 */
public class PartyData {

    private UUID invitingUUID;
    private String invitingName;

    public PartyData() {
    }

    public String getInvitingName() {
        return this.invitingName;
    }

    public void setInvitingName(String name) {
        this.invitingName = name;
    }

    public UUID getInvitingUUID() {
        return this.invitingUUID;
    }

    public void setInvitingUUID(UUID uuid) {
        this.invitingUUID = uuid;
    }
}
