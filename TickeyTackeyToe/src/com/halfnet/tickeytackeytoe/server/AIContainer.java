package com.halfnet.tickeytackeytoe.server;

import com.halfnet.tickeytackeytoe.access.CommandSupplier;

class AIContainer {

    private CommandSupplier commandSupplier;
    private final String name;
    public transient int score;

    public AIContainer(CommandSupplier cs) {
        this.commandSupplier = cs;
        this.name = cs.getClass().getSimpleName();
    }

    public CommandSupplier getCommandSupplier() {
        return commandSupplier;
    }

    public String getName() {
        return name;
    }

    void setCommandSupplier(CommandSupplier cs) {
        this.commandSupplier = cs;
    }
}
